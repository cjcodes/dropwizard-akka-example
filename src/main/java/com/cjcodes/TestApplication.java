package com.cjcodes;

import com.cjcodes.actors.Datastore;
import com.cjcodes.resources.TestResource;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<TestConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TestApplication().run(args);
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public void initialize(final Bootstrap<TestConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final TestConfiguration configuration,
                    final Environment environment) {

        final ActorSystem actorSystem = ActorSystem.create("akkasys");

        final ActorRef datastore = actorSystem.actorOf(Props.create(Datastore.class), "datastore");

        environment.jersey().register(new TestResource(datastore, configuration.TIMEOUT));
    }

}
