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
        iden = 0; 
    }
    
    Localitzacio(String nom, int index_popularitat){
        a_nom = nom;
        a_index_pop = index_popularitat;
    }
    
    int popularitat(){
    //Pre: cert
    //Post: retorna el index de popularitat de la Localitzacio
        return a_index_pop;
    }
    
    boolean esPuntDeRecarrega(){
    //Pre: cert
    //Post: retorna fals indicant que no és un PuntDeRecarrega   
        return false;
    }
    
    public int identificador() {
    // Pre: cert
    // Post: retorna el nombre identificador de la Localitzacio 
        return iden;     
    }
    
    @Override
    public String toString(){
        return "\nNom del punt: " + a_nom + "\nTe popularitat: " + a_index_pop + "\n";
    }
    
    @Override
    public boolean equals(Object o){
        if(!o instanceof Localitzacio) return false;
        else return (a_nom.equals( (Localitzacio) o.a_nom));
    }
    
    
    
    protected String a_nom; //INVARIANT: cert
    protected int a_index_pop; //INVARIANT: 0 < index_pop < 10
    private int iden; 
    
    
    
    
    
}
