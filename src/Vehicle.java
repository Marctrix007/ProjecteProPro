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
    
    String matricula;
    String marca;
    String model;
    float autonomia;          // en km 
    float consum;           // Kwh/100km 
    int duradaCarrega;      // hores / minuts (API temps Java)
    int nPlaces;
    
    // invariant: nPlaces > 1
    //            autonomia > 0
    //            consum > 0 
    //            duradaCarrega > 0 
    
    // Excepcions: el main podria llançar una excepcio si el temps de carrega es excessiu per indicar que el vehicle no es pot acceptar 
    
    public float getAutonomia() {
        
        return autonomia; 
        
    }
    
    public float getConsum() {
        
        return consum; 
        
    }
    
    public int getDuradaCarrega() {
        
        return duradaCarrega;
        
    }
    
    public int getNombreplaces() {
        
        return nPlaces;
        
    }
    
    
    
}
