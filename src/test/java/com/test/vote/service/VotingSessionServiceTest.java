package com.test.vote.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.test.vote.domain.Agenda;
import com.test.vote.domain.VotingSession;
import com.test.vote.domain.request.VotingSessionRequest;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.AgendaRepository;
import com.test.vote.repository.VotingSessionRepository;

@ExtendWith(MockitoExtension.class)
public class VotingSessionServiceTest {

	@Mock
	private AgendaRepository agendaRepository;

	@Mock
	private VotingSessionRepository votingSessionRepository;

	@InjectMocks
	private VotingSessionService votingSessionService;

	@Test
	void testGetPageable() {
		// Prepare
		Pageable pageable = Pageable.ofSize(10).withPage(0);
		when(votingSessionRepository.findAll(pageable)).thenReturn(mock(Page.class));

		// Execute
		Page<VotingSession> result = votingSessionService.getPageable(pageable);

		// Verify
		assertNotNull(result);
		verify(votingSessionRepository, times(1)).findAll(pageable);
	}

	@Test
	void testGet_WhenVotingSessionExists() {
		// Prepare
		String idAgenda = "agenda-id";
		VotingSession votingSession = new VotingSession();
		when(votingSessionRepository.findById(idAgenda)).thenReturn(Optional.of(votingSession));

		// Execute
		VotingSession result = votingSessionService.get(idAgenda);

		// Verify
		assertNotNull(result);
		verify(votingSessionRepository, times(1)).findById(idAgenda);
	}

	@Test
	void testGet_WhenVotingSessionDoesNotExist() {
		// Prepare
		String idAgenda = "agenda-id";
		when(votingSessionRepository.findById(idAgenda)).thenReturn(Optional.empty());

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				votingSessionService.get(idAgenda));

		assertEquals("voting Session not found for id sent", exception.getMessage());
		verify(votingSessionRepository, times(1)).findById(idAgenda);
	}

	@Test
	void testProcessSession() {
		// Prepare
		VotingSessionRequest votingSessionRequest = new VotingSessionRequest();
		votingSessionRequest.setIdAgenda("agenda-id");

		LocalDateTime now = LocalDateTime.now();
		when(agendaRepository.findById(votingSessionRequest.getIdAgenda())).thenReturn(Optional.of(new Agenda()));
		when(votingSessionRepository.save(any())).thenAnswer(invocation -> {
			VotingSession votingSession = invocation.getArgument(0);
			votingSession.setId("voting-session-id");
			votingSession.setStartsIn(now);
			return votingSession;
		});

		// Execute
		VotingSession result = votingSessionService.processSession(votingSessionRequest);

		// Verify
		assertNotNull(result);
		assertEquals("voting-session-id", result.getId());
		assertEquals(now, result.getStartsIn());
		verify(agendaRepository, times(1)).findById(votingSessionRequest.getIdAgenda());
		verify(votingSessionRepository, times(1)).save(any());
	}

	@Test
	void testValidateSession_WhenFinishDateIsPastDate() {
		// Prepare
		VotingSessionRequest votingSessionRequest = new VotingSessionRequest();
		votingSessionRequest.setIdAgenda("agenda-id");
		votingSessionRequest.setFinishIn(LocalDateTime.now().minusMinutes(10));

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				votingSessionService.validateSession(votingSessionRequest));

		assertEquals("The date sent to end the session is a past date", exception.getMessage());
	}

	@Test
	void testValidateSession_WhenAgendaDoesNotExist() {
		// Prepare
		VotingSessionRequest votingSessionRequest = new VotingSessionRequest();
		votingSessionRequest.setIdAgenda("agenda-id");
		votingSessionRequest.setFinishIn(LocalDateTime.now().plusMinutes(10));
		when(agendaRepository.findById(votingSessionRequest.getIdAgenda())).thenReturn(Optional.empty());

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				votingSessionService.validateSession(votingSessionRequest));

		assertEquals("Agenda not found for id sent", exception.getMessage());
		verify(agendaRepository, times(1)).findById(votingSessionRequest.getIdAgenda());
	}
}
