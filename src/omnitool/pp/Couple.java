package omnitool.pp;


import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bernard
 */
public class Couple {
    private String name;
    private Color color;
    
    public Couple(String name){
        this.name=name;
    }
    
    public String getName(){
        return name;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setName(String name){
        this.name=name;
    }
    
    public void setColor(Color k){
        this.color=k;
    }
}
