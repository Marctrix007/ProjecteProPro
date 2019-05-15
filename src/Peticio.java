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
    
    /* ATRIBUTS */ 
    int identificador; 
    Temps horaTrucada;
    Temps horaSortida;
    Localitzacio origen;
    Localitzacio desti; 
    int nClients; 
    
    
    public Peticio(int identificador, Temps horaTrucada, Temps horaSortida, Localitzacio origen, Localitzacio desti, int nClients) {
        
        this.identificador = identificador;
        this.horaTrucada = horaTrucada;
        this.horaSortida = horaSortida;
        this.origen = origen;
        this.desti = desti;
        this.nClients = nClients;        
        
    }

    public int identificador() {
        return identificador; 
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
    
    @Override
    public int compareTo(Peticio p) {
        
        return horaSortida.compareTo(p.horaSortida); 
        
    }
    
    @Override
    public String toString(){
        return "PETICIÓ: " + identificador  + "\n\nHora trucada: " + horaTrucada + "\nHora sortida: " + horaSortida + "\nOrigen: " + origen + "\nDesti " + desti  + "\nNombre de clients: " + nClients + "\n";

    }
    
}
