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
import io.github.djxy.core.CorePlugin;
import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.Command;
import io.github.djxy.core.commands.nodes.ChoiceNode;
import io.github.djxy.core.commands.nodes.Node;
import io.github.djxy.core.files.FileManager;
import io.github.djxy.core.translation.Translator;
import io.github.djxy.spongejavascript.commands.arguments.ScriptNode;
import io.github.djxy.spongejavascript.commands.executors.ReloadScriptExecutor;
import io.github.djxy.spongejavascript.script.ScriptService;
import io.github.djxy.spongejavascript.script.SpongeScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-02-07.
 */
@Plugin(id = "spongejavascript", name = "SpongeJavaScript", version = "v2.0.1", dependencies = @Dependency(id = "djxycore"))
public class Main implements CorePlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static Translator getTranslatorInstance(){
        return instance.getTranslator();
    }

    @Inject @DefaultConfig(sharedRoot = false) private Path path;
    private Translator translator = new Translator(TextSerializers.FORMATTING_CODE.deserialize("&f[&6Sponge&l&4JS&r&f] "));
    private Logger logger = LoggerFactory.getLogger("SpongeJavaScript");
    private SpongeScriptManager scriptManager;
    private File scriptFolder;

    @Listener
    public void onGameConstructionEvent(GameConstructionEvent event){
        instance = this;
        scriptManager = new SpongeScriptManager();

        initScriptFolder();
        loadScripts();

        Sponge.getServiceManager().setProvider(this, ScriptService.class, new ScriptService(scriptManager));

        Sponge.getCommandManager().register(this, new Command(createCommandJavaScript()), "js");

        scriptManager.onGameConstructionEvent(event);
    }

    public Node createCommandJavaScript(){
        return new ChoiceNode("").addNode(new ScriptNode("reload", "script", scriptManager).setExecutor(new ReloadScriptExecutor()));
    }

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event){
        scriptManager.onGamePreInitializationEvent(event);
    }

    @Listener
    public void onGameInitializationEvent(GameInitializationEvent event){
        scriptManager.onGameInitializationEvent(event);
    }

    @Listener
    public void onGamePostInitializationEvent(GamePostInitializationEvent event){
        scriptManager.onGamePostInitializationEvent(event);
    }

    @Listener
    public void onGameLoadCompleteEvent(GameLoadCompleteEvent event){
        scriptManager.onGameLoadCompleteEvent(event);
    }

    @Listener
    public void onGameAboutToStartServerEvent(GameAboutToStartServerEvent event){
        scriptManager.onGameAboutToStartServerEvent(event);
    }

    @Listener
    public void onGameStartingServerEvent(GameStartingServerEvent event){
        scriptManager.onGameStartingServerEvent(event);
    }

    @Listener
    public void onGameStartedServerEvent(GameStartedServerEvent event){
        scriptManager.onGameStartedServerEvent(event);
    }

    @Listener
    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        scriptManager.onGameStoppingServerEvent(event);
    }

    @Listener
    public void onGameStoppedServerEvent(GameStoppedServerEvent event){
        scriptManager.onGameStoppedServerEvent(event);
    }

    private void loadScripts(){
        for(File file : scriptFolder.listFiles()) {
            if(file.isDirectory()) {
                String name = getScriptName(file.getName());
                ArrayList<File> scripts = new ArrayList<>();

                for(File script : file.listFiles())
                    if (script.getName().endsWith(".js"))
                        scripts.add(script);

                try {
                    scriptManager.createScript(this, name, scripts);
                    logger.info(name + " loaded.");
                }catch (Exception e){
                    logger.error("Couldn't load "+name+".");
                }
            }
        }
    }

    private void initScriptFolder(){
        scriptFolder = new File("."+File.separator+"mods"+File.separator+"javascript");
        scriptFolder.mkdirs();
    }

    private String getScriptName(String name){
        if(name.length() == 1)
            return name.toUpperCase();
        else
            return name.substring(0,1).toUpperCase()+name.substring(1);
    }

    @Override
    public String getGithubApiURL() {
        return "https://api.github.com/repos/djxy/SpongeJavaScript";
    }

    @Override
    public Path getTranslationPath() {
        return path.getParent().resolve("translations");
    }

    @Override
    public Translator getTranslator() {
        return translator;
    }

    @Override
    public void loadTranslations() {
        CoreUtil.loadTranslationFiles(getTranslationPath(), translator);
    }

    @Override
    public List<FileManager> getFileManagers(Class<? extends FileManager>... classes) {
        return null;
    }

    @Override
    public FileManager getFileManager(String s, Class<? extends FileManager>... classes) {
        return null;
    }
}