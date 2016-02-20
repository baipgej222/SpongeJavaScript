package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;

/**
 * Created by Samuel on 2016-02-18.
 */
public abstract class Manager {

    protected final Script script;

    public Manager(Script script) {
        this.script = script;
    }

}
