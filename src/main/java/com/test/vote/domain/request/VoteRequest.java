package com.test.vote.domain.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.test.vote.domain.Vote;
import com.test.vote.domain.enums.VoteEnum;

public class VoteRequest {

    @NotBlank(message = "The associate identifier cannot be null or empty.")
    private String idAssociate;

    @NotBlank(message = "The voting session ID cannot be empty or null.")
    private String idVotingSession;

    @NotNull(message = "A vote is required.")
    private VoteEnum voteEnum;

    public String getIdAssociate() { return idAssociate; }
    public void setIdAssociate(final String idAssociate) {this.idAssociate = idAssociate;}

    public String getIdVotingSession() { return idVotingSession; }
    public void setIdVotingSession(String idVotingSession) { this.idVotingSession = idVotingSession;  }

    public VoteEnum getVote() { return voteEnum;}
    public void setVote(final VoteEnum voteEnum) { this.voteEnum = voteEnum; }

    public Vote toEntity(String idAgenda) {
        return new Vote(this.idAssociate, idAgenda, this.voteEnum);
    }
}
