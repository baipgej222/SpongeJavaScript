/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.script.util;

import io.github.djxy.spongejavascript.script.Script;
import io.github.djxy.spongejavascript.javascript.JavascriptObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-02-18.
 */
public class CommandManager {

    private final Object plugin;
    private final List<CommandMapping> commandMappings;

    public CommandManager(Object plugin) {
        this.plugin = plugin;
        this.commandMappings = new CopyOnWriteArrayList<>();
    }

    public void register(ScriptObjectMirror scriptObjectMirror){
        String description = scriptObjectMirror.get("description") != null?(String) scriptObjectMirror.get("description"):null;
        String extDescription = scriptObjectMirror.get("extendedDescription") != null?(String) scriptObjectMirror.get("extendedDescription"):null;
        String permission = scriptObjectMirror.get("permission") != null?(String) scriptObjectMirror.get("permission"):null;
        ScriptObjectMirror executor = scriptObjectMirror.get("executor") != null?(ScriptObjectMirror) scriptObjectMirror.get("executor"):null;
        ScriptObjectMirror arguments = scriptObjectMirror.get("arguments") != null?(ScriptObjectMirror) scriptObjectMirror.get("arguments"):null;
        Object commands[] = scriptObjectMirror.get("commands") != null?((ScriptObjectMirror) scriptObjectMirror.get("commands")).values().toArray():new Object[0];

        CommandSpec.Builder command = CommandSpec.builder();

        if(description != null)
            command.description(Text.of(description));
        if(extDescription != null)
            command.extendedDescription(Text.of(extDescription));
        if(permission != null)
            command.permission(permission);
        if(executor != null) {
            CommandExecutor commandExecutor = new CommandExecutor(executor);
            command.executor(commandExecutor);

            if (arguments != null && arguments.isArray())
                setGenericArguments(command, arguments, commandExecutor);

            commandMappings.add(Sponge.getGame().getCommandManager().register(plugin, command.build(), getCommands(commands)).get());
        }
    }

    public void clearAll(){
        for(CommandMapping commandMapping : commandMappings)
            Sponge.getGame().getCommandManager().removeMapping(commandMapping);
    }

    private void setGenericArguments(CommandSpec.Builder command, ScriptObjectMirror genericArguments, CommandExecutor commandExecutor){
        ArrayList<CommandElement> commandElements = new ArrayList<>();

        for(Object object : genericArguments.values()){
            if(object instanceof ScriptObjectMirror){
                CommandElement type = getGenericArgumentsType((ScriptObjectMirror) object);

                if(type != null)
                    commandElements.add(getGenericArgumentsFormat((ScriptObjectMirror) object, type, commandExecutor));
            }
        }

        command.arguments(commandElements.toArray(new CommandElement[commandElements.size()]));
    }

    private CommandElement getGenericArgumentsFormat(ScriptObjectMirror object, CommandElement arg, CommandExecutor commandExecutor){
        Object obj = object.get("format");

        if(obj != null && obj instanceof String){
            String format = (String) obj;

            if(format.equalsIgnoreCase("onlyOne")) {
                commandExecutor.addArgument(arg.getKey().toPlain(), true);
                return GenericArguments.onlyOne(arg);
            }
            if(format.equalsIgnoreCase("allOf")) {
                commandExecutor.addArgument(arg.getKey().toPlain(), false);
                return GenericArguments.allOf(arg);
            }
            else {
                commandExecutor.addArgument(arg.getKey().toPlain(), true);
                return GenericArguments.onlyOne(arg);
            }
        }
        else {
            commandExecutor.addArgument(arg.getKey().toPlain(), true);
            return GenericArguments.onlyOne(arg);
        }
    }

    private CommandElement getGenericArgumentsType(ScriptObjectMirror object){
        Object objN = object.get("name");

        if(objN != null && objN instanceof String) {
            Object objT = object.get("type");
            String name = (String) objN;

            if (objT != null && objT instanceof String) {
                String type = (String) objT;

                if (type.equalsIgnoreCase("bool")) return GenericArguments.bool(Text.of(name));
                if (type.equalsIgnoreCase("string")) return GenericArguments.string(Text.of(name));
                if (type.equalsIgnoreCase("dimension")) return GenericArguments.dimension(Text.of(name));
                if (type.equalsIgnoreCase("doubleNum")) return GenericArguments.doubleNum(Text.of(name));
                if (type.equalsIgnoreCase("integer")) return GenericArguments.integer(Text.of(name));
                if (type.equalsIgnoreCase("location")) return GenericArguments.location(Text.of(name));
                if (type.equalsIgnoreCase("player")) return GenericArguments.player(Text.of(name));
                if (type.equalsIgnoreCase("playerOrSource")) return GenericArguments.playerOrSource(Text.of(name));
                if (type.equalsIgnoreCase("vector3d")) return GenericArguments.vector3d(Text.of(name));
                if (type.equalsIgnoreCase("world")) return GenericArguments.world(Text.of(name));
                else return GenericArguments.string(Text.of(name));
            } else
                return GenericArguments.string(Text.of(name));
        }
        else
            return null;
    }

    private ArrayList<String> getCommands(Object commands[]){
        ArrayList<String> comms = new ArrayList<>();

        for(Object object : commands)
            if(object instanceof String)
                comms.add((String) object);

        return comms;
    }

    public class CommandExecutor implements org.spongepowered.api.command.spec.CommandExecutor {

        private final HashMap<String, Boolean> arguments;
        private final ScriptObjectMirror executor;

        private CommandExecutor(ScriptObjectMirror executor) {
            this.executor = executor;
            this.arguments = new HashMap<>();
        }

        public void addArgument(String name, boolean isOnlyOne){
            this.arguments.put(name, isOnlyOne);
        }

        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
            executor.call(executor, JavascriptObject.convertObjectToJSObject(commandSource), JavascriptObject.convertObjectToJSObject(commandContext));

            return CommandResult.success();
        }
    }

}
