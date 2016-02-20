package io.github.djxy.javascript.models.sponge;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samuel on 2016-02-18.
 */
public class EventListener extends SpongeImplementation implements org.spongepowered.api.event.EventListener {

    public EventListener(ScriptObjectMirror executor) {
        super(executor);
    }

    @Override
    public void handle(Event event) throws Exception {
        callFunction(createObject(event));
    }

    @Override
    protected Map<String, Object> createObject(Object... args) {
        HashMap<String,Object> map = new HashMap<>();
        Event event = (Event) args[0];

        map.put("event", event);
        map.put("cause", event.getCause());

        if(event.getCause().first(Player.class).isPresent())
            map.put("player", event.getCause().first(Player.class).get());

        return map;
    }
}
