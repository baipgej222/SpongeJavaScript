package io.github.djxy.javascript.views;

import com.google.inject.Inject;
import io.github.djxy.javascript.models.Script;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Samuel on 2016-02-07.
 */
@Plugin(id = "SpongeJavaScript", name = "Sponge JavaScript", version = "1.0")
public class Sponge {

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    private final ArrayList<Script> scripts = new ArrayList<>();

    @Listener
    public void onInitializationEvent(GameInitializationEvent event) throws Exception {
        File folder = new File("."+File.separator+"mods"+File.separator+"javascript");
        folder.mkdirs();

        for(File file : folder.listFiles()) {
            if(file.isDirectory()) {
                ArrayList<File> scripts = new ArrayList<>();

                for(File script : file.listFiles()) {
                    if (script.getName().endsWith(".js")) {
                            scripts.add(script);
                    }
                }

                try {
                    String name = getScriptName(file.getName());

                    this.scripts.add(new Script(this, name, game, logger, scripts));
                    logger.info(name + " loaded.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        for(Script script : scripts)
            script.onGameInitializationEvent(event);
    }

    @Listener
    public void onGameStartedServerEvent(GameStartedServerEvent event){
        for(Script script : scripts)
            script.onGameStartedServerEvent(event);
    }

    private String getScriptName(String name){
        if(name.length() == 1)
            return name.toUpperCase();
        else
            return name.substring(0,1).toUpperCase()+name.substring(1);
    }

}