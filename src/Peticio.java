/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */



public class Peticio implements Comparable<Peticio> {
    
    int identificador; 
    Temps horaTrucada;
    Temps horaSortida;
    Localitzacio origen;
    Localitzacio desti; 
    int nClients; 
    int estat;  
    // estat = -1 (fallada)
    // estat= 0 (per atendre)
    // estat = 1 (satisfeta)
    
    
    public Peticio(int identificador, Temps horaTrucada, Temps horaSortida, Localitzacio origen, Localitzacio desti, int nClients, int estat) {
        
        this.identificador = identificador;
        this.horaTrucada = horaTrucada;
        this.horaSortida = horaSortida;
        this.origen = origen;
        this.desti = desti;
        this.nClients = nClients;
        this.estat = estat; 
        
    }

    
    public Temps horaSortida() {
        
        return horaSortida;
        
    }
    
    public Temps horaTrucada() {
    // Pre: --
    // Post: Retorna l'hora de trucada de la petició
    
        return horaTrucada; 
        
    }
    
    public Localitzacio origen() {
    // Pre: --
    // Post: Retorna el punt d'origen de la petició
        return origen;
    }
    
    
    public Localitzacio desti() {
    // Pre: --
    // Post: Retorna la destinació de la petició
        return desti; 
    }
    
    public int NombreClients() {
        
        return nClients; 
        
    }
    
    
    public void fallada(){
        
        estat = -1;
        
    }
    
    public void satisfeta(){
        
        estat = 1;
        
    }
    
    @Override
    public int compareTo(Peticio p) {
        
        return horaSortida.compareTo(p.horaSortida); 
        
    }
    
    /*
    public static void main(String args[]) {
        
        
        Peticio p1 = new Peticio("20:14");
        Peticio p2 = new Peticio("20:13");
        
        if (p1.compareTo(p2) == -1) 
            System.out.println("L'hora de la primera peticio es inferior\n");
        
        else 
            System.out.println("L'hora de la primera peticio es superior\n");
        
        
    }
    
    */
}
