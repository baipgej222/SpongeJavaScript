package io.github.djxy.javascript.models.javascript;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel on 2016-02-24.
 */
public class Methods {

    private final String name;
    private final HashMap<Integer,Method> methods;

    public Methods(String name) {
        this.name = name;
        this.methods = new HashMap<>();
    }

    public void add(Method method){
        methods.put(method.getParameterCount(), method);
    }

    public List<Method> getMethods() {
        return new ArrayList<>(methods.values());
    }

    public Method get(int parameterCount){
        return methods.get(parameterCount);
    }

    public boolean containOnlyOneMethod(){
        return methods.size() == 1;
    }

    public Method getOnlyMethod(){
        return methods.get(methods.keySet().iterator().next());
    }

    public Method getMethod(int nbParameter){
        return methods.get(nbParameter);
    }

}
