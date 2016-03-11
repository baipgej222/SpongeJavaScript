/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.javascript;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Samuel on 2016-02-23.
 */
public class JavascriptFunction implements JSObject {

    private final Object realObject;
    private final Methods methods;

    public JavascriptFunction(Object realObject, Methods methods) {
        this.realObject = realObject;
        this.methods = methods;
    }

    @Override
    public Object call(Object o, Object... objects) {
        for(int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof JavascriptObject) {
                objects[i] = ((JavascriptObject) objects[i]).getRealObject();
            }
            else if(objects[i] instanceof ScriptObject){
                objects[i] = ScriptUtils.wrap((ScriptObject) objects[i]);
            }
        }

        try {
            Object result = methods.getMethod(objects).invoke(realObject, objects);

            if(result != null)
                return JavascriptObject.convertObjectToJSObject(result);

            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object newObject(Object... objects) {
        return null;
    }

    @Override
    public Object eval(String s) {
        return null;
    }

    @Override
    public Object getMember(String s) {
        if(s.equalsIgnoreCase("toString")) {
            if(methods.getMethod() != null) {
                try {
                    return new JavascriptFunctionToString(methods.getMethod().invoke(realObject));
                } catch (Exception e) {}

                return new JavascriptFunctionToString(methods.getMethod());
            }
            else
                return new JavascriptFunctionToString(methods.getMethods());
        }
        else if (s.equalsIgnoreCase("valueOf")){
            if(methods.getMethod() != null) {
                try {
                    return new JavascriptFunctionValueOf(methods.getMethod().invoke(realObject));
                } catch (Exception e) {}

                return new JavascriptFunctionValueOf(methods.getMethod());
            }
            else
                return new JavascriptFunctionValueOf(methods.getMethods());
        }

        return null;
    }

    @Override
    public Object getSlot(int i) {
        return null;
    }

    @Override
    public boolean hasMember(String s) {
        return false;
    }

    @Override
    public boolean hasSlot(int i) {
        return false;
    }

    @Override
    public void removeMember(String s) {}

    @Override
    public void setMember(String s, Object o) {
    }

    @Override
    public void setSlot(int i, Object o) {}

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public boolean isInstance(Object o) {
        return false;
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return false;
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isStrictFunction() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public double toNumber() {
        return 0;
    }

}
