
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */
public class main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {
    
        Gestio g = new Gestio(); 
        g.CrearMapa();
        g.CrearVehicles();
        g.CrearPeticions();
        g.AtendrePeticions();
        g.MostrarEstadistics();
        
    }
    
}
