/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package music;

import java.util.ArrayList;

/**
 *
 * @author Bernard
 */
public class Condition {
    private ArrayList<CultureGroup> cgLijst;
    private String name;
    
    public Condition(String name){
        cgLijst = new ArrayList<>();
        this.name=name;
    }
    
    public void addCultures(ArrayList<CultureGroup> al){
        cgLijst.addAll(al);
    }
    
    public String getName(){
        return name;
    }
    
    public ArrayList<CultureGroup> getCGLijst(){
        return cgLijst;
    }
    
}
