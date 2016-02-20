package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Samuel on 2016-02-20.
 */
public class ServerManager extends Manager {

    public ServerManager(Script script) {
        super(script);
        script.addVariable("serverManager", this);
    }

    public Player getPlayer(String identifier){
        try {
            Optional<Player> player = script.getGame().getServer().getPlayer(UUID.fromString(identifier));

            if(player.isPresent())
                return player.get();
            else
                return null;
        }catch (Exception e){}

        Optional<Player> player = script.getGame().getServer().getPlayer(identifier);

        if(player.isPresent())
            return player.get();
        else
            return null;
    }

    public void broadcast(String message){
        script.getGame().getServer().getBroadcastChannel().send(Text.of(message));
    }

    public void broadcast(Text message){
        script.getGame().getServer().getBroadcastChannel().send(message);
    }

}
