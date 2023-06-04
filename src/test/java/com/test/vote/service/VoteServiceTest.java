package com.test.vote.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.test.vote.domain.Vote;
import com.test.vote.domain.request.VoteRequest;
import com.test.vote.domain.VotingSession;
import com.test.vote.domain.enums.VoteEnum;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.VoteRepository;
import com.test.vote.repository.VotingSessionRepository;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

	@Mock
	private VotingSessionRepository votingSessionRepository;

	@Mock
	private VoteRepository voteRepository;

	@InjectMocks
	private VoteService voteService;

	@Test
	void testProcessVote() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdVotingSession("voting-session-id");
		voteRequest.setIdAssociate("associate-id");
		voteRequest.setVote(VoteEnum.YES);

		VotingSession votingSession = new VotingSession();
		votingSession.setIdAgenda("agenda-id");

		when(voteService.validateVote(voteRequest)).thenReturn(votingSession);

		// Execute
		voteService.processVote(voteRequest);

		// Verify
		verify(voteRepository, times(1)).save(any(Vote.class));
	}

	@Test
	void testValidateVote_WhenVotingSessionExistsAndIsOpen() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdVotingSession("voting-session-id");
		voteRequest.setIdAssociate("associate-id");
		voteRequest.setVote(VoteEnum.YES);

		VotingSession votingSession = new VotingSession();
		votingSession.setFinishIn(LocalDateTime.now().plusMinutes(10));

		when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.of(votingSession));

		// Execute
		VotingSession result = voteService.validateVote(voteRequest);

		// Verify
		assertNotNull(result);
		verify(votingSessionRepository, times(1)).findById(voteRequest.getIdVotingSession());
	}

	@Test
	void testValidateVote_WhenVotingSessionDoesNotExist() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdVotingSession("voting-session-id");
		voteRequest.setIdAssociate("associate-id");
		voteRequest.setVote(VoteEnum.YES);

		when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.empty());

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				voteService.validateVote(voteRequest));

		assertEquals("Voting session does not exist for id sent.", exception.getMessage());
		verify(votingSessionRepository, times(1)).findById(voteRequest.getIdVotingSession());
	}

	@Test
	void testValidateVote_WhenVotingSessionIsClosed() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdVotingSession("voting-session-id");
		voteRequest.setIdAssociate("associate-id");
		voteRequest.setVote(VoteEnum.YES);

		VotingSession votingSession = new VotingSession();
		votingSession.setFinishIn(LocalDateTime.now().minusMinutes(10));

		when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.of(votingSession));

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				voteService.validateVote(voteRequest));

		assertEquals("Agenda closed for voting", exception.getMessage());
		verify(votingSessionRepository, times(1)).findById(voteRequest.getIdVotingSession());
	}

	@Test
	void testValidIfMemberCanVote_WhenMemberHasNotVotedForAgenda() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdAssociate("associate-id");
		String idAgenda = "agenda-id";

		when(voteRepository.findFirstByIdAssociateAndIdAgenda(voteRequest.getIdAssociate(), idAgenda)).thenReturn(Optional.empty());

		// Execute
		assertDoesNotThrow(() -> voteService.validIfMemberCanVote(voteRequest, idAgenda));

		// Verify
		verify(voteRepository, times(1)).findFirstByIdAssociateAndIdAgenda(voteRequest.getIdAssociate(), idAgenda);
	}

	@Test
	void testValidIfMemberCanVote_WhenMemberHasVotedForAgenda() {
		// Prepare
		VoteRequest voteRequest = new VoteRequest();
		voteRequest.setIdAssociate("associate-id");
		String idAgenda = "agenda-id";

		when(voteRepository.findFirstByIdAssociateAndIdAgenda(voteRequest.getIdAssociate(), idAgenda))
				.thenReturn(Optional.of(new Vote()));

		// Execute and Verify
		BadRequestException exception = assertThrows(BadRequestException.class, () ->
				voteService.validIfMemberCanVote(voteRequest, idAgenda));

		assertEquals("Member has already voted for this Agenda", exception.getMessage());
		verify(voteRepository, times(1)).findFirstByIdAssociateAndIdAgenda(voteRequest.getIdAssociate(), idAgenda);
	}
}
