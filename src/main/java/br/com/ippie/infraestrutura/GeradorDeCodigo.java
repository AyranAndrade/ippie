package br.com.ippie.infraestrutura;

import java.util.ArrayList;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ayran
 */
@Component
public class GeradorDeCodigo
{
    public Integer gera(int minimoDeCaracteres,int maximoDeCaracteres)
    {
    Random g=new Random();
    ArrayList<Integer> a=new ArrayList();
    int b;
    String c="";//NAO PODEM OS CARACTERES *|\:""<>?/
    a.add(1);
    a.add(2);
    a.add(3);
    a.add(4);
    a.add(5);
    a.add(6);
    a.add(7);
    a.add(8);
    a.add(9);
    a.add(0);
      do
      {
      b=g.nextInt(maximoDeCaracteres+1);//Valor Maximo
      }
      while (b<minimoDeCaracteres);//Valor Minimo
      for (int i=0;i<b;i++)
      {
      c=c+a.get(g.nextInt(a.size()));
      }
    a.clear();
    return Integer.valueOf(c);
    }
}