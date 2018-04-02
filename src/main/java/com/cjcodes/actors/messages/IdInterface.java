package com.cjcodes.actors.messages;

public abstract class IdInterface {
    private final String id;

    public IdInterface(String id) {
        this.id = id;
    }

    public final String getId() {
        return id;
    }
}
