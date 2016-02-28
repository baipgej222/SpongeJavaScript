package io.github.djxy.javascript.models.sponge;

import io.github.djxy.javascript.models.javascript.JavascriptObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;

/**
 * Created by Samuel on 2016-02-18.
 */
public class EventListener extends SpongeImplementation implements org.spongepowered.api.event.EventListener {

    public EventListener(ScriptObjectMirror executor) {
        super(executor);
    }

    @Override
    public void handle(Event event) {
        try {
            if (!event.getCause().first(Player.class).isPresent())
                callFunction(new JavascriptObject(event));
            else
                callFunction(new JavascriptObject(event), new JavascriptObject(event.getCause().first(Player.class).get()));
        }catch(Exception e){e.printStackTrace();};
    }

}
