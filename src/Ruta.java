
import java.util.ArrayDeque;

/**
 *
 * @author Xavier Rodríguez i Martínez
 */
public class Ruta {
    
    private Pes pes;
    private ArrayDeque<Integer> cami;
    
    Ruta(){
        cami = new ArrayDeque<>();
        pes = new Pes();
    }
    
    void addFirst(int u) {
    //Pre: --
    //Post: u afegit al principi del camí.
        cami.addFirst(u);
    }

    void afegirPes(Pes p) {
    //Pre: --
    //Post: Es suma el Pes p al de la ruta.
        pes = pes.mes(p);
    }
    
    public Pes cost(){
    //Pre: --
    //Post: retorna el pes (distància i temps total) d'una ruta
        return pes;
    }
}
