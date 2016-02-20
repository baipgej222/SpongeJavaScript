package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;
import io.github.djxy.javascript.models.sponge.CommandExecutor;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;

/**
 * Created by Samuel on 2016-02-18.
 */
public class CommandManager extends Manager {

    public CommandManager(Script script) {
        super(script);
        script.addVariable("commandManager", this);
    }

    public void register(ScriptObjectMirror scriptObjectMirror){
        if(!scriptObjectMirror.isArray()){
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
            }

            script.getGame().getCommandManager().register(script.getPlugin(), command.build(), getCommands(commands));
        }
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

}
