package com.test.vote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.test.vote.domain.Agenda;
import com.test.vote.domain.request.AgendaRequest;
import com.test.vote.exception.BadRequestException;
import com.test.vote.repository.AgendaRepository;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceTest {

	@Mock
	private AgendaRepository agendaRepository;

	@InjectMocks
	private AgendaService agendaService;

	@Test
	void testProcess() {
		// Prepare
		AgendaRequest agendaRequest = new AgendaRequest();
		agendaRequest.setName("Test Agenda");
		Agenda savedAgenda = new Agenda();
		when(agendaRepository.save(any())).thenReturn(savedAgenda);

		// Execute
		Agenda result = agendaService.process(agendaRequest);

		// Verify
		verify(agendaRepository, times(1)).save(any());
		assertEquals(savedAgenda, result);
	}

	@Test
	void testGetExistingAgenda() {
		// Prepare
		String idAgenda = "agenda-id";
		Agenda agenda = new Agenda();
		when(agendaRepository.findById(idAgenda)).thenReturn(Optional.of(agenda));

		// Execute
		Agenda result = agendaService.get(idAgenda);

		// Verify
		verify(agendaRepository, times(1)).findById(idAgenda);
		assertEquals(agenda, result);
	}

	@Test
	void testGetNonExistingAgenda() {
		// Prepare
		String idAgenda = "agenda-id";
		when(agendaRepository.findById(idAgenda)).thenReturn(Optional.empty());

		// Verify
		assertThrows(BadRequestException.class, () -> agendaService.get(idAgenda));
		verify(agendaRepository, times(1)).findById(idAgenda);
	}

	@Test
	void testGetPageable() {
		// Prepare
		Pageable pageable = mock(Pageable.class);
		Page<Agenda> agendaPage = mock(Page.class);
		when(agendaRepository.findAll(pageable)).thenReturn(agendaPage);

		// Execute
		Page<Agenda> result = agendaService.getPageable(pageable);

		// Verify
		verify(agendaRepository, times(1)).findAll(pageable);
		assertEquals(agendaPage, result);
	}
}
