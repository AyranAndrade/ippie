package br.com.ippie.infraestrutura;

import java.util.ArrayList;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 *
 * @author ayran
 */
@Component
public class GeradorDeNomesAleatorios
{
    public String gera(int minimoDeCaracteres,int maximoDeCaracteres)
    {
    Random g=new Random();
    ArrayList a=new ArrayList();
    int b;
    String c="";//NAO PODEM OS CARACTERES *|\:""<>?/
    a.add("-");
    a.add("_");
    a.add(".");
    a.add("a");
    a.add("b");
    a.add("c");
    a.add("d");
    a.add("e");
    a.add("f");
    a.add("g");
    a.add("h");
    a.add("i");
    a.add("j");
    a.add("k");
    a.add("l");
    a.add("m");
    a.add("n");
    a.add("o");
    a.add("p");
    a.add("q");
    a.add("r");
    a.add("s");
    a.add("t");
    a.add("u");
    a.add("v");
    a.add("x");
    a.add("y");
    a.add("z");
    a.add("w");
    a.add("1");
    a.add("2");
    a.add("3");
    a.add("4");
    a.add("5");
    a.add("6");
    a.add("7");
    a.add("8");
    a.add("9");
    a.add("0");
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
    return c;
    }
}