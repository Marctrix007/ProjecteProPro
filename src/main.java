

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Marc Padrós Jiménez
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        Gestio g = new Gestio(); 
        try{
            g.CrearMapa();
            g.CrearVehicles();
            g.CrearPeticions();
            g.AtendrePeticions();
            g.MostrarEstadistics();
        }
        catch(PuntDeRecarrega.ExcepcioNoQuedenVehicles nqv){
            System.err.println("No queden vehicles " + nqv);        
        }
        catch(PuntDeRecarrega.ExcepcioNoQuedenPlaces nqp){
            System.err.println("No queden places " + nqp);
        }
        catch(FileNotFoundException fnf){
            System.err.println("Fitxer no trobat: " + fnf);
        }
        
        catch(IndexOutOfBoundsException ioob){
            System.err.println("Ha sortit fora de rang: " + ioob);
        }
        catch(Exception e){
            System.err.println("Excepcio trobada: " + e);
        }
        
       
    }
    
}
