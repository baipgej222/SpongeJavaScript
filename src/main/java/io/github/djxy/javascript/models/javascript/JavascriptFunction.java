package io.github.djxy.javascript.models.javascript;

import jdk.nashorn.api.scripting.JSObject;

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
        for(int i = 0; i < objects.length; i++)
            if(objects[i] instanceof JavascriptObject)
                objects[i] = ((JavascriptObject) objects[i]).getRealObject();

        try {
            Object result = methods.getMethod(objects).invoke(realObject, objects);

            if(result != null)
                return new JavascriptObject(result);

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
