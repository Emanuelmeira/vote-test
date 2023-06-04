package com.test.vote.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vote.domain.request.CloseVotingRequest;
import com.test.vote.domain.VotingSession;
import com.test.vote.domain.enums.VoteEnum;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.VoteRepositoryImpl;
import com.test.vote.repository.VotingSessionRepository;

@Service
public class CloseVotingService {

    private static final Logger logger = LoggerFactory.getLogger(CloseVotingService.class);
    private final VotingSessionRepository votingSessionRepository;
    private final VoteRepositoryImpl  voteRepository;
    @Autowired
    public CloseVotingService(
            final VotingSessionRepository votingSessionRepository,
            final VoteRepositoryImpl voteRepository) {
        this.votingSessionRepository = votingSessionRepository;
        this.voteRepository = voteRepository;
    }

    public String processCloseVotingSession(final CloseVotingRequest closeVotingRequest) {
        logger.info("Initiated the voting closing process.");
        var votingSession = this.validateCloseVotingSession(closeVotingRequest);
        return this.countTheWinner(votingSession);
    }

    public String countTheWinner(final VotingSession votingSession) {

        var yesCount = voteRepository.countByFields(votingSession.getIdAgenda(), VoteEnum.YES.name());
        var noCount = voteRepository.countByFields(votingSession.getIdAgenda(), VoteEnum.NO.name());

        String result;

        switch (Long.compare(yesCount, noCount)) {
            case 1:
                result = "The 'YES' option was the winner.";
                break;
            case -1:
                result = "The 'NO' option was the winner.";
                break;
            default:
                result = "Tie in the voting.";
                break;
        }

        return result;
    }

    public VotingSession validateCloseVotingSession(final CloseVotingRequest closeVotingRequest) {
        logger.info("Validations for initiated voting closing");
        final var votingSession = this.validateIfVotingSessionExist(closeVotingRequest);
        this.validateIfVotingSessionIsOpen(votingSession);
        return votingSession;
    }

    public VotingSession validateIfVotingSessionExist(final CloseVotingRequest closeVotingRequest) {
        return votingSessionRepository.findById(closeVotingRequest.getIdVotingSession())
                .orElseThrow(() -> new BadRequestException("Voting session does not exist for id sent."));
    }

    public void validateIfVotingSessionIsOpen(final VotingSession votingSession) {
        if(votingSession.getFinishIn().isAfter(LocalDateTime.now())){
            throw new BadRequestException("Agenda is still open for voting and cannot be closed.");
        }
    }
}