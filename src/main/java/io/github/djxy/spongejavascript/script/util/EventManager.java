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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-02-18.
 */
public class EventManager {

    private final Object plugin;
    private final List<EventListener> listeners;

    public EventManager(Object plugin) {
        this.plugin = plugin;
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void register(ScriptObjectMirror scriptObjectMirror){
        if(!scriptObjectMirror.isArray()){
            ScriptObjectMirror listener = scriptObjectMirror.get("listener") != null?(ScriptObjectMirror) scriptObjectMirror.get("listener"):null;
            Class clazz = scriptObjectMirror.get("event") != null?(Class) scriptObjectMirror.get("event"):null;

            if(listener != null && clazz != null) {
                EventListener eventListener = new EventListener(listener);

                listeners.add(eventListener);

                Sponge.getGame().getEventManager().registerListener(plugin, clazz, eventListener);
            }
        }
    }

    public void clearAll(){
        for(EventListener eventListener : listeners)
            Sponge.getGame().getEventManager().unregisterListeners(eventListener);
    }

    public class EventListener implements org.spongepowered.api.event.EventListener {

        private final ScriptObjectMirror executor;

        private EventListener(ScriptObjectMirror executor) {
            this.executor = executor;
        }

        @Override
        public void handle(Event event) {
            if (!event.getCause().first(Player.class).isPresent())
                executor.call(executor, JavascriptObject.convertObjectToJSObject(event));
            else
                executor.call(executor, JavascriptObject.convertObjectToJSObject(event), JavascriptObject.convertObjectToJSObject(event.getCause().first(Player.class).get()));
        }
    }

}
