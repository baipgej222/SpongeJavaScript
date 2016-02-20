package io.github.djxy.javascript.models.sponge;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.world.TargetWorldEvent;

import java.util.ArrayList;
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

        if(event instanceof TargetWorldEvent) {
            setMaTargetWorldEvent(map, (TargetWorldEvent) event);

            if (event instanceof ChangeBlockEvent)
                setMapChangeBlockEvent(map, (ChangeBlockEvent) event);
        }

        map.put("event", event);
        map.put("cause", event.getCause());

        if(event.getCause().first(Player.class).isPresent())
            map.put("player", event.getCause().first(Player.class).get());

        return map;
    }

    private void setMaTargetWorldEvent(Map<String, Object> map, TargetWorldEvent targetWorldEvent){
        map.put("world", targetWorldEvent.getTargetWorld());
    }

    private void setMapChangeBlockEvent(Map<String, Object> map, ChangeBlockEvent changeBlockEvent){
        ArrayList<BlockSnapshot> blocksBefore = new ArrayList<>();
        ArrayList<BlockSnapshot> blocksAfter = new ArrayList<>();

        for(Transaction<BlockSnapshot> blockSnapshot : changeBlockEvent.getTransactions()) {
            blocksAfter.add(blockSnapshot.getFinal());
            blocksBefore.add(blockSnapshot.getOriginal());
        }

        map.put("blocksBefore", blocksBefore);
        map.put("blocksAfter", blocksAfter);
    }
}
