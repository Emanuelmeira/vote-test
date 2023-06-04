package com.test.vote.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.vote.domain.request.CloseVotingRequest;
import com.test.vote.domain.request.VoteRequest;
import com.test.vote.domain.VotingSession;
import com.test.vote.domain.request.VotingSessionRequest;
import com.test.vote.service.CloseVotingService;
import com.test.vote.service.VoteService;
import com.test.vote.service.VotingSessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value={"/v1"})
@Tag(name = "Voting Service API V1" )
public class VotingController {

    private static final Logger logger = LoggerFactory.getLogger(VotingController.class);

    private final VotingSessionService votingSessionService;
    private final VoteService voteService;
    private final CloseVotingService closeVotingService;

    @Autowired
    public VotingController(final VotingSessionService votingSessionService,
                            final VoteService voteService,
                            final CloseVotingService closeVotingService) {
        this.votingSessionService = votingSessionService;
        this.voteService = voteService;
        this.closeVotingService = closeVotingService;
    }

    @Operation(summary = "Registering a new voting session")
    @PostMapping(path = "/session")
    public ResponseEntity post(@RequestBody final VotingSessionRequest votingSessionRequest) {
        logger.info("VotingSession creation process started");
        var votingSession = votingSessionService.processSession(votingSessionRequest);
        logger.info("VotingSession creation process finished");
        return ResponseEntity.created(URI.create("/session/" + votingSession.getId())).build();
    }

    @Operation(summary = "Retrieve a voting session by id")
    @GetMapping(path = "/session/{id}")
    public ResponseEntity<VotingSession> get(@PathVariable("id") final String idVotingSession) {
        return ResponseEntity.ok(votingSessionService.get(idVotingSession));
    }

    @Operation(summary = "Retrieve a list of voting session")
    @GetMapping(path = "/session")
    public ResponseEntity<Page<VotingSession>> get(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(votingSessionService.getPageable(pageable));
    }

    @Operation(summary = "Register a vote for the voting session informed")
    @PostMapping(path = "/vote")
    public ResponseEntity post(@RequestBody final VoteRequest voteRequest) {
        logger.info("Vote registration process started");
        voteService.processVote(voteRequest);
        logger.info("Vote registration process finished");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Close Voting")
    @PostMapping(path = "/close-voting")
    public ResponseEntity post(@RequestBody final CloseVotingRequest closeVotingRequest) {
        var result = closeVotingService.processCloseVotingSession(closeVotingRequest);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
