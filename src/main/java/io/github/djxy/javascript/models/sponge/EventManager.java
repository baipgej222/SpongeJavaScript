package io.github.djxy.javascript.models.sponge;

import io.github.djxy.javascript.models.Script;
import io.github.djxy.javascript.models.javascript.JavascriptObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;

/**
 * Created by Samuel on 2016-02-18.
 */
public class EventManager {

    private final Script script;

    public EventManager(Script script) {
        this.script = script;
        script.addVariable("eventManager", this);
    }

    /*public void register(ScriptObject scriptObject){
        register(ScriptUtils.wrap(scriptObject));
    }*/

    public void register(ScriptObjectMirror scriptObjectMirror){
        if(!scriptObjectMirror.isArray()){
            ScriptObjectMirror listener = scriptObjectMirror.get("listener") != null?(ScriptObjectMirror) scriptObjectMirror.get("listener"):null;
            Class clazz = scriptObjectMirror.get("event") != null?(Class) scriptObjectMirror.get("event"):null;

            if(listener != null && clazz != null)
                script.getGame().getEventManager().registerListener(script.getPlugin(), clazz, new EventListener(listener));
        }
    }

    public class EventListener implements org.spongepowered.api.event.EventListener {

        private final ScriptObjectMirror executor;

        private EventListener(ScriptObjectMirror executor) {
            this.executor = executor;
        }

        @Override
        public void handle(Event event) {
            if (!event.getCause().first(Player.class).isPresent())
                executor.call(executor, new JavascriptObject(event));
            else
                executor.call(executor, new JavascriptObject(event), new JavascriptObject(event.getCause().first(Player.class).get()));
        }
    }

}
