/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Bernard
 */
//this class is the engine behind the musicpanel
public class Quill {
    

    public void addSong(BufferedWriter bw, String name, int factor,boolean hasModifier,ArrayList<Condition> al) throws IOException {
        bw.append("song = {");
        bw.newLine();
        bw.append("\t" + "name = \"" + name + "\"");
        bw.newLine();
        addChance(bw, factor,hasModifier,al);
        bw.append("}");
        bw.newLine();
    }

    public void addChance(BufferedWriter bw, int factor, boolean hasModifier,ArrayList<Condition> al) throws IOException {
        bw.append("\tchance = {");
        bw.newLine();
        bw.append("\t\tfactor = " + factor);
        bw.newLine();
        if (hasModifier) {
            addModifier(bw,al);
        }
        bw.append("\t}");
        bw.newLine();
    }

    public void addModifier(BufferedWriter bw,ArrayList<Condition> al) throws IOException {
        for (Condition s : al){
            if (s.getName().equals("cultureexclusion")){
                addCultureExclusionModifier(bw,s);
            }
        }
    }
    
    public void addCultureExclusionModifier(BufferedWriter bw ,Condition s) throws IOException{
        bw.append("\t\tmodifier = {");
        bw.newLine();
        bw.append("\t\t\tfactor = 0");
        bw.newLine();
        for (CultureGroup cg : s.getCGLijst()){
            bw.append("\t\t\tNOT = {");
            if (cg.getClass().getName().equals("Culture")){
                bw.append(" culture = "+cg.name);
            } else{
                bw.append(" culture_group = "+cg.name);
            }
            bw.newLine();
        }
        bw.append("\t\t}");
    }
}
