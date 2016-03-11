package io.github.djxy.spongejavascript.javascript;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel on 2016-02-24.
 */
public class Methods {

    private final String name;
    private final HashMap<Integer,ArrayList<Method>> methods;

    public Methods(String name) {
        this.name = name;
        this.methods = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void add(Method method){
        if(!methods.containsKey(method.getParameterCount()))
            methods.put(method.getParameterCount(), new ArrayList<>());

        methods.get(method.getParameterCount()).add(method);
    }

    public Method getMethod(Object... objects){
        if(methods.containsKey(objects.length)){
            ArrayList<Method> methods = this.methods.get(objects.length);
            int methodValue = -1;
            Method m = null;

            for(Method method : methods){
                int value = 0;

                for(int i = 0; i < objects.length; i++)
                     value += getClassCompatibility(method.getParameterTypes()[i], objects[i].getClass());

                if(methodValue < value) {
                    methodValue = value;
                    m = method;
                }
            }

            return m;
        }

        return null;
    }

    public List<Method> getMethods() {
        ArrayList<Method> methods = new ArrayList<>();

        for(ArrayList<Method> list : this.methods.values())
            methods.addAll(list);

        return methods;
    }

    public boolean containOnlyOneMethod(){
        return methods.size() == 1 && methods.get(methods.keySet().iterator().next()).size() == 1;
    }

    public Method getOnlyMethod(){
        return methods.get(methods.keySet().iterator().next()).get(0);
    }

    /**
     * Return 0 if not compatible, 1 if can is assignable and 2 if it's same class
     */
    private int getClassCompatibility(Class toBe, Class toCheck){
        if(toBe.equals(toCheck)) return 2;
        if(toCheck.equals(Byte.class) || toCheck.equals(byte.class)) return 2;
        if(toCheck.equals(Short.class) || toCheck.equals(short.class)) return 2;
        if(toCheck.equals(Integer.class) || toCheck.equals(int.class)) return 2;
        if(toCheck.equals(Long.class) || toCheck.equals(long.class)) return 2;
        if(toCheck.equals(Float.class) || toCheck.equals(float.class)) return 2;
        if(toCheck.equals(Double.class) || toCheck.equals(double.class)) return 2;
        if(toCheck.equals(Character.class) || toCheck.equals(char.class)) return 2;
        if(toCheck.equals(Boolean.class) || toCheck.equals(boolean.class)) return 2;
        if(toBe.isAssignableFrom(toCheck)) return 1;

        return 0;
    }

}
