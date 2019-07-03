package br.com.ippie.dao;

import br.com.ippie.negocio.Aprovacao;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Comentario;
import br.com.ippie.negocio.Concordar;
import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.Discordar;
import br.com.ippie.negocio.Login;
import br.com.ippie.negocio.Pesame;
import br.com.ippie.negocio.Reprovacao;
import br.com.ippie.negocio.TipoConteudo;
import br.com.ippie.negocio.Usuario;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author Ayran
 */
public class AuxiliarJpa 
{
    public Collection<Aprovacao> aprovacoes(List<BigInteger> resultList) 
    {
    Collection<Aprovacao> aprovacoes=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(o.longValue());
      Aprovacao a=new Aprovacao(u);
      aprovacoes.add(a);
      });
    return aprovacoes;
    } 

    public Collection<Reprovacao> reprovacoes(List<BigInteger> resultList) 
    {
    Collection<Reprovacao> reprovacoes=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(o.longValue());
      Reprovacao r=new Reprovacao(u);
      reprovacoes.add(r);
      });
    return reprovacoes;
    }

    public Collection<Pesame> pesames(List<BigInteger> resultList) 
    {
    Collection<Pesame> pesames=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(o.longValue());
      Pesame p=new Pesame(u);
      pesames.add(p);
      });
    return pesames;
    }

    public Collection<Concordar> concordar(List<BigInteger> resultList) 
    {
    Collection<Concordar> concordar=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(o.longValue());
      Concordar c=new Concordar(u);
      concordar.add(c);
      });
    return concordar;
    }

    public Collection<Discordar> discordar(List<BigInteger> resultList) 
    {
    Collection<Discordar> discordar=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(o.longValue());
      Discordar d=new Discordar(u);
      discordar.add(d);
      });
    return discordar;
    }
    
    public Collection<Conteudo> conteudos(List<Object[]> obj) 
    {
    Collection<Conteudo> conteudos=new LinkedList<>();
      obj.stream().forEach((o)->
      {
      Conteudo p=new Conteudo();
      p.setCodigo(((BigInteger)o[0]).longValue());
      p.setTexto((String)o[1]);
      TipoConteudo t=new TipoConteudo();
        if(((int)o[2])==1)
        {
        t.setNome("Normal");
        }
        else if(((int)o[2])==2)
        {
        t.setNome("Morte");
        }
        else if(((int)o[2])==3)
        {
        t.setNome("Opiniao");
        }
      p.setTipo(t);
      p.setUrlLink((String)o[3]);
      p.setUrlVideo((String)o[4]);
      p.setUrlImagem((String)o[5]);
      p.setDataCriacao(LocalDateTime.ofInstant(((Date)o[6]).toInstant(),ZoneId.systemDefault()));
      Usuario u=new Usuario();
      u.setCodigo(((BigInteger)o[7]).longValue());
      u.setNome((String)o[8]);
      u.setSobrenome((String)o[9]);
      u.setFotoPerfil((String)o[10]);
      u.setFotoCabecalho((String)o[11]);
      u.setDescricao((String)o[12]);
      int comentarios=((BigInteger)o[13]).intValue();
        for(int i=0;i<comentarios;i++)
        {
        p.getComentarios().add(new Comentario());
        }
      String tags=(String)o[14];
      String[] cadaTag=tags.split(",");
        for (String cadaTag1 : cadaTag)
        {
        Assunto a=new Assunto();
        String[] temas = cadaTag1.split(";");
        a.setCodigo(Long.valueOf(temas[0]));
        a.setNome(temas[1]);
        a.setUrlImagem(temas[2]);
        p.getAssuntos().add(a);
        }
      p.setAutor(u);
      conteudos.add(p);
      });
    return conteudos;
    }
    
    public Collection<Assunto> assuntos(List<Object[]> resultList) 
    {
    Collection<Assunto> assuntos=new ArrayList<>();
      resultList.stream().forEach((o)->
      {
      Assunto a=new Assunto();
      a.setCodigo(((BigInteger)o[0]).longValue());
      a.setNome((String)o[1]);
      a.setUrlImagem((String)o[2]);
      a.setDataCriacao(LocalDateTime.ofInstant(((Date)o[3]).toInstant(),ZoneId.systemDefault()));
      assuntos.add(a);
      });
    return assuntos;
    }
    
    public Collection<Comentario> comentarios(List<Object[]> objects)
    {
    Collection<Comentario> comentarios=new LinkedList<>();
      objects.stream().forEach((o)->
      {
      Comentario c=new Comentario();
      c.setCodigo(((BigInteger)o[0]).longValue());
      c.setTexto((String)o[1]);
      c.setUrlImagem((String)o[2]);
      c.setDataCriacao(LocalDateTime.ofInstant(((Date)o[3]).toInstant(),ZoneId.systemDefault()));
      Usuario u=new Usuario();
      u.setCodigo(((BigInteger)o[4]).longValue());
      u.setNome((String)o[5]);
      u.setSobrenome((String)o[6]);
      u.setFotoPerfil((String)o[7]);
      u.setFotoCabecalho((String)o[8]);
      u.setDescricao((String)o[9]);
      c.setAutor(u);
      comentarios.add(c);
      });
    return comentarios;
    }
    
    public Collection<Conteudo> notificacoes(List<Object[]> obj)
    {
    Collection<Conteudo> conteudos=new LinkedList<>();
      obj.stream().forEach((o)->
      {
      Conteudo p=new Conteudo();
      p.setCodigo(((BigInteger)o[0]).longValue());
      p.setTexto((String)o[1]);
      TipoConteudo t=new TipoConteudo();
        if(((int)o[2])==1)
        {
        t.setNome("Normal");
        }
        else if(((int)o[2])==2)
        {
        t.setNome("Morte");
        }
        else if(((int)o[2])==3)
        {
        t.setNome("Opiniao");
        }
      p.setTipo(t);
      p.setUrlLink((String)o[3]);
      p.setUrlVideo((String)o[4]);
      p.setUrlImagem((String)o[5]);
      Usuario u=new Usuario();
      u.setCodigo(((BigInteger)o[6]).longValue());
      u.setNome((String)o[7]);
      u.setSobrenome((String)o[8]);
      u.setFotoPerfil((String)o[9]);
      u.setFotoCabecalho((String)o[10]);
      u.setDescricao((String)o[11]);
      int comentarios=((BigInteger)o[12]).intValue();
        for(int i=0;i<comentarios;i++)
        {
        p.getComentarios().add(new Comentario());
        }
      p.setAutor(u);
      conteudos.add(p);
      });
    return conteudos;
    }
    
    public Collection<Conteudo> perfil(Usuario u,List<Object[]> obj)
    {
    Collection<Conteudo> conteudos=new LinkedList<>();
      for(Object[] o : obj)
      {
      Conteudo p=new Conteudo();
      p.setCodigo(((BigInteger)o[0]).longValue());
      p.setTexto((String)o[1]);
      TipoConteudo t=new TipoConteudo();
        if(((int)o[2])==1)
        {
        t.setNome("Normal");
        }
        else if(((int)o[2])==2)
        {
        t.setNome("Morte");
        }
        else if(((int)o[2])==3)
        {
        t.setNome("Opiniao");
        }
      p.setTipo(t);
      p.setUrlLink((String)o[3]);
      p.setUrlVideo((String)o[4]);
      p.setUrlImagem((String)o[5]);
      int comentarios=((BigInteger)o[6]).intValue();
        for(int i=0;i<comentarios;i++)
        {
        p.getComentarios().add(new Comentario());
        }
      String tags=(String)o[7];
      String[] cadaTag=tags.split(",");
        for (String cadaTag1 : cadaTag)
        {
        Assunto a=new Assunto();
        String[] temas = cadaTag1.split(";");
        a.setCodigo(Long.valueOf(temas[0]));
        a.setNome(temas[1]);
        a.setUrlImagem(temas[2]);
        p.getAssuntos().add(a);
        }
      p.setAutor(u);
      conteudos.add(p);
      }
    return conteudos;
    }
    
    public Collection<Conteudo> conteudosParaDenuncia(List<Object[]> resultList) 
    {
    Collection<Conteudo> conteudos=new HashSet<>();
      resultList.stream().forEach((o)->
      {
      Conteudo c=new Conteudo();
      c.setCodigo(((BigInteger)o[0]).longValue());
      c.setTexto((String)o[1]);
      c.setUrlLink((String)o[2]);
      c.setUrlVideo((String)o[3]);
      c.setUrlImagem((String)o[4]);
      conteudos.add(c);
      });
    return conteudos;
    }
    
    public Usuario usuarios(List<Object[]> singleResult) 
    {
    Usuario u=new Usuario();
    Object[] o;
      try
      {
      o=singleResult.stream().findFirst().get();
      }
      catch(NoSuchElementException ex)
      {
      return null;
      }
    u.setCodigo(((BigInteger)o[0]).longValue());
    u.setNome((String)o[1]);
    u.setSobrenome((String)o[2]);
    u.setFotoPerfil((String)o[3]);
    u.setFotoCabecalho((String)o[4]);
    u.setDescricao((String)o[5]);
    u.setModerador((Boolean)o[8]);
    Login l=new Login();
    l.setCodigo(((BigInteger)o[6]).longValue());
    l.setEmail((String)o[9]);
    l.setSenha((String)o[7]);
    u.setLogin(l);
    return u;
    }
    
    public Collection<Usuario> usuariosParaPesquisa(List<Object[]> resultList) 
    {
    Collection<Usuario> usuarios=new LinkedList<>();
      resultList.stream().map((o)->
      {
      Usuario u=new Usuario();
      u.setCodigo(((BigInteger)o[0]).longValue());
      u.setNome((String)o[1]);
      u.setSobrenome((String)o[2]);
      u.setFotoPerfil((String)o[3]);
      u.setDescricao((String)o[4]);
      u.setFotoCabecalho((String)o[5]);
      return u;
      }).forEach((u)->
      {
      usuarios.add(u);
      });
    return usuarios;
    } 
    
    public Login login(List<Object[]> obj)
    {
      if(obj==null || obj.isEmpty() || obj.size()>1)
      {
      return null;    
      }
    Object[] g=obj.stream().findFirst().get();
    Login l=new Login();
    l.setCodigo(((BigInteger)g[0]).longValue());
    l.setEmail((String)g[1]);
    l.setSenha((String)g[2]);
    return l;
    }
}