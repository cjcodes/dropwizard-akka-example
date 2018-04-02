package com.cjcodes.actors;

import java.util.ArrayList;
import java.util.List;

import com.cjcodes.actors.messages.IdCreate;
import com.cjcodes.actors.messages.IdDelete;
import com.cjcodes.actors.messages.IdGet;
import com.cjcodes.actors.messages.IdList;

import akka.actor.AbstractLoggingActor;
import akka.actor.Status.Failure;

public class Datastore extends AbstractLoggingActor {
    private List<String> ids = new ArrayList<String>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(IdCreate.class, this::onCreate)
            .match(IdList.class, this::onList)
            .match(IdGet.class, this::onGet)
            .match(IdDelete.class, this::onDelete)
            .build();
    }

    private void onCreate(final IdCreate idMessage) {
        final String id = idMessage.getId();

        log().debug("Creating new id: {}", id);

        ids.add(id);
        getSender().tell(id, getSelf());
    }

    private void onList(final IdList idMessage) {
        log().debug("Listing ids ({})", ids.size());
        getSender().tell(ids, getSelf());
    }

    private void onGet(final IdGet idMessage) {
        final String id = idMessage.getId();

        log().debug("Retrieving id: {}", id);

        if (ids.contains(id)) {
            log().debug("Found id: {}", id);
            getSender().tell(id, getSelf());
        } else {
            log().warning("Did not find: {}", id);
            getSender().tell(new Failure(new Exception()), getSelf());
        }
    }

    private void onDelete(final IdDelete idMessage) {
        final String id = idMessage.getId();

        log().debug("Deleting id: {}", id);

        if (ids.contains(id)) {
            ids.remove(id);
            log().debug("Deleted id: {}", id);
            getSender().tell(id, getSelf());
        } else {
            log().warning("Did not find: {}", id);
            getSender().tell(new Failure(new Exception()), getSelf());
        }
    }
}
