/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author qsamb
 */
public class Localitzacio {
    Localitzacio(){
        a_nom = " ";
        a_index_pop = 0;
    }
    
    Localitzacio(String nom, int index_popularitat){
        a_nom = nom;
        a_index_pop = index_popularitat;
    }
    
    int popularitat(){
        return a_index_pop;
    }
    
    boolean esPuntDeRecarrega(){
        return false;
    }
    
    
    
    
    private String a_nom;
    private int a_index_pop;
    
    
    
    
    
    
}
