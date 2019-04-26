/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author aula
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println(random());
        System.out.println(random());
        System.out.println(random());
        System.out.println(random());
        System.out.println(random());
        
    }
    
    public static int random() {
        List<Integer> PuntRecar = Arrays.asList(24,454453,21,43345,2136,656);
        Random rand = new Random();
        int randomElement = PuntRecar.get(rand.nextInt(PuntRecar.size()));
        return randomElement;
    }
    
    
    
}
