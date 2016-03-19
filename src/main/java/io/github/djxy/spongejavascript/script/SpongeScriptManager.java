package io.github.djxy.spongejavascript.script;

import io.github.djxy.spongejavascript.script.util.*;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuelmarchildon-lavoie on 16-03-16.
 */
public class SpongeScriptManager extends ScriptManager {

    public SpongeScriptManager() {
        super();
        addVariable("game", Sponge.getGame());

        addCode(SCHEDULER_FUNCTIONS);
        addCode(JAVASCRIPT);
        addCode(JSON);
        addCode(TEXT);
    }

    @Override
    public Script createScript(Object plugin, String name, ArrayList<File> files) {
        Script script = super.createScript(plugin, name, files);

        script.addVariable("scheduler", new Scheduler(plugin));
        script.addVariable("fileManager", new FileManager(script.getName()));
        script.addVariable("commandManager", new CommandManager(script.getPlugin()));
        script.addVariable("eventManager", new EventManager(script.getPlugin()));
        script.addVariable("console", new Console(LoggerFactory.getLogger(script.getName())));

        return script;
    }

    public void onGameConstructionEvent(GameConstructionEvent event){
        invoke("onGameConstructionEvent", event);
    }

    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        invoke("onGamePreInitializationEvent", event);
    }

    public void onGameInitializationEvent(GameInitializationEvent event){
        invoke("onGameInitializationEvent", event);
    }

    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        invoke("onGamePostInitializationEvent", event);

        if(Sponge.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).isPresent())
            for(Script script : getScripts())
                script.addVariable("economyService", new EconomyService(script));
    }

    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        invoke("onGameLoadCompleteEvent", event);
    }

    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        invoke("onGameAboutToStartServerEvent", event);
    }

    public void onGameStartingServerEvent(GameStartingServerEvent event){
        invoke("onGameStartingServerEvent", event);
    }

    public void onGameStartedServerEvent(GameStartedServerEvent event){
        invoke("onGameStartedServerEvent", event);
    }

    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        invoke("onGameStoppingServerEvent", event);
    }

    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        invoke("onGameStoppedServerEvent", event);
    }

}
