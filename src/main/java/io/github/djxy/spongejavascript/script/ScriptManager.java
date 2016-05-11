/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.script;

import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-14.
 */
public class ScriptManager {

    private final ConcurrentHashMap<String,Object> variables;
    private final CopyOnWriteArrayList<Script> scripts;
    private final CopyOnWriteArrayList<String> javascriptCodes;
    private final NashornScriptEngineFactory factory;
    private final ClassFilter filter;

    public ScriptManager() {
        this(null);
    }

    public ScriptManager(ClassFilter classFilter) {
        this.scripts = new CopyOnWriteArrayList<>();
        this.variables = new ConcurrentHashMap<>();
        this.javascriptCodes = new CopyOnWriteArrayList<>();
        this.factory = new NashornScriptEngineFactory();
        this.filter = classFilter;
    }

    /**
     * Invoke a function in each script.
     * @param function
     * @param args
     */
    public void invoke(String function, Object... args){
        for(Script script : scripts)
            script.invoke(function, args);
    }

    /**
     * Return all the scripts created.
     * @return
     */
    public List<Script> getScripts(){
        return new ArrayList<>(scripts);
    }

    /**
     * Adds JavaScript code in each script. For more information about how it works, go read https://docs.oracle.com/javase/7/docs/api/javax/script/ScriptEngine.html#eval(java.lang.String)
     * @param code
     */
    public void addCode(String code){
        javascriptCodes.add(code);

        for(Script script : scripts)
            script.addCode(code);
    }

    /**
     * Adds a new variable to each script. The variable will be called the name you give it and will contain the object given in parameter.
     * @param name The name of the variable.
     * @param o The object assigned to the variable.
     */
    public void addVariable(String name, Object o){
        variables.put(name, o);

        for(Script script : scripts)
            script.addVariable(name, o);
    }

    /**
     * Create a new script with a random name.
     * @param plugin Your plugin object.
     * @param files The files containing your JavaScript.
     * @return
     */
    public Script createScript(Object plugin, ArrayList<File> files){
        return createScript(plugin, UUID.randomUUID().toString(), files);
    }

    /**
     * Create a new script with a custom name.
     * @param plugin Your plugin object.
     * @param files The files containing your JavaScript.
     * @param name Name of the script.
     * @return
     */
    public Script createScript(Object plugin, String name, ArrayList<File> files){
        Script script = new Script(plugin, name, files, createScriptEngine(), this);

        for(Map.Entry pairs : variables.entrySet())
            script.addVariable((String) pairs.getKey(), pairs.getValue());

        javascriptCodes.forEach(script::addCode);

        scripts.add(script);

        return script;
    }

    protected ScriptEngine createScriptEngine(){
        return filter == null?factory.getScriptEngine():factory.getScriptEngine(filter);
    }

    protected void reloadScript(Script script){
        script.setEngine(createScriptEngine());
        script.loadFiles();

        for(Map.Entry pairs : variables.entrySet())
            script.addVariable((String) pairs.getKey(), pairs.getValue());

        javascriptCodes.forEach(script::addCode);
    }

}
