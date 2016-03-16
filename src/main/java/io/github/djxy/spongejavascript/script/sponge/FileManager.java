/*
 * Copyright (c) 2016 Samuel Marchildon-Lavoie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.djxy.spongejavascript.script.sponge;

import io.github.djxy.spongejavascript.script.Script;

import java.io.*;

/**
 * Created by Samuel on 2016-02-18.
 */
public class FileManager  {

    private final String path;

    public FileManager(Script script) {
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
