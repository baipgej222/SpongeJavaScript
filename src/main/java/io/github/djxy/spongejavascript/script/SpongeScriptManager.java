package io.github.djxy.spongejavascript.script;

import io.github.djxy.spongejavascript.script.util.*;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by samuelmarchildon-lavoie on 16-03-16.
 */
public class SpongeScriptManager extends ScriptManager {

    public SpongeScriptManager() {
        addVariable("game", Sponge.getGame());

        addCode("var Javascript = Java.type('io.github.djxy.spongejavascript.javascript.JavascriptObject');");
        addCode("var JSON = Java.type('io.github.djxy.spongejavascript.script.util.JSONParser');");
        addCode("var Text = Java.type('org.spongepowered.api.text.Text');");
        addCode("var TextSerializers = Java.type('org.spongepowered.api.text.serializer.TextSerializers');");
        addCode("var TextColors = Java.type('org.spongepowered.api.text.format.TextColors');");
        addCode("var Player = Java.type('org.spongepowered.api.entity.living.player.Player');");
        addCode("function setInterval(callback, interval){return scheduler.setInterval(callback, interval);}");
        addCode("function setTimeout(callback, delay){return scheduler.setTimeout(callback, delay);}");
        addCode("function clearInterval(intervalId){Scheduler.getInstance().clearInterval(intervalId);}");
        addCode("function clearTimeout(timeoutId){Scheduler.getInstance().clearTimeout(timeoutId);}");
        addCode("function convertToJSObject(object){return Javascript.convertObjectToJSObject(object);}");
        addCode("function convertToObject(object){return Javascript.convertJSObjectToObject(object);}");
        addCode("function stringToText(text){return TextSerializers.FORMATTING_CODE.deserialize(text);}");

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

    @Override
    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        super.onGamePostInitializationEvent(event);

        if(Sponge.getGame().getServiceManager().provide(org.spongepowered.api.service.economy.EconomyService.class).isPresent())
            for(Script script : getScripts())
                script.addVariable("economyService", new EconomyService(script));
    }
}
