package com.test.vote.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.vote.domain.VotingSession;
import com.test.vote.domain.request.VotingSessionRequest;
import com.test.vote.exception.BadRequestException;

import com.test.vote.repository.AgendaRepository;
import com.test.vote.repository.VotingSessionRepository;

@Service
public class VotingSessionService {

    private static final Logger logger = LoggerFactory.getLogger(VotingSessionService.class);
    private final AgendaRepository agendaRepository;
    private final VotingSessionRepository votingSessionRepository;

    @Autowired
    public VotingSessionService(final AgendaRepository agendaRepository,
                                final VotingSessionRepository votingSessionRepository) {
        this.agendaRepository = agendaRepository;
        this.votingSessionRepository = votingSessionRepository;
    }

    public Page<VotingSession> getPageable(Pageable pageable) {
        logger.info("Searching for agenda with page");
        return votingSessionRepository.findAll(pageable);
    }

    public VotingSession get(final String idAgenda) {
        logger.info("Searching for voting session by Id");
        return votingSessionRepository.findById(idAgenda)
                .orElseThrow(() -> new BadRequestException("voting Session not found for id sent"));
    }
    public VotingSession processSession(final VotingSessionRequest votingSessionRequest) {
        this.validateSession(votingSessionRequest);
        return votingSessionRepository.save(votingSessionRequest.toEntity());
    }

    public void validateSession(final VotingSessionRequest votingSessionRequest) {
        logger.info("Validation process for voting session started.");
        this.validateIfFinishDateIsValid(votingSessionRequest);
        this.validateIfAgendaExist(votingSessionRequest);
        logger.info("Validation process for voting session finished.");
    }

    public void validateIfFinishDateIsValid(final VotingSessionRequest votingSessionRequest) {
        if(Objects.nonNull(votingSessionRequest.getFinishIn()) && isPastDate(votingSessionRequest.getFinishIn())){
            throw new BadRequestException("The date sent to end the session is a past date");
        }
    }

    public void validateIfAgendaExist(final VotingSessionRequest votingSessionRequest) {
        if(agendaRepository.findById(votingSessionRequest.getIdAgenda()).isEmpty()){
            throw new BadRequestException("Agenda not found for id sent");
        }
    }

    private boolean isPastDate(final LocalDateTime finishIn) {
        final LocalDateTime now = LocalDateTime.now();
        return finishIn.isBefore(now);
    }

}