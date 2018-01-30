package com.psj.akka.execute;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AkkademyDb extends AbstractActor {
	protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
	protected final Map<String, Object> map = new HashMap<>();
	
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return null;
	}
	
}