package io.github.djxy.javascript.models.sponge;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samuel on 2016-02-18.
 */
public final class CommandExecutor extends SpongeImplementation implements org.spongepowered.api.command.spec.CommandExecutor {

    private final HashMap<String, Boolean> arguments;

    public CommandExecutor(ScriptObjectMirror executor) {
        super(executor);
        this.arguments = new HashMap<>();
    }

    public void addArgument(String name, boolean isOnlyOne){
        this.arguments.put(name, isOnlyOne);
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        callFunction(createObject(commandSource, commandContext));

        return CommandResult.success();
    }

    @Override
    protected Map<String,Object> createObject(Object... args){
        CommandSource commandSource = (CommandSource) args[0];
        CommandContext commandContext = (CommandContext) args[1];
        HashMap<String,Object> map = new HashMap<>();
        HashMap<String, Object> arguments = new HashMap<>();

        map.put("isPlayerSender", commandSource instanceof Player);
        map.put("player", commandSource instanceof Player ? commandSource : null);
        map.put("commandSource", commandSource);
        map.put("commandContext", commandContext);
        map.put("arguments", arguments);

        for(String argument : this.arguments.keySet()) {
            if(this.arguments.get(argument))
                arguments.put(argument, commandContext.getOne(argument).get());
            else
                arguments.put(argument, Arrays.asList(commandContext.getAll(argument).toArray()));
        }

        return map;
    }

}
