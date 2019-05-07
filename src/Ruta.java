
import java.util.ArrayDeque;
import java.util.Iterator;

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
    
    public void Concatenar(Ruta r) {
        //Pre: --
        //Post: Es concatenen la ruta actual i  r  afegint aquesta al final
        
        pes = pes.mes(r.pes);
        Iterator<Integer> ite = r.cami.iterator();
        while (ite.hasNext())
            cami.add(ite.next());
    }
    
    public boolean Conte(Ruta r){
        return false;
    } 
}
