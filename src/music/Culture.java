/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package music;

/**
 *
 * @author Bernard
 */
public class Culture extends CultureGroup{

    public Culture(String name) {
        super(name);
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
