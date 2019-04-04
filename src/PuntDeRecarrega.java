
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
        a_places_lliures = 0;
    }
    
    
    boolean accepta_carregaRapida(){
        return a_carregaRapida;
    }
    
    int places_lliures(){
        return a_places_lliures;
    }
    
    void estacionar_vehicle(Vehicle v, Temps hArribada){
        a_parking.add(v);
        //v.temps_arribada_estacionat(Temps hArribada);
    }
    
    void sortida_vehicle(Vehicle v, Temps hSortida){
        a_parking.remove(v);
        //v.carregar_bateria(Temps hSortida)
    }
    
    @Override
    boolean esPuntDeRecarrega(){
        return true;
    }
    
    
    private int a_nPlaces;
    private int a_places_lliures;
    private boolean a_carregaRapida;
    private HashSet<Vehicle> a_parking;
    
    
}
