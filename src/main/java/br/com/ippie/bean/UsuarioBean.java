package br.com.ippie.bean;

import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeUsuarios;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Usuario;
import java.util.Collection;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ayran
 */
@Controller
public class UsuarioBean 
{
private String nome;
private final RepositorioDeUsuarios usuarios;
private Usuario usuarioParaBloquear;
private Conteudo conteudoParaBloquear;
private List<Assunto> assuntosParaBloquear;
private Collection<Usuario> pesquisa;

    @Autowired
    public UsuarioBean(RepositorioDeUsuarios usuarios) 
    {
    this.usuarios=usuarios;
    }

    public Collection<Usuario> getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Collection<Usuario> pesquisa) {
        this.pesquisa = pesquisa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }
    
    public void pesquisa()
    {
    pesquisa=usuarios.busca(nome);
    }
    
    @Transactional
    public void bloqueiaConteudo()
    {
    usuarios.bloqueia(usuario(),conteudoParaBloquear);
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Você bloqueou este conteúdo com "
                    + "sucesso! Recarregue a página para o conteúdo sumir!")); 
    }
    
    @Transactional
    public void bloqueiaAssuntos()
    {
    assuntosParaBloquear.stream().forEach((a)->usuarios.bloqueia(usuario(),a));
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Você bloqueou o(s) assunto(s)"
                    + " com sucesso! Recarregue a página para que este(s) suma(m)!")); 
    }
    
    @Transactional
    public void bloqueiaUsuario()
    {
    usuarios.bloqueia(usuario(),usuarioParaBloquear);
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Você bloqueou "
                    +usuarioParaBloquear.getNomeCompleto()+". Recarregue a "
                    + "página para os conteúdos deste usuário sumirem!")); 
    }
    
    @Transactional
    public void desbloqueia(Conteudo p)
    {
    usuarios.desbloqueia(usuario(),p);
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Conteúdo desbloqueado.")); 
    }
    
    @Transactional
    public void desbloqueia(Assunto t)
    {
    usuarios.desbloqueia(usuario(),t);
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Assunto "+t.getNome()+" desbloqueado.")); 
    }
    
    @Transactional
    public void desbloqueia(Usuario u)
    {
    usuarios.desbloqueia(usuario(),u);
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Usuário "+u.getNomeCompleto()+" desbloqueado.")); 
    }
    
    public Usuario getUsuarioParaBloquear() {
        return usuarioParaBloquear;
    }

    public void setUsuarioParaBloquear(Usuario usuarioParaBloquear) {
        this.usuarioParaBloquear = usuarioParaBloquear;
    }

    public Conteudo getConteudoParaBloquear() {
        return conteudoParaBloquear;
    }

    public void setConteudoParaBloquear(Conteudo conteudoParaBloquear) {
        this.conteudoParaBloquear = conteudoParaBloquear;
    }
    
    public List<Assunto> getAssuntosParaBloquear() {
        return assuntosParaBloquear;
    }

    public void setAssuntosParaBloquear(List<Assunto> assuntosParaBloquear) {
        this.assuntosParaBloquear = assuntosParaBloquear;
    }
    
    public void limpaBuscarUsuario(CloseEvent event)
    {
    pesquisa=null;
    nome=null;
    RequestContext.getCurrentInstance().reset("frmBuscarUsuario:txtBuscarUsuario");
    RequestContext.getCurrentInstance().update("frmBuscarUsuario");    
    RequestContext.getCurrentInstance().update("dtlBuscarUsuario");
    }
}