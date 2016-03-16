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

import org.spongepowered.api.event.game.state.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-14.
 */
public class ScriptManager {

    private final ConcurrentHashMap<String,Object> variables;
    private final CopyOnWriteArrayList<Script> scripts;
    private final CopyOnWriteArrayList<String> javascriptCodes;

    public ScriptManager() {
        this.scripts = new CopyOnWriteArrayList<>();
        this.variables = new ConcurrentHashMap<>();
        this.javascriptCodes = new CopyOnWriteArrayList<>();
    }

    public void invoke(String function, Object... args){
        for(Script script : scripts)
            script.invoke(function, args);
    }

    public List<Script> getScripts(){
        return new ArrayList<>(scripts);
    }

    public void addCode(String code){
        javascriptCodes.add(code);

        for(Script script : scripts)
            script.addCode(code);
    }

    public void addVariable(String name, Object o){
        variables.put(name, o);

        for(Script script : scripts)
            script.addVariable(name, o);
    }

    public Script createScript(Object plugin, ArrayList<File> files){
        return createScript(plugin, UUID.randomUUID().toString(), files);
    }

    public Script createScript(Object plugin, String name, ArrayList<File> files){
        Script script = new Script(plugin, name, files);

        for(Map.Entry pairs : variables.entrySet())
            script.addVariable((String) pairs.getKey(), pairs.getValue());

        javascriptCodes.forEach(script::addCode);

        scripts.add(script);

        return script;
    }

    public void onGameConstructionEvent(GameConstructionEvent event){
        for(Script script : scripts)
            script.onGameConstructionEvent(event);
    }

    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        for(Script script : scripts)
            script.onGamePreInitializationEvent(event);
    }

    public void onGameInitializationEvent(GameInitializationEvent event){
        for(Script script : scripts)
            script.onGameInitializationEvent(event);
    }

    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        for(Script script : scripts)
            script.onGamePostInitializationEvent(event);
    }

    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        for(Script script : scripts)
            script.onGameLoadCompleteEvent(event);
    }

    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        for(Script script : scripts)
            script.onGameAboutToStartServerEvent(event);
    }

    public void onGameStartingServerEvent(GameStartingServerEvent event){
        for(Script script : scripts)
            script.onGameStartingServerEvent(event);
    }

    public void onGameStartedServerEvent(GameStartedServerEvent event){
        for(Script script : scripts)
            script.onGameStartedServerEvent(event);
    }

    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        for(Script script : scripts)
            script.onGameStoppingServerEvent(event);
    }

    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        for(Script script : scripts)
            script.onGameStoppedServerEvent(event);
    }

}