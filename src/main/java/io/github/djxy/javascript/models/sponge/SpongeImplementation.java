package io.github.djxy.javascript.models.sponge;

import io.github.djxy.javascript.models.javascript.JavascriptObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Created by Samuel on 2016-02-19.
 */
public abstract class SpongeImplementation {

    protected final ScriptObjectMirror executor;

    public SpongeImplementation(ScriptObjectMirror executor) {
        this.executor = executor;
    }

    public void callFunction(JavascriptObject... javascriptObjects){
        executor.call(executor, javascriptObjects);
    }

}
