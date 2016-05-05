/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.commands.arguments;

import io.github.djxy.core.commands.nodes.ArgumentNode;
import io.github.djxy.spongejavascript.script.Script;
import io.github.djxy.spongejavascript.script.ScriptManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-05-05.
 */
public class ScriptNode extends ArgumentNode {

    private final ScriptManager scriptManager;

    public ScriptNode(String alias, String name, ScriptManager scriptManager) {
        super(alias, name);
        this.scriptManager = scriptManager;
    }

    public ScriptNode(String alias, ScriptManager scriptManager) {
        super(alias);
        this.scriptManager = scriptManager;
    }

    @Override
    public Object getValue(String s) {
        s = s.toLowerCase();

        for(Script script : scriptManager.getScripts())
            if(script.getName().toLowerCase().equals(s))
                return script;

        return null;
    }

    @Override
    protected List<String> complete(String s) {
        List<String> values = new ArrayList<>();

        for(Script script : scriptManager.getScripts())
            if(script.getName().toLowerCase().startsWith(s))
                values.add(script.getName());

        return values;
    }

}
