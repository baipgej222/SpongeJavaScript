package io.github.djxy.javascript.models.sponge;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;

/**
 * Created by Samuel on 2016-02-19.
 */
public abstract class SpongeImplementation {

    protected final ScriptObjectMirror executor;

    protected abstract Map<String,Object> createObject(Object... args);

    public SpongeImplementation(ScriptObjectMirror executor) {
        this.executor = executor;
    }

    public void callFunction(Object map){
        executor.call(executor, map);
    }

}
