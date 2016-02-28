package io.github.djxy.javascript.models;

import io.github.djxy.javascript.models.javascript.JavascriptObject;
import io.github.djxy.javascript.models.managers.*;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.game.state.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Samuel on 2016-02-17.
 */
public class Script {

    private final String name;
    private final FileManager fileManager;
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private EconomyManager economyManager;
    private final Object plugin;
    private final Game game;
    private final Console console;
    private final ArrayList<File> files;
    private final ScriptEngine engine;

    public Script(Object plugin, String name, Game game, ArrayList<File> files) throws Exception {
        this.plugin = plugin;
        this.game = game;
        this.console = new Console(LoggerFactory.getLogger(name));
        this.name = name;
        this.files = files;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.fileManager = new FileManager(this);
        this.commandManager = new CommandManager(this);
        this.eventManager = new EventManager(this);

        addVariable("game", game);
        addVariable("console", console);
        engine.eval("var Javascript = Java.type('io.github.djxy.javascript.models.javascript.JavascriptObject');");
        engine.eval("var JSON = Java.type('io.github.djxy.javascript.models.JSONParser');");
        engine.eval("var Text = Java.type('org.spongepowered.api.text.Text');");
        engine.eval("var Player = Java.type('org.spongepowered.api.entity.living.player.Player');");

        for(File script : files)
            engine.eval(new FileReader(script));
    }

    public void addVariable(String name, Object object){
        engine.put(name, new JavascriptObject(object));
    }

    public Object getPlugin() {
        return plugin;
    }

    public Game getGame() {
        return game;
    }

    public String getName() {
        return name;
    }

    public void onGameConstructionEvent(GameConstructionEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameConstructionEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGamePreInitializationEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameInitializationEvent(GameInitializationEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameInitializationEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        this.economyManager = new EconomyManager(this);//Need this event for the EconomyService to be ready.

        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGamePostInitializationEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameLoadCompleteEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameAboutToStartServerEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameStartingServerEvent(GameStartingServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStartingServerEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameStartedServerEvent(GameStartedServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStartedServerEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStoppingServerEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStoppedServerEvent", new JavascriptObject(event));
        }catch (Exception e){}
    }

}
