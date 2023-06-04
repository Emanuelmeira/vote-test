package com.test.vote.domain;

import org.springframework.data.annotation.Id;

import com.test.vote.domain.enums.VoteEnum;

public class Vote {

    @Id
    private String id;
    private String idAssociate;
    private String idAgenda;
    private VoteEnum voteEnum;

    public Vote(final String idAssociate, final String idAgenda, final VoteEnum voteEnum) {
        this.idAssociate = idAssociate;
        this.idAgenda = idAgenda;
        this.voteEnum = voteEnum;
    }

    public Vote() {  }

    public String getId() {return id;}

    public void setId(final String id) { this.id = id; }

    public String getIdAssociate() { return idAssociate;}

    public void setIdAssociate(final String idAssociate) { this.idAssociate = idAssociate; }

    public String getIdAgenda() { return idAgenda; }

    public void setIdAgenda(final String idAgenda) { this.idAgenda = idAgenda; }

    public VoteEnum getVoteEnum() { return voteEnum; }

    public void setVoteEnum(final VoteEnum voteEnum) { this.voteEnum = voteEnum;}

}
