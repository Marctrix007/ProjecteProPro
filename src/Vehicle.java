/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */
public class Vehicle {
    
    private String matricula;
    private String marca;
    private String model;
    private float autonomia;        // en km 
    private int duradaCarrega;      // hores / minuts (API temps Java)
    //Temps(hh,mm)
    private int nPlaces;
    
    // nPlacesOcupades afegir potser 
    
    // invariant: nPlaces > 1
    //            autonomia > 0
    //            consum > 0 
    //            duradaCarrega > 0 
    
    // Excepcions: el main podria llançar una excepcio si el temps de carrega es excessiu per indicar que el vehicle no es pot acceptar 
    
    public Vehicle(String mat, String mar, String mod, float auto, int duradaCarr, int nombrePlaces) {
    
        matricula = mat;
        marca = mar;
        model = mod; 
        autonomia = auto;
        duradaCarrega = duradaCarr;
        nPlaces = nombrePlaces; 
        
    }
    
    
    public float Autonomia() {
        
        return autonomia; 
        
    }
     
    public Temps DuradaCarrega(float kmRuta) {
        
        return duradaCarrega*(1-(autonomia-kmRuta)/autonomia);
    
    }
    
    public int Nombreplaces() {
        
        return nPlaces;
        
    }
    
    
    
}
