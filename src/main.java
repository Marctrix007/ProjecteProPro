
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
    public static void main(String[] args) throws IOException {
        Gestio g = new Gestio();
        g.CrearMapa();
        g.CrearVehicles();
        g.AtendrePeticions();
        g.MostrarEstadistics();
    }
    
}
