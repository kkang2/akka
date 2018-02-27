package com.psj.akka.basic;

import java.util.concurrent.CompletionStage;

import com.psj.akka.domain.Alarm;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

public class HttpServerMinimalExampleTest extends AllDirectives {

  public static void main(String[] args) throws Exception {
    // boot up server using the route as defined below
    ActorSystem system = ActorSystem.create("v2x");

    final Http http = Http.get(system);
    final ActorMaterializer materializer = ActorMaterializer.create(system);

    //In order to access all directives we need an instance where the routes are define.
    HttpServerMinimalExampleTest app = new HttpServerMinimalExampleTest();

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
        ConnectHttp.toHost("localhost", 8080), materializer);

    System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
    System.in.read(); // let it run until user presses return

    binding
        .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
        .thenAccept(unbound -> system.terminate()); // and shutdown when done
  	}

  private Route createRoute() {
    return route(
    	// post 방식 [http://localhost:8080/alarm/occurred] : Json 데이터 받아서 다시 Json 형태로 리턴함
        path(PathMatchers.segment("alarm").slash("occurred"), () ->
	        post(() ->
		        entity(Jackson.unmarshaller(Alarm.class), alarm ->
		        	complete(StatusCodes.OK, alarm, Jackson.<Alarm>marshaller())))
		        	//complete(StatusCodes.OK, "user : " + alarm.getUser() + ", message : " + alarm.getMessage())))
        	)
    	);
  }
}

// 참고
// https://github.com/akka/akka-http/blob/master/akka-http-tests/src/main/java/akka/http/javadsl/server/examples/simple/SimpleServerApp.java