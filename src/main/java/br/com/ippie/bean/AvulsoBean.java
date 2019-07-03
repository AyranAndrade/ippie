package br.com.ippie.bean;

import br.com.ippie.negocio.Candidatura;
import br.com.ippie.negocio.RepositorioDeCoisasAvulsas;
import br.com.ippie.negocio.Sugestao;
import java.time.LocalDateTime;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class AvulsoBean 
{
private String emailSugestor;
private String sugestao;
private String nomeCandidato;
private String emailCandidato;
private String motivacaoCandidato;
private final RepositorioDeCoisasAvulsas avulso;
private final Validator validator;

    @Autowired
    public AvulsoBean(RepositorioDeCoisasAvulsas avulso) 
    {
    this.avulso=avulso;
    validator=Validation.buildDefaultValidatorFactory().getValidator();
    }

    public String getEmailSugestor() {
        return emailSugestor;
    }

    public void setEmailSugestor(String emailSugestor) {
        this.emailSugestor = emailSugestor;
    }

    public String getSugestao() {
        return sugestao;
    }

    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public String getEmailCandidato() {
        return emailCandidato;
    }

    public void setEmailCandidato(String emailCandidato) {
        this.emailCandidato = emailCandidato;
    }

    public String getMotivacaoCandidato() {
        return motivacaoCandidato;
    }

    public void setMotivacaoCandidato(String motivacaoCandidato) {
        this.motivacaoCandidato = motivacaoCandidato;
    }
    
    @Transactional
    public void adicionaSugestao()
    {
    Sugestao s=new Sugestao();
    s.setDataSugestao(LocalDateTime.now());
    s.setEmailSugestor(emailSugestor);
    s.setTexto(sugestao);
    Set<ConstraintViolation<Sugestao>> v=validator.validate(s);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else
      {
      avulso.adicionaSugestao(s);
      emailSugestor=null;
      sugestao=null;
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Obrigado pela sua sugestão!"));
      }
    }
    
    @Transactional
    public void adicionaCandidatura()
    {
    Candidatura c=new Candidatura();
    c.setDataCandidatura(LocalDateTime.now());
    c.setEmailCandidato(emailCandidato);
    c.setMotivacaoCandidatura(motivacaoCandidato);
    c.setNomeCandidato(nomeCandidato);
    Set<ConstraintViolation<Candidatura>> v=validator.validate(c);
      if(!v.isEmpty())
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findFirst().get()
                    .getMessage()));
      }
      else
      {
      avulso.adicionaCandidatura(c);
      emailCandidato=null;
      motivacaoCandidato=null;
      nomeCandidato=null;
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Obrigado pela sua candidatura! "
                    + "Entraremos em contato em breve!"));
      }    
    }
}