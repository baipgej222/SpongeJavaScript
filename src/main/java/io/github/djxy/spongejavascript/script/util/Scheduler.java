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

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by samuelmarchildon-lavoie on 16-03-03.
 */
public class Scheduler {

    private final Object plugin;
    private final org.spongepowered.api.scheduler.Scheduler scheduler;
    private final ConcurrentHashMap<UUID, Task> tasks;

    public Scheduler(Object plugin) {
        this.plugin = plugin;
        this.scheduler = Sponge.getScheduler();
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
