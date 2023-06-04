package com.test.vote.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.vote.domain.request.CloseVotingRequest;
import com.test.vote.domain.VotingSession;
import com.test.vote.repository.VoteRepositoryImpl;
import com.test.vote.repository.VotingSessionRepository;
import com.test.vote.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class CloseVotingServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(CloseVotingService.class);

	@Mock
	private VotingSessionRepository votingSessionRepository;

	@Mock
	private VoteRepositoryImpl voteRepository;

	@InjectMocks
	private CloseVotingService closeVotingService;

	@Test
	void testProcessCloseVotingSession_WhenWinnerIsYes() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		VotingSession votingSession = new VotingSession();
		votingSession.setId("voting-session-id");
		votingSession.setIdAgenda("agenda-id");
		votingSession.setFinishIn(LocalDateTime.now().minusMinutes(5));
		votingSession.setStartsIn(LocalDateTime.now().minusHours(1));

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.of(votingSession));

		when(voteRepository.countByFields("agenda-id", "YES")).thenReturn(5L);
		when(voteRepository.countByFields("agenda-id", "NO")).thenReturn(3L);

		// Execute
		String result = closeVotingService.processCloseVotingSession(closeVotingRequest);

		// Verify
		assertEquals("The 'YES' option was the winner.", result);
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
		verify(voteRepository, times(1)).countByFields("agenda-id", "YES");
		verify(voteRepository, times(1)).countByFields("agenda-id", "NO");
	}

	@Test
	void testProcessCloseVotingSession_WhenWinnerIsNo() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		VotingSession votingSession = new VotingSession();
		votingSession.setId("voting-session-id");
		votingSession.setIdAgenda("agenda-id");
		votingSession.setFinishIn(LocalDateTime.now().minusMinutes(5));
		votingSession.setStartsIn(LocalDateTime.now().minusHours(1));

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.of(votingSession));

		when(voteRepository.countByFields("agenda-id", "YES")).thenReturn(3L);
		when(voteRepository.countByFields("agenda-id", "NO")).thenReturn(5L);

		// Execute
		String result = closeVotingService.processCloseVotingSession(closeVotingRequest);

		// Verify
		assertEquals("The 'NO' option was the winner.", result);
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
		verify(voteRepository, times(1)).countByFields("agenda-id", "YES");
		verify(voteRepository, times(1)).countByFields("agenda-id", "NO");
	}

	@Test
	void testProcessCloseVotingSession_WhenTie() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		VotingSession votingSession = new VotingSession();
		votingSession.setId("voting-session-id");
		votingSession.setIdAgenda("agenda-id");
		votingSession.setFinishIn(LocalDateTime.now().minusMinutes(5));
		votingSession.setStartsIn(LocalDateTime.now().minusHours(1));

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.of(votingSession));

		when(voteRepository.countByFields("agenda-id", "YES")).thenReturn(4L);
		when(voteRepository.countByFields("agenda-id", "NO")).thenReturn(4L);

		// Execute
		String result = closeVotingService.processCloseVotingSession(closeVotingRequest);

		// Verify
		assertEquals("Tie in the voting.", result);
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
		verify(voteRepository, times(1)).countByFields("agenda-id", "YES");
		verify(voteRepository, times(1)).countByFields("agenda-id", "NO");
	}

	@Test
	void testValidateCloseVotingSession_WhenSessionExistsAndIsOpen() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		VotingSession votingSession = new VotingSession();
		votingSession.setId("voting-session-id");
		votingSession.setFinishIn(LocalDateTime.now().plusDays(2));

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.of(votingSession));

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				closeVotingService.validateCloseVotingSession(closeVotingRequest));

		assertEquals("Agenda is still open for voting and cannot be closed.", exception.getMessage());
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
	}

	@Test
	void testValidateCloseVotingSession_WhenSessionExistsAndIsClosed() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		VotingSession votingSession = new VotingSession();
		votingSession.setId("voting-session-id");
		votingSession.setFinishIn(LocalDateTime.now().minusMinutes(10));

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.of(votingSession));

		// Execute and Verify
		assertDoesNotThrow(() -> closeVotingService.validateCloseVotingSession(closeVotingRequest));
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
	}

	@Test
	void testValidateCloseVotingSession_WhenSessionDoesNotExist() {
		// Prepare
		CloseVotingRequest closeVotingRequest = new CloseVotingRequest();
		closeVotingRequest.setIdVotingSession("voting-session-id");

		when(votingSessionRepository.findById(closeVotingRequest.getIdVotingSession()))
				.thenReturn(Optional.empty());

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				closeVotingService.validateCloseVotingSession(closeVotingRequest));

		assertEquals("Voting session does not exist for id sent.", exception.getMessage());
		verify(votingSessionRepository, times(1)).findById(closeVotingRequest.getIdVotingSession());
	}
}
