
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
  
    
    public void estacionar_vehicle(Vehicle v){
      
        if ( places_lliures() == 0))
           throw("No queden places lliures"); 
        else{
            a_parking.add(v);
        }
    }
    
    public Vehicle sortida_vehicle(int recorregut, int nPersones){    // vehicle no surt fins que no està carregat 
        
        if (a_parking.isEmpty())
            throw("No hi ha vehicles");
        else{
        
            Iterator<Vehicle> it_vehicles = a_parking.iterator();
            boolean trobat = false;
            Vehicle v;
            
            while(it_vehicles.hasNext() && v.autonomia() > recorregut && v.nPlaces() > nPersones){
                v = it_vehicles.next();
            }
            
            if (!it_vehicles.hasNext())
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
    private HashSet<Vehicle> a_parking;
    
    
}
