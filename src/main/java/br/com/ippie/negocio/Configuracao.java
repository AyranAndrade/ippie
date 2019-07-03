package br.com.ippie.negocio;

import java.io.File;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ayran
 */
@Component
public final class Configuracao 
{
public static final int Desenvolvimento=0;

public static final int Producao=1;

public static final int minimoDeAssuntosPorConteudo=2;

public static final int maximoDeAssuntosPorConteudo=5;

public static final int minimoDeAssuntosFavoritos=3;

private final String relativo;

private final String absoluto;

    public Configuracao(int fase) 
    {
      if(fase!=Producao && fase!=Desenvolvimento)
      {
      throw new IllegalArgumentException("Use As Constantes Desta Classe!");
      }
      if(fase==Producao)
      {
      absoluto=System.getProperty("catalina.home")+File.separator+"webapps"
              +File.separator;
      relativo="../../../";
      }
      else
      {
        if(System.getProperty("os.name").contains("Windows"))
        {
        absoluto="C:\\Users\\Ayran\\Ippie3\\target\\ippie-1.0.0-SNAPSHOT\\";//trocar este caminho aqui
        relativo="../";
        }
        else
        {
        absoluto="/home/user/ayran/Ippie3/target/ippie-1.0.0-SNAPSHOT/";//trocar este caminho aqui
        relativo="../";
        }
      }
    }
    
    public final int minimoDeAssuntosFavoritos()
    {
    return minimoDeAssuntosFavoritos;
    }
    
    public final int maximoDeAssuntosPorConteudo()
    {
    return maximoDeAssuntosPorConteudo;
    }
    
    public final int minimoDeAssuntosPorConteudo()
    {
    return minimoDeAssuntosPorConteudo;
    }
    
    public final String fotoCabecalhoDefault()
    {
    return "cabecalho_default.jpg";
    }
    
    public final String fotoPerfilDefault()
    {
    return "perfil_default.jpg";
    }
    
    public final String fotoAssuntoDefault()
    {
    return "assunto_default.png";
    }
    
    public final String fotoCabecalhoRelativo()
    {
    return relativo+"cabecalho/";
    }  
    
    public final String fotoPerfilRelativo()
    {
    return relativo+"perfil/";
    }

    public final String fotoOuVideoConteudoRelativo()
    {
    return relativo+"conteudo/";
    }

    public final String fotoComentarioRelativo()
    {
    return relativo+"comentario/";
    }
    
    public final String fotoAssuntoRelativo()
    {
    return relativo+"assunto/";
    }
    
    public final String fotoAssuntoRelativoCadastro()
    {
    return "assunto/";
    }
    
    public final String fotoCabecalhoAbsoluto()
    {
    return absoluto+"cabecalho"+File.separator;
    }  
    
    public final String fotoPerfilAbsoluto()
    {
    return absoluto+"perfil"+File.separator;
    }

    public final String fotoOuVideoConteudoAbsoluto()
    {
    return absoluto+"conteudo"+File.separator;
    }

    public final String fotoComentarioAbsoluto()
    {
    return absoluto+"comentario"+File.separator;
    }
    
    public final String fotoAssuntoAbsoluto()
    {
    return absoluto+"assunto"+File.separator;
    }    
    
    public final int alturaFotoCabecalho()
    {
    return 270;
    }
    
    public final int alturaFotoPerfil()
    {
    return 150;
    }
    
    public final int alturaFotoComentario()
    {
    return 150;
    }
    
    public final int alturaFotoConteudo()
    {
    return 450;
    }
    
    public final int alturaFotoAssunto()
    {
    return 100;
    }
}