package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;
import io.github.djxy.javascript.models.sponge.EventListener;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Created by Samuel on 2016-02-18.
 */
public class EventManager extends Manager {

    public EventManager(Script script) {
        super(script);
        script.addVariable("eventManager", this);
    }

    public void register(ScriptObjectMirror scriptObjectMirror){
        if(!scriptObjectMirror.isArray()){
            ScriptObjectMirror listener = scriptObjectMirror.get("listener") != null?(ScriptObjectMirror) scriptObjectMirror.get("listener"):null;
            Class clazz = scriptObjectMirror.get("event") != null?(Class) scriptObjectMirror.get("event"):null;

            if(listener != null && clazz != null)
                script.getGame().getEventManager().registerListener(script.getPlugin(), clazz, new EventListener(listener));
        }
    }

}
