package com.test.vote.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vote.domain.request.VoteRequest;
import com.test.vote.domain.VotingSession;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.VoteRepository;
import com.test.vote.repository.VotingSessionRepository;

@Service
public class VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    private final VotingSessionRepository votingSessionRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(final VotingSessionRepository votingSessionRepository,
                       final VoteRepository voteRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.voteRepository = voteRepository;
    }

    public void processVote(final VoteRequest voteRequest) {
        var votingSession = this.validateVote(voteRequest);
        var vote = voteRequest.toEntity(votingSession.getIdAgenda());
        voteRepository.save(vote);
    }

    public VotingSession validateVote(final VoteRequest voteRequest) {
        logger.info("Validation process for vote started.");
        var votingSession = this.validateIfSessionExist(voteRequest);
        this.validateIfSessionIsOpen(votingSession);
        this.validIfMemberCanVote(voteRequest, votingSession.getIdAgenda());
        logger.info("Validation process for vote finished.");
        return votingSession;
    }

    public VotingSession validateIfSessionExist(VoteRequest voteRequest) {
        return votingSessionRepository.findById(voteRequest.getIdVotingSession())
                .orElseThrow( () -> new BadRequestException("Voting session does not exist for id sent."));
    }

    public void validateIfSessionIsOpen(VotingSession votingSession) {
        if(votingSession.getFinishIn().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Agenda closed for voting");
        }
    }

    public void validIfMemberCanVote(final VoteRequest voteRequest, String idAgenda) {
        voteRepository.findFirstByIdAssociateAndIdAgenda(voteRequest.getIdAssociate(), idAgenda)
                .ifPresent(obj -> {
                    throw new BadRequestException("Member has already voted for this Agenda");
                });
    }
}
