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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-03-14.
 */
public class ScriptService {

    private final ScriptManager scriptManager;

    public ScriptService(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    /**
     * Return all the scripts created.
     * @return
     */
    public List<Script> getScripts(){
        return scriptManager.getScripts();
    }

    /**
     * Invoke a function in each script.
     * @param function
     * @param args
     */
    public void invoke(String function, Object... args){
        scriptManager.invoke(function, args);
    }

    /**
     * Adds JavaScript code in each script. For more information about how it works, go read https://docs.oracle.com/javase/7/docs/api/javax/script/ScriptEngine.html#eval(java.lang.String)
     * @param code
     */
    public void addCode(String code){
        scriptManager.addCode(code);
    }

    /**
     * Adds a new variable to each script. The variable will be called the name you give it and will contain the object given in parameter.
     * @param name The name of the variable.
     * @param o The object assigned to the variable.
     */
    public void addVariable(String name, Object o){
        scriptManager.addVariable(name, o);
    }

    /**
     * Create a new script with a random name.
     * @param plugin Your plugin object.
     * @param files The files containing your JavaScript.
     * @return
     */
    public Script createScript(Object plugin, ArrayList<File> files){
        return scriptManager.createScript(plugin, files);
    }

    /**
     * Create a new script with a custom name.
     * @param plugin Your plugin object.
     * @param files The files containing your JavaScript.
     * @return
     */
    public Script createScript(Object plugin, String name, ArrayList<File> files){
        return scriptManager.createScript(plugin, name, files);
    }

}
