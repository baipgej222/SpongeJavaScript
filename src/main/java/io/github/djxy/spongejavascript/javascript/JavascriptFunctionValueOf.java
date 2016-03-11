package io.github.djxy.spongejavascript.javascript;

import jdk.nashorn.api.scripting.JSObject;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Samuel on 2016-02-27.
 */
public class JavascriptFunctionValueOf implements JSObject {

    private final Object object;

    public JavascriptFunctionValueOf(Object object) {
        this.object = object;
    }

    @Override
    public Object call(Object o, Object... objects) {
        if(object.getClass() == int.class || object.getClass() == Integer.class || object.getClass() == double.class || object.getClass() == Double.class || object.getClass() == long.class || object.getClass() == Long.class)
            return object;
        else
            return object.toString();
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
        if(s.equalsIgnoreCase("toString"))
            return new JavascriptFunctionToString(object);

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
