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
public class CultureGroup {
    protected String name;
    protected ArrayList<Culture> offspring;
    
    public CultureGroup(String name){
        this.name = name;
        offspring = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Culture> getOffspring() {
        return offspring;
    }

    public void setOffspring(ArrayList<Culture> offspring) {
        this.offspring = offspring;
    }
    
    @Override
    public String toString(){
        StringBuilder sub = new StringBuilder();
        sub.append(name);
        sub.append(" contains the following cultures:");
        for (Culture c : offspring){
            sub.append("\n");
            sub.append("\t ");
            sub.append(c.toString());
        }
        return sub.toString();
    }
}
