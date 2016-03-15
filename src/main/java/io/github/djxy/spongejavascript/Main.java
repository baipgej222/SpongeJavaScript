/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript;

import com.google.inject.Inject;
import io.github.djxy.spongejavascript.sponge.Scheduler;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Samuel on 2016-02-07.
 */
@Plugin(id = "sponge_javascript", name = "Sponge JavaScript", version = "1.0")
public class Main {

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    private final ArrayList<Script> scripts = new ArrayList<>();

    @Listener
    public void onGameConstructionEvent(GameConstructionEvent event){
        Scheduler.createScheduler(this, org.spongepowered.api.Sponge.getScheduler());

        File folder = new File("."+File.separator+"mods"+File.separator+"spongejavascript");
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

                    this.scripts.add(new Script(this, name, game, scripts));
                    logger.info(name + " loaded.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        for(Script script : scripts)
            script.onGameConstructionEvent(event);
    }

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        for(Script script : scripts)
            script.onGamePreInitializationEvent(event);
    }

    @Listener
    public void onGameInitializationEvent(GameInitializationEvent event){
        for(Script script : scripts)
            script.onGameInitializationEvent(event);
    }

    @Listener
    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        for(Script script : scripts)
            script.onGamePostInitializationEvent(event);
    }

    @Listener
    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        for(Script script : scripts)
            script.onGameLoadCompleteEvent(event);
    }

    @Listener
    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        for(Script script : scripts)
            script.onGameAboutToStartServerEvent(event);
    }

    @Listener
    public void onGameStartingServerEvent(GameStartingServerEvent event){
        for(Script script : scripts)
            script.onGameStartingServerEvent(event);
    }

    @Listener
    public void onGameStartedServerEvent(GameStartedServerEvent event){
        for(Script script : scripts)
            script.onGameStartedServerEvent(event);
    }

    @Listener
    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        for(Script script : scripts)
            script.onGameStoppingServerEvent(event);
    }

    @Listener
    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        for(Script script : scripts)
            script.onGameStoppedServerEvent(event);
    }

    private String getScriptName(String name){
        if(name.length() == 1)
            return name.toUpperCase();
        else
            return name.substring(0,1).toUpperCase()+name.substring(1);
    }

}