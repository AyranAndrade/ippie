package br.com.ippie.infraestrutura;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ayran
 */
@Component
public class GerenciadorDeImagens
{
private final GeradorDeNomesAleatorios gerador;

    public GerenciadorDeImagens() 
    {
    gerador=new GeradorDeNomesAleatorios();
    }
    
    public String grava(InputStream inputstream, int alturaFoto, 
            String pastaDestino) throws IOException 
    {
    String extensao="png";
    //VERIFICA SE A PASTA EXISTE
      if(!Paths.get(pastaDestino).toFile().exists())
      {
      new File(pastaDestino).mkdirs();
      }
    //VERIFICA SE O NOME JA EXISTE
    File arq;
    String nome;
      do
      {
      nome=gerador.gera(5,40)+"."+extensao;
      arq=new File(pastaDestino+nome);
      }
      while(arq.exists());      
      //CALCULA AS DIMENSOES DA IMAGEM
    BufferedImage original=ImageIO.read(inputstream);
    int larguraOriginal=original.getWidth();
    int alturaOriginal=original.getHeight();
    int altura=alturaFoto;
    int largura=(larguraOriginal*altura)/alturaOriginal;
    //SALVA A IMAGEM NO DISCO
    int type;
      if(original.getType()==0)
      {
      type=BufferedImage.TYPE_INT_ARGB;
      }
      else
      {
      type=original.getType();
      }
    BufferedImage resizeImageHintPng=resizeImageWithHint(original,type,largura,altura);
    ImageIO.write(resizeImageHintPng,extensao,new File(pastaDestino+nome));
    return nome;
    }
    
    public String grava(InputStream inputstream, String pastaDestino) throws IOException
    {
    String extensao=".png";
    //VERIFICA SE A PASTA EXISTE
      if(!Paths.get(pastaDestino).toFile().exists())
      {
      new File(pastaDestino).mkdirs();
      }
    //VERIFICA SE O NOME JA EXISTE
    File arq;
    String nome;
      do
      {
      nome=gerador.gera(5,40)+extensao;
      arq=new File(pastaDestino+nome);
      }
      while(arq.exists());      
    BufferedImage original=ImageIO.read(inputstream);
    ImageIO.write(original,"png",new File(pastaDestino+nome));    
    return nome;
    }
    
    private BufferedImage resizeImageWithHint(BufferedImage original, int type,
            int IMG_WIDTH, int IMG_HEIGHT) 
    {
    BufferedImage resizedImage=new BufferedImage(IMG_WIDTH, IMG_HEIGHT,type);
    Graphics2D g=resizedImage.createGraphics();
    g.drawImage(original, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    g.setComposite(AlphaComposite.Src);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_RENDERING,
    RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    g.dispose();	
    return resizedImage;
    }    
    
    public void gravaComEsteNome(InputStream inputstream, int alturaFoto,
            String nome, String pastaDestino) throws IOException
    {
      if(!Paths.get(pastaDestino).toFile().exists())
      {
      new File(pastaDestino).mkdirs();
      }
    //CALCULA AS DIMENSOES DA IMAGEM
    BufferedImage original=ImageIO.read(inputstream);
    int larguraOriginal=original.getWidth();
    int alturaOriginal=original.getHeight();
    int altura=alturaFoto;
    int largura=(larguraOriginal*altura)/alturaOriginal;
    //SALVA A IMAGEM NO DISCO
    int type;
      if(original.getType()==0)
      {
      type=BufferedImage.TYPE_INT_ARGB;
      }
      else
      {
      type=original.getType();
      }
    BufferedImage resizeImageHintPng=resizeImageWithHint(original,type,largura,altura);
    ImageIO.write(resizeImageHintPng,"png",new File(pastaDestino+nome));
    }
}