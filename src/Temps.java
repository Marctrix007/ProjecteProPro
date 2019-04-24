/**
 * @author Xavier Rodríguez i Martínez
 */
public class Temps implements Comparable<Temps>{
    //Descripció general: Classe Temps amb Hora:minut

    private final int hora;
    private final int minut;

    public Temps(){
        hora = 0;
        minut = 0;
    }

    public Temps(int h, int m){
        //Pre: --
        //Post: Temps creat amb l'excés de minuts corregits
        hora = h + m/60;
        minut = m%60;
    }

    public Temps(float t){
      //Pre: --
      //Post: L'hora és l'enter i els minuts la fracció centesimal dels decimals
      hora = (int) t;
      t -= hora;
      minut = (int) t*60;
    }

    public Temps mes(Temps t){
        //Pre: --
        //Post: Retorna els temps de  this  i  t  sumats.
        return new Temps(this.hora+t.hora, this.minut+t.minut);
    }

    public Temps menys(Temps t){
        //Pre: --
        //Post: Retora la diferència de  this  i  t.
        int m1 = this.hora*60 + this.minut;
        int m2 = t.hora*60 + t.minut;
        return new Temps(0, m1-m2);
    }


    public Temps per(float n){
        //Pre: --
        //Post: multiplica el temps  this  per  n  vegades
        int m = this.minut + this.hora*60;
        m = (int) (m*n);
        return new Temps(0, m);
    }

    @Override
    public int compareTo(Temps t){
      int r = this.hora - t.hora;
      if (r==0) r = this.minut - t.minut;
      return r;
    }

    @Override
    public String toString(){
        if (minut <= -10) return "-" + (-hora) + ":" + (-minut);
        else if (minut < 0) return "-" + (-hora) + ":0" + (-minut);
        else if (minut<10) return hora + ":0" + minut;
        else return hora + ":" + minut;
    }
}
