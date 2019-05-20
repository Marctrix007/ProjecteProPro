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
    private final float autonomia;        // en km 
    private boolean carrRapida;
    private Temps duradaCarrega;      // hores / minuts (API temps Java)
    //Temps(hh,mm)
    private final int nPlaces;
    private int nPlacesLliures; 
    
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
        nPlacesLliures = nPlaces; 
        
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
        
        return duradaCarrega.per(kmRuta/autonomia);
    
    }
    
    public int NombrePlaces() {
        
        return nPlaces;
    }
    
    public int NombrePlacesLliures() {
    // Pre: --
    // Post: Retorna el nombre de places lliures del vehicle 
        return nPlacesLliures; 
    }
    
    public double Ocupacio() throws Exception{
        System.out.println("Places: " + nPlaces + " Lliures: " + nPlacesLliures);
        if((100.0*(nPlaces - nPlacesLliures)/nPlaces)>100) throw new Exception("OYEEEE");
        return 100.0*(nPlaces - nPlacesLliures)/nPlaces;
    }
    
    public void CarregarPassatgers(int nPass) {
    // Pre: --
    // Post: Redueix el nombre de places lliures del vehicle en funció del nombre de passatgers que ha de carregar  
        nPlacesLliures-=nPass;
    }
    
    public void DescarregarPassatgers(int nPass) {
    // Pre: --
    // Post: Augmenta el nombre de places lliures del vehicle en funció del nombre de passatgers que ha de descarregar  
        nPlacesLliures+=nPass;
    }
    
    
    public Temps TempsCarrega(){
        return duradaCarrega;
    }
    
    //Per estadistiques
    public String matricula(){
        return matricula;
    }
    
    @Override
    public String toString(){
        return "Matricula: " + matricula + "\nTipus: " + tipus + "\nModel: " + model + "\nAutonomia: " + autonomia + "\nTemps de carrega: " + duradaCarrega.toString() + "\nPlaces vehicle: " + nPlaces + "\n";
    }
    
}
