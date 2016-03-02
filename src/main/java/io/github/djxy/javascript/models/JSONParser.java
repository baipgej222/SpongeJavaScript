package io.github.djxy.javascript.models;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Samuel on 2016-02-18.
 */
public class JSONParser {

    private final static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    public static Object parse(String json) throws Exception {
        return scriptEngine.eval("Java.asJSONCompatible("+json+")");
    }

    public static String stringify(ScriptObjectMirror object){
        if(!object.isArray())
            return new JSONObject(object).toString(4);
        else
            return new JSONArray(object.values()).toString(4);
    }

}
