
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
  
    
    public void estacionar_vehicle(Vehicle v, Temps tempsEntrada){
      
        if ( places_lliures() == 0))
           throw("No queden places lliures"); 
        else{
            a_parking.put(v, tempEntrada.mes(v.tempsCarrega()));
        }
    }
    
    public Vehicle sortida_vehicle(int recorregut, int nPersones, Temps tempsSortida){    // vehicle no surt fins que no està carregat 
        
        if (a_parking.isEmpty())
            throw("No hi ha vehicles");
        else{
        
            Set<Map.Entry<Vehicle, Temps>> vehicles = a_parking.entrySet();
            Iterator<Map.Entry<Vehicle, Temps>> it_vehicles = vehicles.iterator();
            Map.Entry<Vehicle, Temps> entry_vehicle;
            Vehicle v;
            boolean trobat = false;
            
            while(it_vehicles.hasNext() && !trobat){
                entry_vehicle = it_vehicles.next();
                v = entry_vehicle.getKey();
                if (v.autonomia() > recorregut && v.nPlaces() > nPersones && entry_vehicle.getValue().inferior(tempsSortida)){
                    trobat = true;
                }
            }
            
            if (!trobat)
                return null;
            else{
                a_parking.remove(v);
                return v;
            }
            
        }
        
        
        
    }
    
    @Override
    public boolean esPuntDeRecarrega(){
        return true;
    }
    
    
    private int a_nPlaces;
    private boolean a_carregaRapida;
    private HashMap<Vehicle, Temps> a_parking;
    
    
}
