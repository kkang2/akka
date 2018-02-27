package com.psj.akka.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Alarm {
	final String user;
	final String message;
	
	@JsonCreator
	public Alarm(@JsonProperty("user") String user,
         @JsonProperty("message") String message) {
      this.user = user;
      this.message = message;
    }

	public String getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}
}
