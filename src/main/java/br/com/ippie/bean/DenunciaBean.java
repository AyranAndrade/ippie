package br.com.ippie.bean;

import br.com.ippie.negocio.Conteudo;
import br.com.ippie.negocio.RepositorioDeDenuncias;
import br.com.ippie.negocio.RepositorioDeConteudos;
import br.com.ippie.negocio.RepositorioDeAssuntos;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.Usuario;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class DenunciaBean 
{
private Collection<Conteudo> conteudos;
private Collection<Assunto> assuntos;
private final RepositorioDeDenuncias denuncias;
private final RepositorioDeConteudos repConteudos;
private final RepositorioDeAssuntos repAssuntos;
private Conteudo conteudoParaDenunciar;
private List<Assunto> assuntosParaDenunciar;

    @Autowired
    public DenunciaBean(RepositorioDeDenuncias denuncias, 
            RepositorioDeAssuntos repTags, RepositorioDeConteudos repConteudos) 
    {
    this.denuncias=denuncias;
    this.repConteudos=repConteudos;
    this.repAssuntos=repTags;
    }
    
    /**
     * Retorna todos os conteudos denunciados que estao gravados no sistema.
     * @return Os conteudos denunciados.
     */
    public Collection<Conteudo> conteudos()
    {
      if(conteudos==null)
      {
      conteudos=denuncias.conteudosDenunciados();
      }
    return conteudos;
    }
    
    /**
     * Retorna todas as assuntos denunciadas que estao gravadas no sistema.
     * @return
     */
    public Collection<Assunto> assuntos()
    {
      if(assuntos==null)
      {
      assuntos=denuncias.assuntosDenunciados();
      }
    return assuntos;
    }
    
    /**
     * Aprova a denuncia deste conteÃºdo.
     * @param p O conteudo denunciado.
     * @throws java.io.IOException
     */
    @Transactional
    public void aprovaDenuncia(Conteudo p) throws IOException
    {
    denuncias.denunciaVerificada(p);
    conteudos.remove(p);
    repConteudos.exclui(p);
    recarregaDenuncias();
    }
    
    /**
     * Aprova a denuncia deste assunto.
     * @param t A assunto denunciada.
     * @throws java.io.IOException
     */
    @Transactional
    public void aprovaDenuncia(Assunto t) throws IOException
    {
    denuncias.denunciaVerificada(t);
    assuntos.remove(t);
    repAssuntos.exclui(t);
    recarregaDenuncias();
    }
    
    /**
     * Verifica se o usuario ja aprovou ou ja rejeitou a denuncia deste conteudo.
     * @param p O conteudo denunciado.
     * @return Retorna true se o usuario ja aprovou ou rejeitou a denuncia, ou
     * false caso ele ainda nao tenha aprovado nem rejeitado.
     */
    public boolean denunciaAindaDeveSerVerificada(Conteudo p)
    {
    return conteudos.contains(p);
    }
    
    /**
     * Verifica se o usuario ja aprovou ou ja rejeitou a denuncia deste assunto.
     * @param t A assunto denunciada.
     * @return Retorna true se o usuario ja aprovou ou rejeitou a denuncia, ou
     * false caso contrario.
     */
    public boolean denunciaAindaDeveSerVerificada(Assunto t)
    {
    return assuntos.contains(t);
    }
    
    /**
     * Retorna a quantidade total de conteudos denunciados que ainda nao foram 
     * apurados.
     * @return A quantidade total de conteudos.
     */
    public int quantosConteudos()
    {
      if(conteudos==null)
      {
      return 0;
      }
    return conteudos.size();
    }
    
    /**
     * Retorna a quantidade total de assuntos denunciadas que ainda nao foram 
     * apurados.
     * @return A quantidade total de assuntos.
     */
    public int quantosAssuntos()
    {
      if(assuntos==null)
      {
      return 0;
      }
    return assuntos.size();
    }
    
    /**
     * Rejeita a denuncia feita pra este conteudo.
     * @param p O conteudo denunciado.
     * @throws java.io.IOException
     */
    @Transactional
    public void rejeitaDenuncia(Conteudo p) throws IOException
    {
    conteudos.remove(p);
    denuncias.denunciaVerificada(p);
    recarregaDenuncias();
    }
    
    /**
     * Rejeita a denuncia feita pra este assunto.
     * @param t O assunto denunciado.
     * @throws java.io.IOException
     */
    @Transactional
    public void rejeitaDenuncia(Assunto t) throws IOException
    {
    assuntos.remove(t);
    denuncias.denunciaVerificada(t);
    recarregaDenuncias();
    }
    
    /**
     * Denuncia este conteúdo. O autor da denúncia será o usuário que está 
     * atualmente logado no sistema. Este método deve ser usado apenas quando o
     * usuário logado no sistema não for o moderador.
     */
    @Transactional
    public void denunciaConteudo()
    {
    denuncias.denuncia(conteudoParaDenunciar,usuario());
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_INFO,"Ippie","Conteúdo denunciado com "
                      + "sucesso. Avaliaremos a sua denúncia em breve.")); 
    }
    
    /**
     * Denuncia este assunto. O autor desta denuncia sera o usuario atualmente 
     * logado no sistema. Este método deve ser usado apenas quando o
     * usuário logado no sistema não for o moderador.
     */
    @Transactional
    public void denunciaAssuntos()
    {
    assuntosParaDenunciar.stream().forEach((a)->denuncias.denuncia(a,usuario()));
    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_INFO,"Ippie","Assunto(s) denunciado(s)"
                      + " com sucesso. Avaliaremos a sua denúncia em "
                      + "breve."));
    }
    
    private Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }

    public Conteudo getConteudoParaDenunciar() {
        return conteudoParaDenunciar;
    }

    public void setConteudoParaDenunciar(Conteudo conteudoParaDenunciar) {
        this.conteudoParaDenunciar = conteudoParaDenunciar;
    }

    public List<Assunto> getAssuntosParaDenunciar() {
        return assuntosParaDenunciar;
    }

    public void setAssuntosParaDenunciar(List<Assunto> assuntosParaDenunciar) {
        this.assuntosParaDenunciar = assuntosParaDenunciar;
    }
    
    public void recarregaDenuncias() throws IOException
    {
    assuntos=null;
    conteudos=null;
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("denuncia.xhtml"); 
    }
}