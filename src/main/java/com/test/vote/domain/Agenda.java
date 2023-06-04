package com.test.vote.domain;

import org.springframework.data.annotation.Id;

public class Agenda {

     @Id
     private String id;
     private String name;
     public Agenda() { }

     public Agenda(final String name) {
          this.name = name;
     }

     public String getId() { return id;}

     public void setId(final String id) { this.id = id;  }

     public String getName() { return name;}

     public void setName(final String name) {this.name = name;}

}
