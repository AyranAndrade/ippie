package br.com.ippie.bean;

import br.com.ippie.negocio.Aprovacao;
import br.com.ippie.negocio.Comentario;
import br.com.ippie.negocio.Concordar;
import br.com.ippie.negocio.Configuracao;
import br.com.ippie.negocio.Discordar;
import br.com.ippie.infraestrutura.GerenciadorDeImagens;
import br.com.ippie.negocio.Pesame;
import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeConteudos;
import br.com.ippie.negocio.Reprovacao;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.TipoConteudo;
import br.com.ippie.negocio.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class ConteudoBean 
{
private String comentario;//Aqui fica o texto do comentario
private Conteudo conteudoParaComentario;//Serve para comentar o conteudo
private String conteudo;//Aqui fica o texto do conteudo
private InputStream fotoOuVideoConteudo;//Aqui fica a foto do conteudo
private InputStream fotoComentario;//Aqui fica a foto do comentÃ¡rio
private Conteudo conteudoParaEditar;//Seleciona um conteudo para editar
private Conteudo conteudoParaExcluir;//Seleciona um conteudo para excluir
private Collection<Assunto> assuntosConteudo;//Quando for criar um conteudo, aqui ficam os assuntos do conteudo
private final Validator validator;
private Comentario comentarioParaEditar;//Seleciona um comentÃ¡rio para editar
private Comentario comentarioParaExcluir;//Seleciona um comentario para excluir
private final RepositorioDeConteudos conteudos;
private Collection<Usuario> usuariosMarcados;//Quando for criar um conteudo, aqui vÃ£o ficar os usuarios marcados
private Collection<Conteudo> roldeConteudos;
private Collection<Conteudo> perfil;
private final GerenciadorDeImagens imagens;
private final Configuracao config;
private String tipoConteudo;//Para o usuÃ¡rio selecionar o tipo de conteudo na hora de postar.
private String link;//Link do conteudo
private Usuario perfilParaVisistar;//Perfil para visitar
private String idBotaoComentario;
private Conteudo conteudoParaVerAssuntos;
private Collection<Conteudo> notificacoes;

    @Autowired
    public ConteudoBean(RepositorioDeConteudos conteudos, GerenciadorDeImagens imagens,
            Configuracao config)
    {
    this.conteudos=conteudos;
    validator=Validation.buildDefaultValidatorFactory().getValidator();
    this.imagens=imagens;
    this.config=config;
    tipoConteudo="Normal";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setConteudoParaComentario(Conteudo conteudoParaComentario) {
        this.conteudoParaComentario = conteudoParaComentario;
    }

    public Conteudo getConteudoParaComentario() {
        return conteudoParaComentario;
    }

    public Conteudo getConteudoParaVerAssuntos() {
        return conteudoParaVerAssuntos;
    }

    public void setConteudoParaVerAssuntos(Conteudo conteudoParaVerAssuntos) {
        this.conteudoParaVerAssuntos = conteudoParaVerAssuntos;
    }

    public void setConteudoParaEditar(Conteudo p) 
    {
    this.conteudoParaEditar=p;
    conteudo=p.getTexto();
    assuntosConteudo=p.getAssuntos();
    link=p.getUrlLink();
    tipoConteudo=p.getTipo().getNome();
        System.out.println("setConteudoParaEditar="+conteudo);
    }
    
    public void anulaPostarConteudo()
    {
    conteudo=null;
    assuntosConteudo=null;
    link=null;
    tipoConteudo="Normal";
    fotoOuVideoConteudo=null;
    conteudoParaEditar=null;
    }

    public void setConteudoParaExcluir(Conteudo conteudoParaExcluir) {
        this.conteudoParaExcluir = conteudoParaExcluir;
    }

    public void setComentarioParaEditar(Comentario comentarioParaEditar) {
        this.comentarioParaEditar = comentarioParaEditar;
        comentario=comentarioParaEditar.getTexto();
    }

    public void setComentarioParaExcluir(Comentario comentarioParaExcluir) {
        this.comentarioParaExcluir = comentarioParaExcluir;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Collection<Assunto> getAssuntosConteudo() {
        return assuntosConteudo;
    }

    public void setAssuntosConteudo(Collection<Assunto> assuntosPost) {
        this.assuntosConteudo = assuntosPost;
    }

    /**
     * Seta quais sao os usuarios marcados no conteudo feito.
     * @return
     */
    public Collection<Usuario> getUsuariosMarcados() {
        return usuariosMarcados;
    }

    /**
     * Seta quais sao os usuarios marcados no conteudo feito.
     * @param usuariosMarcados
     */
    public void setUsuariosMarcados(Collection<Usuario> usuariosMarcados) {
        this.usuariosMarcados = usuariosMarcados;
    }
    
    public Collection<Conteudo> rolDeConteudos(Usuario u)
    {
      if(roldeConteudos==null)
      {long i=System.currentTimeMillis();
      roldeConteudos=conteudos.rolDeConteudos(u);
      long t=System.currentTimeMillis();
      System.out.println("tempo="+(t-i));
      }
    return roldeConteudos;
    }
    
    public Collection<Conteudo> perfil(Usuario u)
    {
      if(perfil==null)
      {
      perfil=conteudos.perfil(u);
      }
    return perfil;
    }
    
    public Collection<Conteudo> perfil()
    {
      if(perfil==null)
      {
      perfil=conteudos.perfil(perfilParaVisistar);
      }
    return perfil;
    }
    
    @Transactional
    public void aprova(Conteudo p)
    {
    Aprovacao a=new Aprovacao(usuario());
    p.getAprovacoes().add(a);
    conteudos.aprova(p,a);
    conteudos.notifica(usuario(),p);
      if(estaReprovado(p))
      {
      Reprovacao r=new Reprovacao(usuario());
      p.getReprovacoes().remove(r);
      conteudos.exclui(p, r);
      }
    }
    
    @Transactional
    public void reprova(Conteudo p)
    {
    Reprovacao r=new Reprovacao(usuario());
    p.getReprovacoes().add(r);
    conteudos.reprova(p, r);
    conteudos.notifica(usuario(), p);
      if(estaAprovado(p))
      {
      Aprovacao a=new Aprovacao(usuario());
      p.getAprovacoes().remove(a);
      conteudos.exclui(p, a);
      }
    }
    
    public boolean estaAprovado(Conteudo p)
    {
    Aprovacao a=new Aprovacao(usuario());
    return p.getAprovacoes().contains(a);
    }
    
    public boolean estaReprovado(Conteudo p)
    {
    Reprovacao r=new Reprovacao(usuario());
    return p.getReprovacoes().contains(r);
    }
    
    @Transactional
    public void comenta()
    {
    Comentario c=new Comentario();
    c.setAutor(usuario());
    c.setTexto(comentario);
    c.setDataCriacao(LocalDateTime.now());
    boolean erro=false;
      if(fotoComentario!=null)
      {
        try
        {
        String url=imagens.grava(fotoComentario,config
                    .alturaFotoComentario(),config.fotoComentarioAbsoluto());
        c.setUrlImagem(url);
        }
        catch (IOException ex) 
        {
        Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_WARN,"Falha nossa!","Tivemos um problema "
                    + "interno, afinal, todos nos erramos :/ Por favor "
                    + "recarregue a pagina."));
        erro=true;
        }
      }
    Set<ConstraintViolation<Comentario>> v=validator.validate(c);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else if(!erro)
      {
      conteudos.comenta(conteudoParaComentario,c);
      conteudoParaComentario.getComentarios().add(c);
      conteudos.notifica(usuario(), conteudoParaComentario);
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Comentário efetuado com sucesso!"));
      fotoComentario=null;
      comentario=null;
      RequestContext.getCurrentInstance().update(idBotaoComentario);
      }
    }
    
    @Transactional
    public void postarConteudo()
    {
    Conteudo p=new Conteudo();
    p.setAutor(usuario());
    p.setTexto(conteudo);
    p.setDataCriacao(LocalDateTime.now());
    p.setAssuntos(assuntosConteudo);
    p.setUsuariosMarcados(usuariosMarcados);
    p.setUrlLink(link.replace("http://","").replace("https://",""));
    p.setTipo(new TipoConteudo(tipoConteudo));
    boolean erro=false;
      if(fotoOuVideoConteudo!=null)
      {
        try 
        {
        String url=imagens.grava(fotoOuVideoConteudo,config.alturaFotoConteudo(),
                config.fotoOuVideoConteudoAbsoluto());
        p.setUrlImagem(url);
        }
        catch (IOException ex) 
        {
        Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_WARN,"Falha nossa!","Tivemos um problema "
                    + "interno, afinal, todos nos erramos :/ Por favor "
                    + "recarregue a pagina."));
        erro=true;
        }
      }
    Set<ConstraintViolation<Conteudo>> v=validator.validate(p);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else if(!erro)
      {
      conteudos.adiciona(p);
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Conteúdo postado com sucesso!"));
      conteudos.notifica(usuario(),p);
      anulaPostarConteudo();
      }
    }
    
    @Transactional
    public void aprova(Comentario c)
    {
    Aprovacao a=new Aprovacao(usuario());   
    c.getAprovacoes().add(a);
    conteudos.aprova(c, a);
    }
    
    @Transactional
    public void reprova(Comentario c)
    {
    Reprovacao r=new Reprovacao(usuario());
    c.getReprovacoes().add(r);
    conteudos.reprova(c, r);
    }
    
    public boolean estaAprovado(Comentario c)
    {
    Aprovacao a=new Aprovacao(usuario());
    return c.getAprovacoes().contains(a);
    }
    
    public boolean estaReprovado(Comentario c)
    {
    Reprovacao r=new Reprovacao(usuario());
    return c.getReprovacoes().contains(r);
    }
    
    @Transactional
    public void retiraAprovacao(Conteudo p)
    {
    Aprovacao a=new Aprovacao(usuario());
    p.getAprovacoes().remove(a);
    conteudos.exclui(p,a);
    }
    
    @Transactional
    public void retiraReprovacao(Conteudo p)
    {
    Reprovacao r=new Reprovacao(usuario());
    p.getReprovacoes().remove(r);
    conteudos.exclui(p, r);
    }
    
    @Transactional
    public void retiraAprovacao(Comentario c)
    {
    Aprovacao a=new Aprovacao(usuario());
    c.getAprovacoes().remove(a);
    conteudos.exclui(c, a);
    }
    
    @Transactional
    public void retiraReprovacao(Comentario c)
    {
    Reprovacao r=new Reprovacao(usuario());
    c.getReprovacoes().remove(r);
    conteudos.exclui(c, r);
    }
    
    @Transactional
    public void edita() throws IOException
    {
    conteudoParaEditar.setTexto(conteudo);
    conteudoParaEditar.setUrlLink(link);
    boolean erro=false;
      if(fotoOuVideoConteudo!=null)
      {
        try 
        {
        String url=imagens.grava(fotoOuVideoConteudo,config.alturaFotoConteudo(),
                config.fotoOuVideoConteudoAbsoluto());
        conteudoParaEditar.setUrlImagem(url);
        }
        catch (IOException ex) 
        {
        Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_WARN,"Falha nossa!","Tivemos um problema "
                    + "interno, afinal, todos nos erramos :/ Por favor "
                    + "recarregue a pagina."));
        erro=true;
        }
      }
    Set<ConstraintViolation<Conteudo>> v=validator.validate(conteudoParaEditar);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      erro=true;
      }
      if(!erro)
      {
      conteudos.edita(conteudoParaEditar);
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Conteúdo editado com sucesso!"));
      anulaPostarConteudo();
      redirecionaRolDeConteudos();
      }
    }

    public Comentario getComentarioParaEditar() {
        return comentarioParaEditar;
    }

    public Comentario getComentarioParaExcluir() {
        return comentarioParaExcluir;
    }
    
    @Transactional
    public void exclui() throws IOException
    {
    conteudos.exclui(conteudoParaExcluir);
    /*FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Conteúdo excluído com sucesso!"));*/
    redirecionaRolDeConteudos();
    }
    
    @Transactional
    public void editaComentario()
    {
    comentarioParaEditar.setTexto(comentario);
    boolean erro=false;
      if(fotoComentario!=null)
      {
        try
        {
        String url=imagens.grava(fotoComentario,config
                    .alturaFotoComentario(),config.fotoComentarioAbsoluto());
        comentarioParaEditar.setUrlImagem(url);
        }
        catch (IOException ex) 
        {
        Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_WARN,"Falha nossa!","Tivemos um problema "
                    + "interno, afinal, todos nós erramos :/ Por favor "
                    + "recarregue a página."));
        erro=true;
        }
      }
    Set<ConstraintViolation<Comentario>> v=validator.validate(comentarioParaEditar);
      if(!v.isEmpty())
      {
      erro=true;
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      if(!erro)
      {
      conteudos.edita(comentarioParaEditar);
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Comentário editado com sucesso!"));
      fotoComentario=null;
      comentario=null;
      comentarioParaEditar=null;
      RequestContext.getCurrentInstance().reset("frmComentario:textoComentario");
      RequestContext.getCurrentInstance().update(idBotaoComentario);
      RequestContext.getCurrentInstance().update("frmComentario");
      }        
    }
    
    @Transactional
    public void excluiComentario()
    {
    conteudos.exclui(comentarioParaExcluir);
    conteudoParaComentario.getComentarios().remove(comentarioParaExcluir);
    RequestContext.getCurrentInstance().update(idBotaoComentario);
    comentarioParaExcluir=null;
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Comentário excluído com sucesso!"));
    }
    
    @Transactional
    public void concorda(Conteudo p)
    {
    Concordar c=new Concordar(usuario());
    p.getConcordar().add(c);
    conteudos.concorda(p,c);
    conteudos.notifica(usuario(), p);
      if(estaDiscordado(p))
      {
      Discordar d=new Discordar(usuario());
      p.getDiscordar().remove(d);
      conteudos.exclui(p,d);
      }
    }
    
    @Transactional
    public void discorda(Conteudo p)
    {
    Discordar d=new Discordar(usuario());
    p.getDiscordar().add(d);
    conteudos.discorda(p, d);
    conteudos.notifica(usuario(), p);
      if(estaConcordado(p))
      {
      Concordar c=new Concordar(usuario());
      p.getConcordar().remove(c);
      conteudos.exclui(p, c);
      }
    }
    
    @Transactional
    public void pesame(Conteudo p)
    {
    Pesame pe=new Pesame(usuario());
    p.getPesames().add(pe);
    conteudos.pesames(p, pe);
    conteudos.notifica(usuario(), p);
    }
    
    public boolean estaConcordado(Conteudo p)
    {
    Concordar c=new Concordar(usuario());
    return p.getConcordar().contains(c);
    }
    
    public boolean estaDiscordado(Conteudo p)
    {
    Discordar d=new Discordar(usuario());
    return p.getDiscordar().contains(d);
    }
    
    public boolean conteudoNormal(Conteudo p)
    {
    return p.getTipo().getNome().equals("Normal");
    }
    
    public boolean conteudoSobreMorte(Conteudo p)
    {
    return p.getTipo().getNome().equals("Morte");
    }
    
    public boolean conteudoSobreOpiniao(Conteudo p)
    {
    return p.getTipo().getNome().equals("Opiniao");
    }
    
    public Collection<Conteudo> rolDeConteudosApenasFotos(Usuario u)
    {
    return conteudos.apenasConteudosComFotos(u);
    }
    
    private Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }

    public Usuario getPerfilParaVisistar() {
        return perfilParaVisistar;
    }

    public void setPerfilParaVisistar(Usuario perfilParaVisistar) {
        this.perfilParaVisistar = perfilParaVisistar;
    }
    
    public void redirecionaPerfil() throws IOException
    {
    perfil=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("perfil.xhtml");    
    }
    
    @Transactional
    public void retiraConcordar(Conteudo p)
    {
    Concordar c=new Concordar(usuario());
    p.getConcordar().remove(c);
    conteudos.exclui(p, c);
    }
    
    @Transactional
    public void retiraDiscordar(Conteudo p)
    {
    Discordar d=new Discordar(usuario());
    p.getDiscordar().remove(d);
    conteudos.exclui(p, d);
    }
    
    @Transactional
    public void retiraPesame(Conteudo p)
    {
    Pesame pe=new Pesame(usuario());
    p.getPesames().remove(pe);
    conteudos.exclui(p, pe);
    }
    
    public boolean estaComPesame(Conteudo p)
    {
    Pesame pe=new Pesame(usuario());
    return p.getPesames().contains(pe);
    }
    
    public int quantasAprovacoes(Conteudo p)
    {
    return p.getAprovacoes().size();
    }
    
    public int quantasReprovacoes(Conteudo p)
    {
    return p.getReprovacoes().size();
    }
    
    public int quantosPesames(Conteudo p)
    {
    return p.getPesames().size();
    }
    
    public int quantosConcordar(Conteudo p)
    {
    return p.getConcordar().size();
    }
    
    public int quantosDiscordar(Conteudo p)
    {
    return p.getDiscordar().size();
    }
    
    public int quantosComentarios(Conteudo p)
    {
    return p.getComentarios().size();
    }
    
    public void comentarios(Conteudo p)
    {
    p.setComentarios(conteudos.comentarios(p));
    }

    public String getIdBotaoComentario() {
        return idBotaoComentario;
    }

    public void setIdBotaoComentario(String idBotaoComentario) {
        this.idBotaoComentario = idBotaoComentario;
    }
    
    public void fotoComentario(FileUploadEvent event)
    {
      try 
      {
      fotoComentario=event.getFile().getInputstream();
      }
      catch (IOException ex) 
      {
      Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public boolean upouFotoComentario()
    {
    return fotoComentario!=null;
    }
    
    public void fotoOuVideoConteudo(FileUploadEvent event)
    {
      try 
      {
      fotoOuVideoConteudo=event.getFile().getInputstream();
      }
      catch (IOException ex) 
      {
      Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public boolean upouFotoOuVideoConteudo()
    {
    return fotoOuVideoConteudo!=null;
    }
    
    public void excluiImagemComentario()
    {
      if(comentarioParaEditar!=null)
      {
      comentarioParaEditar.setUrlImagem(null);
      }
    }
    
    public void excluiImagemOuVideoConteudo()
    {
      if(conteudoParaEditar!=null)
      {
      conteudoParaEditar.setUrlImagem(null);
      }
    }
    
    public boolean conteudoParaEditarPossuiImagem()
    {
      if(conteudoParaEditar==null)
      {
      return false;
      }
    return conteudoParaEditar.getUrlImagem()==null;
    }
    
    public boolean comentarioParaEditarPossuiImagem()
    {
      if(comentarioParaEditar==null)
      {
      return false;
      }
    return comentarioParaEditar.getUrlImagem()==null;
    }

    public Conteudo getConteudoParaEditar() {
        return conteudoParaEditar;
    }

    public Conteudo getConteudoParaExcluir() {
        return conteudoParaExcluir;
    }
    
    public void redirecionaRolDeConteudos() throws IOException
    {
    roldeConteudos=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("rolDeConteudos.xhtml");
    }
    
    public void redirecionaRolDeConteudosRestrito() throws IOException
    {
    roldeConteudos=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("restrito/rolDeConteudos.xhtml");
    }    
    
    public void redirecionaNotificacoes() throws IOException
    {
    notificacoes=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("notificacoes.xhtml");        
    }
    
    public boolean ehAutorDoConteudo(Conteudo p)
    {
      if(usuario()==null || p==null)
      {
      return false;
      }
    return usuario().equals(p.getAutor());
    }
    
    public boolean ehAutorDoComentario(Comentario c)
    {
      if(usuario()==null || c==null)
      {
      return false;
      }
    return usuario().equals(c.getAutor());
    }

    public String getTipoConteudo() {
        return tipoConteudo;
    }

    public void setTipoConteudo(String tipoConteudo) {
        this.tipoConteudo = tipoConteudo;
    }
    
    public Collection<Conteudo> notificacoes(Usuario u)
    {
      if(notificacoes==null)
      {
      notificacoes=conteudos.notificacoes(u);
      }
    return notificacoes;
    }
    
    public boolean haNotificacoesNaoVistas(Usuario u)
    {
    return conteudos.haNotificacoesNaoVistas(u);
    }
    
    public void todasAsNotificacoesForamVistas(Usuario u)
    {
    conteudos.notificacoesVista(u);
    }
    
    public void anulaTudo()
    {
    anulaPostarConteudo();
    comentario=null;
    conteudoParaComentario=null;
    fotoComentario=null;
    conteudoParaEditar=null;
    conteudoParaExcluir=null;
    comentarioParaEditar=null;
    comentarioParaExcluir=null;
    usuariosMarcados=null;
    roldeConteudos=null;
    perfil=null;
    perfilParaVisistar=null;
    idBotaoComentario=null;
    conteudoParaVerAssuntos=null;
    }
}