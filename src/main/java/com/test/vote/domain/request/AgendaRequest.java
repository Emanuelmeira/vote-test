package com.test.vote.domain.request;

import javax.validation.constraints.NotBlank;

import com.test.vote.domain.Agenda;

public class AgendaRequest {

     @NotBlank(message = "Attribute name is null or blank.")
     private String name;

     public String getName() { return name;}

     public void setName(final String name) {this.name = name;}

     public Agenda toEntity() {
          return new Agenda(this.name);
     }
}
