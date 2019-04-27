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
    private String tipus;
    private String model;
    private float autonomia;        // en km 
    private boolean carrRapida;
    private Temps duradaCarrega;      // hores / minuts (API temps Java)
    //Temps(hh,mm)
    private int nPlaces;
    
    // nPlacesOcupades afegir potser 
    
    // invariant: nPlaces > 1
    //            autonomia > 0
    //            consum > 0 
    //            duradaCarrega > 0 
    
    // Excepcions: el main podria llançar una excepcio si el temps de carrega es excessiu per indicar que el vehicle no es pot acceptar 
    //matricula,model,tipus,autonomia,carrega,nPlaces, tCarrega
    public Vehicle(String mat, String mod, String tipus, float auto, boolean carregaRapida, int nombrePlaces, Temps duradaCarr) {
    
        matricula = mat;
        this.tipus = tipus;
        model = mod; 
        autonomia = auto;
        duradaCarrega = duradaCarr;
        nPlaces = nombrePlaces; 
        this.carrRapida = carregaRapida;
        
    }

    public Vehicle() {
        matricula = " ";
        this.tipus = " ";
        model = " "; 
        autonomia = 0;
        duradaCarrega = new Temps();
        nPlaces = 0; 
        this.carrRapida = false;
    }
    
    public boolean CarregaRapida(){
        return carrRapida;
    }
    
    
    public float Autonomia() {
        
        return autonomia; 
        
    }
     
    public Temps DuradaCarrega(float kmRuta) {
        
        return duradaCarrega.per(1-(autonomia-kmRuta)/autonomia);
    
    }
    
    public int NombrePlaces() {
        
        return nPlaces;
    }
    
    
    public Temps TempsCarrega(){
        return duradaCarrega;
    }
    
    @Override
    public String toString(){
        return "Matricula: " + matricula + "\nTipus: " + tipus + "\nModel: " + model + "\nAutonomia: " + autonomia + "\nTemps de carrega: " + duradaCarrega.toString() + "\nPlaces vehicle: " + nPlaces + "\n";
    }
    
}
