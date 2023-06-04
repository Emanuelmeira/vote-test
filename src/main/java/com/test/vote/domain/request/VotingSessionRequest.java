package com.test.vote.domain.request;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.test.vote.domain.VotingSession;

public class VotingSessionRequest {

    @NotBlank(message = "The agenda identifier cannot be null or empty.")
    private String idAgenda;

    private LocalDateTime finishIn;

    public LocalDateTime getFinishIn() {
        return finishIn;
    }

    public void setFinishIn(final LocalDateTime finishIn) {
        this.finishIn = finishIn;
    }

    public String getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(final String idAgenda) {
        this.idAgenda = idAgenda;
    }

    public VotingSession toEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final var votingSession = new VotingSession();

        votingSession.setStartsIn(now);
        votingSession.setIdAgenda(this.getIdAgenda());

        Optional.ofNullable(this.getFinishIn())
                .ifPresentOrElse(votingSession::setFinishIn,
                        () -> votingSession.setFinishIn(now.plusMinutes(1)));

        return votingSession;
    }
}
