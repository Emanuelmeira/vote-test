package com.test.vote.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.test.vote.domain.Agenda;
import com.test.vote.domain.request.AgendaRequest;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.AgendaRepository;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private final AgendaRepository agendaRepository;

    @Autowired
    public AgendaService(final AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda process(final AgendaRequest agendaRequest) {
        return agendaRepository.save(agendaRequest.toEntity());
    }

    public Agenda get(final String idAgenda) {
        logger.info("Searching for agenda by Id");
        return agendaRepository.findById(idAgenda)
                .orElseThrow(() -> new BadRequestException("Agenda does not exist for id sent."));
    }

    public Page<Agenda> getPageable(Pageable pageable) {
        logger.info("Searching for agenda with page");
        return agendaRepository.findAll(pageable);
    }
}
