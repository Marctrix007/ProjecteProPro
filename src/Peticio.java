/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */



public class Peticio {
    
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
    public boolean equals(Object o) {
        
        if (!(o instanceof Peticio)) return false;
        else return (horaSortida.equals(((Peticio)o).horaSortida) && origen.equals((Peticio)o).origen);
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
