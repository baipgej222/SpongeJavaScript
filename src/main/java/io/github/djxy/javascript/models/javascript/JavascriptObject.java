package io.github.djxy.javascript.models.javascript;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Samuel on 2016-02-23.
 */
public class JavascriptObject implements jdk.nashorn.api.scripting.JSObject, InvocationHandler {

    private final Object realObject;
    private final boolean isArray;
    private final HashMap<String,Methods> mapRead;
    private final HashMap<String,Methods> mapWrite;

    public JavascriptObject(Object realObject) {
        this.realObject = realObject instanceof Object[]?(new ArrayList<>(Arrays.asList((Object[]) realObject))):realObject;
        this.isArray = realObject instanceof List || realObject instanceof Object[];
        this.mapRead = new HashMap<>();
        this.mapWrite = new HashMap<>();

        try {
            introspect(realObject, this);
        } catch (Exception e) {e.printStackTrace();}
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
        if(s.equalsIgnoreCase("toString"))
            return new JavascriptFunctionToString(realObject);
        if (mapRead.containsKey(s))
            return new JavascriptFunction(realObject, mapRead.get(s));
        else if(mapWrite.containsKey(s))
            return new JavascriptFunction(realObject, mapWrite.get(s));

        return null;
    }

    @Override
    public Object getSlot(int i) {
        return isArray?((List) realObject).get(i):null;
    }

    @Override
    public boolean hasMember(String s) {
        return !isArray?mapRead.containsKey(s) || mapWrite.containsKey(s):false;
    }

    @Override
    public boolean hasSlot(int i) {
        return isArray?i < ((List) realObject).size():false;
    }

    @Override
    public void removeMember(String s) {}

    @Override
    public void setMember(String s, Object o) {
        o = o instanceof JavascriptObject?((JavascriptObject) o).realObject:o;

        if(mapWrite.get(s) != null){
            Method method = mapWrite.get(s).getMethod(1);

            if(method != null && method.getGenericParameterTypes().length == 1) {
                try {
                    method.invoke(realObject, o);
                } catch (Exception e) {}
            }
        }
    }

    @Override
    public void setSlot(int i, Object o) {
        if(isArray)
            ((List) realObject).set(i, o);
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<>(mapRead.size()+mapWrite.size());

        set.addAll(mapRead.keySet());
        set.addAll(mapWrite.keySet());

        return set;
    }

    @Override
    public Collection<Object> values() {
        return isArray?((Collection<Object>) realObject):null;
    }

    @Override
    public boolean isInstance(Object o) {
        return realObject == o;
    }

    @Override
    public boolean isInstanceOf(Object o) {
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

    private void addWriteMethod(Method method){
        String name = convertSetName(method);
        Methods methods = mapWrite.get(name);

        if(methods == null)
            mapWrite.put(name, (methods = new Methods(name)));

        methods.add(method);
    }

    private void addReadMethod(Method method){
        String name = convertGetName(method);
        Methods methods = mapRead.get(name);

        if(methods == null)
            mapRead.put(name, (methods = new Methods(name)));

        methods.add(method);
    }

    private static void introspect(Object obj, JavascriptObject jsObj) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());

        for(MethodDescriptor md : info.getMethodDescriptors()){
            if(!md.getName().startsWith("func_")) {
                Method method = md.getMethod();

                if(method.getReturnType() == Void.TYPE) {
                    jsObj.addWriteMethod(method);
                }
                else if(method.getGenericParameterTypes().length == 0) {
                    jsObj.addReadMethod(method);
                }
                else {
                    jsObj.addWriteMethod(method);
                }
            }
        }
    }

    private static String convertGetName(Method get){
        if(get.getName().startsWith("get") && !get.getName().equalsIgnoreCase("get"))
            return (get.getName().charAt(3)+"").toLowerCase()+get.getName().substring(4);
        else
            return get.getName();
    }

    private static String convertSetName(Method get){
        if(get.getName().startsWith("set") && !get.getName().equalsIgnoreCase("set"))
            return (get.getName().charAt(3)+"").toLowerCase()+get.getName().substring(4);
        else
            return get.getName();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    @Override
    public String toString() {
        return realObject.toString();
    }
}
