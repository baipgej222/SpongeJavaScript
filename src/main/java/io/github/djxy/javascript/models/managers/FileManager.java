package io.github.djxy.javascript.models.managers;

import io.github.djxy.javascript.models.Script;

import java.io.*;

/**
 * Created by Samuel on 2016-02-18.
 */
public class FileManager extends Manager {

    private final String path;

    public FileManager(Script script) {
        super(script);
        script.addVariable("fileManager", this);
        this.path = "."+File.separator+"config"+File.separator+script.getName();

        new File(this.path).mkdirs();
    }

    public void create(String name) throws Exception {
        File file = new File(getFile(name));

        if(!file.exists())
            file.createNewFile();
    }

    public void delete(String name){
        File file = new File(getFile(name));

        if(file.exists())
            file.delete();
    }

    public void write(String name, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(getFile(name));

        fos.write(content.getBytes());

        fos.close();
    }

    public String read(String name) throws Exception {
        File file = new File(getFile(name));
        StringBuilder sb = new StringBuilder((int) file.length());
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;

        while((line = br.readLine()) != null)
            sb.append(line);

        br.close();

        return sb.toString().trim();
    }

    private String getFile(String name){
        return this.path+File.separator+name;
    }

}
