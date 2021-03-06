package io.github.djxy.spongejavascript.script;

import io.github.djxy.spongejavascript.script.util.*;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.*;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by samuelmarchildon-lavoie on 16-03-16.
 */
public class SpongeScriptManager extends ScriptManager {

    public static final String SCHEDULER_FUNCTIONS = "function setInterval(callback, interval){return scheduler.setInterval(callback, interval);}" +
            "function setTimeout(callback, delay){return scheduler.setTimeout(callback, delay);}" +
            "function clearInterval(intervalId){Scheduler.getInstance().clearInterval(intervalId);}" +
            "function clearTimeout(timeoutId){Scheduler.getInstance().clearTimeout(timeoutId);}";
    public static final String JAVASCRIPT = "var Javascript = Java.type('io.github.djxy.spongejavascript.javascript.JavascriptObject');" +
            "function convertToJSObject(object){return Javascript.convertObjectToJSObject(object);}" +
            "function convertToObject(object){return Javascript.convertJSObjectToObject(object);}";
    public static final String JSON = "var JSON = Java.type('io.github.djxy.spongejavascript.script.util.JSONParser');";
    public static final String PLAYER = "var Player = Java.type('org.spongepowered.api.entity.living.player.Player');";
    public static final String TEXT = "var Text = Java.type('org.spongepowered.api.text.Text');" +
            "var TextSerializers = Java.type('org.spongepowered.api.text.serializer.TextSerializers');" +
            "var TextColors = Java.type('org.spongepowered.api.text.format.TextColors');" +
            "function stringToText(text){return TextSerializers.FORMATTING_CODE.deserialize(text);}";

    private final ConcurrentHashMap<Script, ScriptVariables> scriptVariables;
    private EconomyService economyService;

    public SpongeScriptManager() {
        super();
        scriptVariables = new ConcurrentHashMap<>();
        addVariable("game", Sponge.getGame());

        addCode(SCHEDULER_FUNCTIONS);
        addCode(JAVASCRIPT);
        addCode(JSON);
        addCode(TEXT);
        addCode(PLAYER);
    }

    @Override
    public Script createScript(Object plugin, String name, ArrayList<File> files) {
        Script script = super.createScript(plugin, name, files);
        ScriptVariables scriptVariables = new ScriptVariables(script);
        scriptVariables.update();

        this.scriptVariables.put(script, scriptVariables);

        return script;
    }

    @Override
    protected void reloadScript(Script script) {
        script.invoke("onStop");
        this.scriptVariables.get(script).clear();
        super.reloadScript(script);
        this.scriptVariables.get(script).update();
        script.invoke("onStart");
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
                script.addVariable("economyService", economyService = new EconomyService(script));
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

    private class ScriptVariables{

        private final Script script;
        private final FileManager fileManager;
        private final Console console;
        private CommandManager commandManager;
        private EventManager eventManager;
        private Scheduler scheduler;

        public ScriptVariables(Script script) {
            this.script = script;
            this.fileManager = new FileManager(script.getName());
            this.console = new Console(LoggerFactory.getLogger(script.getName()));
        }

        public void clear(){
            commandManager.clearAll();
            eventManager.clearAll();
            scheduler.clearAll();
        }

        public void update(){
            commandManager = new CommandManager(script.getPlugin());
            eventManager = new EventManager(script.getPlugin());
            scheduler = new Scheduler(script.getPlugin());

            script.addVariable("scheduler", scheduler);
            script.addVariable("fileManager", fileManager);
            script.addVariable("commandManager", commandManager);
            script.addVariable("eventManager", eventManager);
            script.addVariable("console", console);

            if(economyService != null)
                script.addVariable("economyService", economyService);
        }
    }
}
