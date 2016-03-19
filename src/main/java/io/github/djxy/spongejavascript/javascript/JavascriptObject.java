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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-02-23.
 */
public class JavascriptObject implements JSObject {

    private static final HashMap<String, ConcurrentHashMap<Object, JavascriptObject>> objects = new HashMap<>();

    static {
        objects.put("net.minecraft.entity.player.EntityPlayerMP", new ConcurrentHashMap(32));
        objects.put("net.minecraft.world.WorldServer", new ConcurrentHashMap(3));
    }

    private final Object realObject;
    private final boolean isArray;
    private final HashMap<String,Methods> mapGet;
    private final HashMap<String,Methods> mapSet;

    private JavascriptObject(Object realObject) {
        this.realObject = realObject instanceof Object[]?(new ArrayList<>(Arrays.asList((Object[]) realObject))):realObject;
        this.isArray = realObject instanceof List || realObject instanceof Object[];
        this.mapGet = new HashMap<>();
        this.mapSet = new HashMap<>();

        try {
            introspect(realObject, this);
        } catch (Exception e) {e.printStackTrace();}
    }

    public static JavascriptObject convertObjectToJSObject(Object object){
        if(object != null) {
            if (!(object instanceof JavascriptObject)) {
                ConcurrentHashMap<Object, JavascriptObject> map = objects.get(object.getClass().getName());
                JavascriptObject javascriptObject;

                if (map != null) {
                    if ((javascriptObject = map.get(object)) != null) {
                        return javascriptObject;
                    } else {
                        JavascriptObject js = new JavascriptObject(object);
                        map.put(object, js);
                        return js;
                    }
                }

                return new JavascriptObject(object);
            } else
                return (JavascriptObject) object;
        }
        else
            return null;
    }

    public static Object convertJSObjectToObject(Object object){
        return object instanceof JavascriptObject?((JavascriptObject)object).getRealObject():object;
    }

    public Object getRealObject() {
        return realObject;
    }

    @Override
    public Object call(Object o, Object... objects) {
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
        try {
            if (s.equalsIgnoreCase("toString"))
                return new JavascriptFunctionToString(realObject);
            else if (s.equalsIgnoreCase("valueOf"))
                return new JavascriptFunctionValueOf(realObject);
            else if (mapGet.containsKey(s))
                return convertObjectToJSObject(mapGet.get(s).getMethod().invoke(realObject));
            else if (mapSet.containsKey(s))
                return new JavascriptFunction(realObject, mapSet.get(s));
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    @Override
    public Object getSlot(int i) {
        if(isArray){
            Object o = ((List) realObject).get(i);

            if(o != null)
                return convertObjectToJSObject(o);
        }
        return null;
    }

    @Override
    public boolean hasMember(String s) {
        return !isArray? mapGet.containsKey(s) || mapSet.containsKey(s):false;
    }

    @Override
    public boolean hasSlot(int i) {
        return isArray?i < ((List) realObject).size():false;
    }

    @Override
    public void removeMember(String s) {}

    @Override
    public void setMember(String s, Object o) {
        Methods set = mapSet.get(s);
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;
        o = o instanceof ScriptObject? ScriptUtils.wrap((ScriptObject) o):o;

        if(set != null){
            Method method = set.getMethod(o);

            if(method != null) {
                try {
                    method.invoke(realObject, o);
                } catch (Exception e) {}
            }
        }
    }

    @Override
    public void setSlot(int i, Object o) {
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;

        if(isArray)
            ((List) realObject).set(i, o);
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<>(mapGet.size()+ mapSet.size());

        set.addAll(mapGet.keySet());
        set.addAll(mapSet.keySet());

        return set;
    }

    @Override
    public Collection<Object> values() {
        return isArray?((Collection<Object>) realObject):null;
    }

    @Override
    public boolean isInstance(Object o) {
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;

        return realObject == o;
    }

    @Override
    public boolean isInstanceOf(Object o) {
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;

        return realObject.getClass().equals(o.getClass());
    }

    @Override
    public String getClassName() {
        return realObject.getClass().getName();
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isStrictFunction() {
        return false;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }

    @Override
    public double toNumber() {
        return 0;
    }

    private void addSetMethod(Method method){
        String name = convertName(method.getName());
        Methods methodsShortName = mapSet.get(name);
        Methods methods = mapSet.get(name);

        if(methodsShortName == null)
            mapSet.put(name, (methodsShortName = new Methods(name)));
        if(methods == null)
            mapSet.put(method.getName(), (methods = new Methods(method.getName())));

        methodsShortName.add(method);
        methods.add(method);
    }

    private void addGetMethod(Method method){
        String name = convertName(method.getName());
        Methods methodsShortName = mapGet.get(name);
        Methods methods = mapGet.get(name);

        if(methodsShortName == null)
            mapGet.put(name, (methodsShortName = new Methods(name)));
        if(methods == null)
            mapGet.put(method.getName(), (methods = new Methods(method.getName())));

        methodsShortName.add(method);
        methods.add(method);
    }

    private static void introspect(Object obj, JavascriptObject jsObj) throws Exception {
        ArrayList<Class> spongeClasses = new ArrayList<>();

        for(Class c : Arrays.asList(obj.getClass().getInterfaces()))
            if(c.getName().startsWith("org.spongepowered.api"))
                spongeClasses.add(c);

        if(obj.getClass().getName().startsWith("org.spongepowered.api"))
            spongeClasses.add(obj.getClass());

        if(obj.getClass().getDeclaringClass() != null && obj.getClass().getDeclaringClass().getName().startsWith("org.spongepowered.api"))
            spongeClasses.add(obj.getClass().getDeclaringClass());

        if(spongeClasses.size() == 0) {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());

            for (MethodDescriptor md : info.getMethodDescriptors()) {
                if (!md.getName().startsWith("func_")) {
                    Method method = md.getMethod();

                    if (method.getGenericParameterTypes().length == 0 && method.getReturnType() != Void.TYPE)
                        jsObj.addGetMethod(method);
                    else
                        jsObj.addSetMethod(method);
                }
            }
        }
        else{
            for(Class c : jsObj.getSpongeClasses(spongeClasses)){
                BeanInfo info = Introspector.getBeanInfo(c);

                for (MethodDescriptor md : info.getMethodDescriptors()) {
                    if (!md.getName().startsWith("func_")) {
                        Method method = md.getMethod();

                        if (method.getGenericParameterTypes().length == 0 && method.getReturnType() != Void.TYPE)
                            jsObj.addGetMethod(method);
                        else
                            jsObj.addSetMethod(method);
                    }
                }
            }
        }
    }

    private HashSet<Class> getSpongeClasses(ArrayList<Class> spongeClasses){
        HashSet<Class> classes = new HashSet<>();

        classes.addAll(spongeClasses);

        for(Class c : spongeClasses)
            classes.addAll(getSpongeClasses(c));

        return classes;
    }

    private HashSet<Class> getSpongeClasses(Class spongeClass){
        HashSet<Class> classes = new HashSet<>();

        if(spongeClass.getDeclaringClass() != null) {
            classes.add(spongeClass.getDeclaringClass());
            classes.addAll(getSpongeClasses(spongeClass.getDeclaringClass()));
        }

        for(Class c : spongeClass.getInterfaces()) {
            classes.add(c);
            classes.addAll(getSpongeClasses(c));
        }

        return classes;
    }

    private static String convertName(String name){
        if(name.startsWith("get") && !name.equalsIgnoreCase("get"))
            name = (name.charAt(3)+"").toLowerCase()+name.substring(4);
        if(name.startsWith("set") && !name.equalsIgnoreCase("set"))
            return (name.charAt(3)+"").toLowerCase()+name.substring(4);

        return name;
    }

    @Override
    public String toString() {
        return realObject.toString();
    }
}
