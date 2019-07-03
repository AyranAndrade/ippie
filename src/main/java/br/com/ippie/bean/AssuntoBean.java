package br.com.ippie.bean;

import br.com.ippie.negocio.Configuracao;
import br.com.ippie.infraestrutura.GerenciadorDeImagens;
import br.com.ippie.negocio.RepositorioDeAssuntos;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeUsuarios;
import br.com.ippie.negocio.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class AssuntoBean 
{
private InputStream imagem;
private String nome;
private final RepositorioDeAssuntos assuntos;
private final RepositorioDeUsuarios usuarios;
private Collection<Assunto> pesquisa;
private Collection<Assunto> assuntosCriados;
private Collection<Assunto> assuntosParaCadastro;
private final GerenciadorDeImagens imagens;
private final Configuracao config;
private final Validator validator;
private Collection<Conteudo> conteudos;
private Assunto assunto;
private Assunto paraEditar;

    @Autowired
    public AssuntoBean(RepositorioDeAssuntos tags, GerenciadorDeImagens imagens, 
            Configuracao config, RepositorioDeUsuarios usuarios)
    {
    this.assuntos=tags;
    this.usuarios=usuarios;
    this.imagens=imagens;
    this.config=config;
    validator=Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    public void inicia()
    {
    pesquisa=assuntos.listaAssuntosMaisUsadosPorUsuarios(12);    
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Assunto> getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Collection<Assunto> pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Collection<Assunto> getAssuntosParaCadastro() {
        return assuntosParaCadastro;
    }

    public void setAssuntosParaCadastro(Collection<Assunto> assuntosParaCadastro) {
        this.assuntosParaCadastro = assuntosParaCadastro;
    }
    
    @Transactional
    public void criaAssunto()
    {
    Assunto t=new Assunto();
    t.setAutor(usuario());
    t.setNome(nome);
    t.setDataCriacao(LocalDateTime.now());
      if(imagem!=null)
      {
        try 
        {
        String url=imagens.grava(imagem,config.alturaFotoAssunto(),
                config.fotoAssuntoAbsoluto());
        t.setUrlImagem(url);
        } 
        catch (IOException ex) 
        {
        Logger.getLogger(AssuntoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      else
      {
      t.setUrlImagem(config.fotoAssuntoDefault());
      }
    Set<ConstraintViolation<Assunto>> v=validator.validate(t);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else
      {
        if(!assuntos.existeEsteAssunto(nome))
        {
        assuntos.adiciona(t);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Assunto adicionado com sucesso!"));
        limpaCriarAssunto();
        }
        else
        {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro","Este assunto já existe."));    
        }
      }      
    }
    
    public void limpaCriarAssunto()
    {
    imagem=null;
    nome=null;    
    }
    
    public void pesquisaAssuntosPeloNome()
    {
      if(nome.trim().equals(""))
      {
      pesquisa=assuntos.listaAssuntosMaisUsadosPorUsuarios(12);    
      }
      else
      {
      pesquisa=assuntos.listaAssuntos(nome);
      }
    nome=null;
    }
    
    @Transactional
    public void onTagDrop(DragDropEvent e)
    {
    Assunto t=(Assunto)e.getData();
      if(!usuario().getAssuntosFavoritos().contains(t))
      {
      usuario().getAssuntosFavoritos().add(t);
      usuarios.adicionaEsteAssuntoAosFavoritos(usuario(),t);
      pesquisa.remove(t);    
      }
      else 
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro","O assunto '"+t.getNome()+"' já"
                    + " está na sua lista de favoritos."));    
      }
    }
    
    @Transactional
    public void onTagExclude(Assunto t)
    {
      if(usuario().getAssuntosFavoritos().size()-1>=config.minimoDeAssuntosFavoritos())
      {
      usuario().getAssuntosFavoritos().remove(t);
      usuarios.excluiEsteAssuntoDosFavoritos(usuario(),t);
        if(!pesquisa.contains(t))
        {
        pesquisa.add(t);    
        }
      }
      else
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro","Você precisa ter, pelo menos, "
                    +config.minimoDeAssuntosFavoritos()+" assuntos favoritos."));    
      }
    }
    
    public void onAssuntoDropCadastro(DragDropEvent e)
    {
    Assunto t=(Assunto)e.getData();
      if(assuntosParaCadastro==null)
      {
      assuntosParaCadastro=new ArrayList<>();
      }
      if(!assuntosParaCadastro.contains(t))
      {
      assuntosParaCadastro.add(t);
      pesquisa.remove(t);    
      }
      else 
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro","O assunto '"+t.getNome()+"' já"
                    + " está na sua lista de favoritos."));    
      }    
    }
    
    public void onAssuntoExcludeCadastro(Assunto t)
    {
      if(assuntosParaCadastro==null)
      {
      assuntosParaCadastro=new ArrayList<>();
      }        
    assuntosParaCadastro.remove(t);
      if(!pesquisa.contains(t))
      {
      pesquisa.add(t);    
      }
    }
    
    @Transactional
    public void editaAssunto()
    {
      if(imagem!=null)
      {
        try 
        {
        String url=imagens.grava(imagem,config.alturaFotoAssunto(),
                config.fotoAssuntoAbsoluto());
        paraEditar.setUrlImagem(url);
        } 
        catch (IOException ex) 
        {
        Logger.getLogger(AssuntoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      else
      {
      paraEditar.setUrlImagem(config.fotoAssuntoDefault());
      }
    Set<ConstraintViolation<Assunto>> v=validator.validate(paraEditar);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else
      {
        if(!assuntos.existeEsteAssunto(paraEditar.getNome()))
        {
        assuntos.edita(paraEditar);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Assunto editado com sucesso!"));
        limpaEditaAssunto();
        }
        else
        {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro","Este assunto já existe."));    
        }
      }       
    }
    
    public void limpaEditaAssunto()
    {
    imagem=null;
        paraEditar=null;    
    }
    
    public Collection<Assunto> pesquisaAssuntosPassandoONome(String nome)
    {
    return assuntos.listaAssuntos(nome);
    }
    
    private Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }

    public Assunto getParaEditar() {
        return paraEditar;
    }

    public void setParaEditar(Assunto paraEditar) {
        this.paraEditar = paraEditar;
    }
    
    public Collection<Conteudo> conteudosComEsteAssunto()
    {
      if(conteudos==null)
      {
      conteudos=assuntos.conteudosComEsteAssunto(assunto);
      }
    return conteudos;
    }

    public Assunto getAssunto() {
        return assunto;
    }

    public void setAssunto(Assunto assunto) throws IOException {
        this.assunto = assunto;
        redirecionaConteudosComEsteAssunto();
    }
    
    public void redirecionaConteudosComEsteAssunto() throws IOException
    {
    conteudos=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("conteudos_com_este_assunto.xhtml");
    }
    
    public void fotoAssunto(FileUploadEvent event)
    {
      try 
      {
      imagem=event.getFile().getInputstream();
      }
      catch (IOException ex) 
      {
      Logger.getLogger(ConteudoBean.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public boolean upouFotoAssunto()
    {
    return imagem!=null;
    }
    
    public boolean assuntoParaEditarPossuiImagem()
    {
      if(paraEditar==null)
      {
      return false;
      }
    return paraEditar.getUrlImagem().equals(config.fotoAssuntoDefault());
    }
    
    public void excluiImagemAssunto()
    {
      if(paraEditar!=null)
      {
      paraEditar.setUrlImagem(config.fotoAssuntoDefault());
      }
    }
    
    public void iniciaAssuntosCriados(Usuario u)
    {
    assuntosCriados=assuntos.listaAssuntosCriadosPor(u);
    }

    public Collection<Assunto> getAssuntosCriados() {
        return assuntosCriados;
    }

    public void setAssuntosCriados(Collection<Assunto> assuntosCriados) {
        this.assuntosCriados = assuntosCriados;
    }
    
    public void limpaCadastroParte3()
    {
    assuntosParaCadastro=null;
    nome=null;
    RequestContext.getCurrentInstance().update("tptCadastroParte3AssuntosSelecionados");
    }
}