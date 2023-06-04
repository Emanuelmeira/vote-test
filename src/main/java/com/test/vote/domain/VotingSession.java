package com.test.vote.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class VotingSession {

    @Id
    private String id;
    private String idAgenda;
    private LocalDateTime finishIn;
    private LocalDateTime startsIn;

    public String getId() { return id; }

    public void setId(final String id) { this.id = id; }

    public LocalDateTime getFinishIn() { return finishIn; }

    public void setFinishIn(final LocalDateTime finishIn) { this.finishIn = finishIn; }

    public LocalDateTime getStartsIn() { return startsIn; }

    public void setStartsIn(final LocalDateTime startsIn) { this.startsIn = startsIn; }

    public String getIdAgenda() { return idAgenda;}

    public void setIdAgenda(final String idAgenda) {this.idAgenda = idAgenda; }
}
