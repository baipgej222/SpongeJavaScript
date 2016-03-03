package io.github.djxy.javascript.models.sponge;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by samuelmarchildon-lavoie on 16-03-03.
 */
public class Scheduler {

    private static Scheduler instance;

    public synchronized static Scheduler createScheduler(Object plugin, org.spongepowered.api.scheduler.Scheduler scheduler){
        if(instance == null)
            instance = new Scheduler(plugin, scheduler);

        return instance;
    }

    public static Scheduler getInstance(){
        return instance;
    }

    private final Object plugin;
    private final org.spongepowered.api.scheduler.Scheduler scheduler;
    private final ConcurrentHashMap<UUID, Task> tasks;

    public Scheduler(Object plugin, org.spongepowered.api.scheduler.Scheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.tasks = new ConcurrentHashMap<>();
    }

    public void clearInterval(String id){
        UUID uuid = UUID.fromString(id);
        Task task = tasks.get(uuid);

        if(task != null) {
            task.cancel();
            tasks.remove(uuid);
        }
    }

    public void clearTimeout(String id){
        UUID uuid = UUID.fromString(id);
        Task task = tasks.get(uuid);

        if(task != null) {
            task.cancel();
            tasks.remove(uuid);
        }
    }

    public String setInterval(ScriptObjectMirror function, long milliseconds){
        if(function.isFunction()) {
            UUID taskId = UUID.randomUUID();
            Task.Builder taskBuilder = scheduler.createTaskBuilder();

            Task task = taskBuilder.execute(new Runnable() {
                @Override
                public void run() {
                    function.call(function);
                }
            }).interval(milliseconds, TimeUnit.MILLISECONDS)
                    .name(taskId.toString()).submit(plugin);

            tasks.put(taskId, task);

            return taskId.toString();
        }

        return null;
    }

    public String setTimeout(ScriptObjectMirror function, long milliseconds){
        if(function.isFunction()) {
            UUID taskId = UUID.randomUUID();
            Task.Builder taskBuilder = scheduler.createTaskBuilder();

            Task task = taskBuilder.execute(new Runnable() {
                @Override
                public void run() {
                    function.call(function);
                }
            }).delay(milliseconds, TimeUnit.MILLISECONDS)
                    .name(taskId.toString()).submit(plugin);

            tasks.put(taskId, task);

            return taskId.toString();
        }

        return null;
    }

}
