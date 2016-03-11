package io.github.djxy.spongejavascript;

import io.github.djxy.spongejavascript.javascript.JavascriptObject;
import io.github.djxy.spongejavascript.sponge.*;
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
    private EconomyService economyManager;
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
        engine.eval("var Javascript = Java.type('io.github.djxy.spongejavascript.javascript.JavascriptObject');");
        engine.eval("var Scheduler = Java.type('io.github.djxy.spongejavascript.sponge.Scheduler');");
        engine.eval("var JSON = Java.type('io.github.djxy.spongejavascript.JSONParser');");
        engine.eval("var Text = Java.type('org.spongepowered.api.text.Text');");
        engine.eval("var TextColors = Java.type('org.spongepowered.api.text.format.TextColors');");
        engine.eval("var Player = Java.type('org.spongepowered.api.entity.living.player.Player');");
        engine.eval("function setInterval(callback, interval){return Scheduler.getInstance().setInterval(callback, interval);}");
        engine.eval("function setTimeout(callback, delay){return Scheduler.getInstance().setTimeout(callback, delay);}");
        engine.eval("function clearInterval(intervalId){Scheduler.getInstance().clearInterval(intervalId);}");
        engine.eval("function clearTimeout(timeoutId){Scheduler.getInstance().clearTimeout(timeoutId);}");

        for(File script : files)
            engine.eval(new FileReader(script));

    }

    public void addVariable(String name, Object object){
        engine.put(name, JavascriptObject.convertObjectToJSObject(object));
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

            invocable.invokeFunction("onGameConstructionEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGamePreInitializationEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameInitializationEvent(GameInitializationEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameInitializationEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        this.economyManager = new EconomyService(this);//Need this event for the EconomyService to be ready.

        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGamePostInitializationEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameLoadCompleteEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameAboutToStartServerEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameStartingServerEvent(GameStartingServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStartingServerEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameStartedServerEvent(GameStartedServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStartedServerEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStoppingServerEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        try{
            Invocable invocable = (Invocable) engine;

            invocable.invokeFunction("onGameStoppedServerEvent", JavascriptObject.convertObjectToJSObject(event));
        }catch (Exception e){}
    }

}
