/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.HashMap;
import omnitool.ProvincesPanel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bernard
 */
public class ProvincesPanel2 {
    
    public ProvincesPanel2() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGiveCountyBasicRGB(){
        HashMap<Color,String> map = new HashMap<>();
        map.put(Color.red, "rood");
        map.put(Color.black, "zwart");
        map.put(Color.blue, "blauw");
        map.put(Color.cyan, "cyaan");
        ProvincesPanel p =new ProvincesPanel(false,false);
        //assertEquals(Color.red, p.giveCountyBasicRGB(map,"rood"));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
