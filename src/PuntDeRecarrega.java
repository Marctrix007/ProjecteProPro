
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author qsamb
 */
public class PuntDeRecarrega extends Localitzacio {
    
    
    
    PuntDeRecarrega(String nom, int index_popularitat, int nPlaces, boolean CarregaRapida){
        super(nom,index_popularitat);
        a_nPlaces = nPlaces;
        a_carregaRapida = CarregaRapida;
    }
    
    
    public boolean accepta_carregaRapida(){
        return a_carregaRapida;
    }
    
    public int places_lliures(){
        return (a_nPlaces - a_parking.size());
    }
  
    
    public void estacionar_vehicle(Vehicle v, Temps hArribada){
      
        if ( places_lliures() == 0)
           throw("No queden places lliures"); 
        a_parking.add(v);
       
        //v.temps_arribada_estacionat(Temps hArribada);
    }
    
    public void sortida_vehicle(Vehicle v, Temps hSortida){    // vehicle no surt fins que no està carregat 
        
        a_parking.remove(v);
        //v.carregar_bateria(Temps hSortida)
    }
    
    @Override
    public boolean esPuntDeRecarrega(){
        return true;
    }
    
    
    private int a_nPlaces;
    private boolean a_carregaRapida;
    private HashSet<Vehicle> a_parking;
    
    
}
