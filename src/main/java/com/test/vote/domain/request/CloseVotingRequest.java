package com.test.vote.domain.request;

import javax.validation.constraints.NotBlank;

public class CloseVotingRequest {

    @NotBlank(message = "The voting session ID cannot be empty or null.")
    private String idVotingSession;

    public String getIdVotingSession() { return idVotingSession;  }

    public void setIdVotingSession(final String idVotingSession) {  this.idVotingSession = idVotingSession;  }
}
