package com.test.vote.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.vote.domain.Agenda;
import com.test.vote.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.test.vote.domain.request.AgendaRequest;
@RestController
@RequestMapping(value={"/v1"})
@Tag(name = "Agenda Service API V1" )
public class AgendaController {
    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);
    private final AgendaService agendaService;

    @Autowired
    public AgendaController(final AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @Operation(summary = "Registering a new agenda")
    @PostMapping(path = "/agenda")
    public ResponseEntity post(@RequestBody final AgendaRequest agendaRequest) {
        logger.info("Agenda creation process started");
        var agenda = agendaService.process(agendaRequest);
        logger.info("Agenda creation process finished");
        return ResponseEntity.created(URI.create("/agenda/" + agenda.getId())).build();
    }

    @Operation(summary = "Retrieve a agenda by id")
    @GetMapping(path = "/agenda/{id}")
    public ResponseEntity<Agenda> get(@PathVariable("id") final String idAgenda) {
        return ResponseEntity.ok(agendaService.get(idAgenda));
    }

    @Operation(summary = "Retrieve a list of agenda")
    @GetMapping(path = "/agenda")
    public ResponseEntity<Page<Agenda>> get(@RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(agendaService.getPageable(pageable));
    }

}
