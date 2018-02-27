package com.psj.akka.client;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psj.akka.domain.Alarm;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpHeader;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.util.ByteString;

public class SimpleClient {

	public static void main(String[] args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Alarm al = new Alarm("Park", "Hi!!");
		
		final ActorSystem system = ActorSystem.create("v2x-client");
		final Materializer materializer = ActorMaterializer.create(system);
		
		final CompletionStage<HttpResponse> responseFuture =
			      Http.get(system)
			          .singleRequest(HttpRequest.create("http://localhost:8080/alarm/occurred")
			        		  .withMethod(HttpMethods.POST)
			        		  //.addHeader(HttpHeader.parse("Content-Type", "application/json"))
			        		  .withEntity(ContentTypes.APPLICATION_JSON, ByteString.fromString(objectMapper.writeValueAsString(al)))
			        	);
		
		System.out.println("요청보냄");
		//responseFuture.thenAccept(res -> System.out.println(res.entity().toString()));
		//responseFuture.thenAccept(res -> System.out.println(res.entity().getDataBytes().map(bs -> bs.utf8String()).toString()));
		responseFuture.thenAccept(res -> {
			try {
				//System.out.println(res.entity().toStrict(1000, materializer).toCompletableFuture().get().getData());
				System.out.println(Jackson.unmarshaller(Alarm.class).unmarshal(res.entity(), materializer).toCompletableFuture().get());
				System.out.println(res.entity().getDataBytes());
				System.out.println(res.entity().getContentType());
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		System.out.println("요청받음");
	}
}
