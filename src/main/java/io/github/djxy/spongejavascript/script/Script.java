/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.script;

import io.github.djxy.spongejavascript.javascript.JavascriptObject;
import io.github.djxy.spongejavascript.script.exceptions.CodeException;
import io.github.djxy.spongejavascript.script.sponge.*;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
    private final Console console;
    private final Scheduler scheduler;
    private final ArrayList<File> files;
    private final ScriptEngine engine;

    protected Script(Object plugin, String name, ArrayList<File> files) {
        this.plugin = plugin;
        this.console = new Console(LoggerFactory.getLogger(name));
        this.name = name;
        this.files = files;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.fileManager = new FileManager(this);
        this.commandManager = new CommandManager(this);
        this.eventManager = new EventManager(this);
        this.scheduler = new Scheduler(plugin);

        addVariable("game", Sponge.getGame());
        addVariable("console", console);
        addVariable("scheduler", scheduler);

        try {
            engine.eval("var Javascript = Java.type('io.github.djxy.spongejavascript.javascript.JavascriptObject');");
            engine.eval("var JSON = Java.type('io.github.djxy.spongejavascript.script.util.JSONParser');");
            engine.eval("var Text = Java.type('org.spongepowered.api.text.Text');");
            engine.eval("var TextSerializers = Java.type('org.spongepowered.api.text.serializer.TextSerializers');");
            engine.eval("var TextColors = Java.type('org.spongepowered.api.text.format.TextColors');");
            engine.eval("var Player = Java.type('org.spongepowered.api.entity.living.player.Player');");
            engine.eval("function setInterval(callback, interval){return scheduler.setInterval(callback, interval);}");
            engine.eval("function setTimeout(callback, delay){return scheduler.setTimeout(callback, delay);}");
            engine.eval("function clearInterval(intervalId){Scheduler.getInstance().clearInterval(intervalId);}");
            engine.eval("function clearTimeout(timeoutId){Scheduler.getInstance().clearTimeout(timeoutId);}");
            engine.eval("function convertToJSObject(object){return Javascript.convertObjectToJSObject(object);}");
            engine.eval("function convertToObject(object){return Javascript.convertJSObjectToObject(object);}");
            engine.eval("function stringToText(text){return TextSerializers.FORMATTING_CODE.deserialize(text);}");

            for(File script : files)
                engine.eval(new FileReader(script));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVariable(String name, Object object){
        engine.put(name, JavascriptObject.convertObjectToJSObject(object));
    }

    public void addCode(String code) {
        try {
            engine.eval(code);
        } catch (ScriptException e) {
            throw new CodeException(code);
        }
    }

    public Object getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public Object invoke(String function, Object... args){
        try{
            Invocable invocable = (Invocable) engine;

            for(int i = 0; i < args.length; i++)
                args[i] = JavascriptObject.convertObjectToJSObject(args[i]);

            return invocable.invokeFunction(function, args);
        }catch (Exception e){}

        return null;
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
        this.economyManager = new EconomyService(this);//Need this event for the EconomyService to be ready.
        invoke("onGamePostInitializationEvent", event);
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
