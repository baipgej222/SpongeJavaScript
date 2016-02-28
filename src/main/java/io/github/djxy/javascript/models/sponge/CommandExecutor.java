package io.github.djxy.javascript.models.sponge;

import io.github.djxy.javascript.models.javascript.JavascriptObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

import java.util.HashMap;

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
        callFunction(new JavascriptObject(commandSource), new JavascriptObject(commandContext));

        return CommandResult.success();
    }

}
