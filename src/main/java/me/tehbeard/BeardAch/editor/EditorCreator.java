package me.tehbeard.BeardAch.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.tehbeard.BeardAch.editor.elements.*;

/**
 * 
 * @author james
 *
 */
public class EditorCreator {

    private ElementMaker[] makers = {new TextboxElement(),new SelectElement(),new CheckboxElement()};

    private List<String> parsedTriggerElements = new ArrayList<String>();
    private List<String> parsedRewardElements = new ArrayList<String>();

    private String template;
    private File output;

    public EditorCreator(InputStream template,File output){
        this.template = read(template);
        this.output = output;
    }

    public void addElement(HtmlForm form,boolean isTrigger){
        for(HtmlElement element : form.elements()){
            if(isTrigger){
                parsedTriggerElements.add(parseElement(element));
            }
            else
            {
                parsedRewardElements.add(parseElement(element));
            }
        }
        throw new UnsupportedOperationException();
    }

    private String parseElement(HtmlElement element){
        for(ElementMaker maker : makers){
            if(maker.getType() == element.type()){
                return maker.makeHtml(element);
            }
        }
        return null;

    }

    public void write(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            bw.write(template.replaceAll("{triggers}", toStr(parsedTriggerElements)).replaceAll("{rewards}", toStr(parsedRewardElements)));
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String read(InputStream file){
        BufferedReader br = new BufferedReader(new InputStreamReader(file));

        String str ="";
        String s = null;
        try {

            while((s=br.readLine()) != null){
                str+=s + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                br.close();
            } catch (IOException e) {
            }
        }


        return str;
    }

    private static String toStr(List<String> l){
        String r = "";
        for(String k : l){
            r += k + "\n";
        }
        return r;
    }

}
