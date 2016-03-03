package io.github.djxy.javascript.models.javascript;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Samuel on 2016-02-23.
 */
public class JavascriptObject implements JSObject {

    private final Object realObject;
    private final boolean isArray;
    private final HashMap<String,Methods> mapGet;
    private final HashMap<String,Methods> mapSet;
    private final HashMap<String,Methods> mapSetVoid;

    public JavascriptObject(Object realObject) {
        this.realObject = realObject instanceof Object[]?(new ArrayList<>(Arrays.asList((Object[]) realObject))):realObject;
        this.isArray = realObject instanceof List || realObject instanceof Object[];
        this.mapGet = new HashMap<>();
        this.mapSet = new HashMap<>();
        this.mapSetVoid = new HashMap<>();

        try {
            introspect(realObject, this);
        } catch (Exception e) {e.printStackTrace();}
    }

    public static JavascriptObject convertObjectToJSObject(Object object){
        return object instanceof JavascriptObject? (JavascriptObject) object :new JavascriptObject(object);
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
                return new JavascriptObject(mapGet.get(s).getMethod().invoke(realObject));
            else if (mapSet.containsKey(s))
                return new JavascriptFunction(realObject, mapSet.get(s));
            else if (mapSetVoid.containsKey(s))
                return new JavascriptFunction(realObject, mapSetVoid.get(s));
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    @Override
    public Object getSlot(int i) {
        if(isArray){
            Object o = ((List) realObject).get(i);

            if(o != null)
                return new JavascriptObject(o);
        }
        return null;
    }

    @Override
    public boolean hasMember(String s) {
        return !isArray? mapGet.containsKey(s) || mapSet.containsKey(s) || mapSetVoid.containsKey(s):false;
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
        Methods setVoid = mapSetVoid.get(s);
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;
        o = o instanceof ScriptObject? ScriptUtils.wrap((ScriptObject) o):o;

        if(set != null || setVoid != null){
            Method method = set == null?setVoid.getMethod(o):set.getMethod(o);

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
        Set<String> set = new HashSet<>(mapGet.size()+ mapSet.size()+ mapSetVoid.size());

        set.addAll(mapGet.keySet());
        set.addAll(mapSet.keySet());
        set.addAll(mapSetVoid.keySet());

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

    private void addSetVoidMethod(Method method){
        String name = convertName(method.getName());
        Methods methodsShortName = mapSetVoid.get(name);
        Methods methods = mapSetVoid.get(name);

        if(methodsShortName == null)
            mapSetVoid.put(name, (methodsShortName = new Methods(name)));
        if(methods == null)
            mapSetVoid.put(method.getName(), (methods = new Methods(method.getName())));

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

                    if (method.getReturnType() == Void.TYPE)
                        jsObj.addSetVoidMethod(method);
                    else if (method.getGenericParameterTypes().length == 0)
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

                        if (method.getReturnType() == Void.TYPE)
                            jsObj.addSetVoidMethod(method);
                        else if (method.getGenericParameterTypes().length == 0)
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
