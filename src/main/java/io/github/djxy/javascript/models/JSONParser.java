package io.github.djxy.javascript.models;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Samuel on 2016-02-18.
 */
public class JSONParser {

    private final static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    public static Object parse(String json){
        try {
            return scriptEngine.eval("Java.asJSONCompatible("+json+")");
        } catch (ScriptException e) {}

        return null;
    }

    public static String stringify(ScriptObjectMirror object){

        if(!object.isArray()) {
            try {
                return new JSONObject(object).toString(4);
            } catch (Exception e) {
            }
        }
        else{
            try {
                return new JSONArray(object.values()).toString(4);
            }catch(Exception e){}
        }

        return "";
    }

}
