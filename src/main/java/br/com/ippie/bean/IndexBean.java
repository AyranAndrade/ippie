package br.com.ippie.bean;

import br.com.ippie.infraestrutura.AnalisadorDeVisitante;
import br.com.ippie.negocio.RepositorioDeVisitante;
import br.com.ippie.negocio.Visitante;
import java.io.IOException;
import java.util.Calendar;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class IndexBean 
{
private final RepositorioDeVisitante dao;
private final AnalisadorDeVisitante analisador;
 
    @Autowired
    public IndexBean(RepositorioDeVisitante dao,AnalisadorDeVisitante analisador)
    {
    this.dao = dao;
    this.analisador=analisador;
    }
    
    @Transactional
    public void redireciona() throws IOException
    {
    String userAgent=FacesContext.getCurrentInstance().getExternalContext()
            .getRequestHeaderMap().get("User-Agent");
    
      if(analisador.isMobile(userAgent))
      {
      FacesContext.getCurrentInstance().getExternalContext()
                .redirect("mobile.xhtml");
      }
      else
      {
      FacesContext.getCurrentInstance().getExternalContext()
                .redirect("login.xhtml");
      }
    }
    
    @Transactional
    public void registraVisitante()
    {
    String userAgent=FacesContext.getCurrentInstance().getExternalContext()
            .getRequestHeaderMap().get("User-Agent");
    
    String ip=analisador.getClientIpAddress((HttpServletRequest)FacesContext
            .getCurrentInstance().getExternalContext().getRequest());
    
    String userAgent200;
    
      if(userAgent.length()>200)
      {
      userAgent200=userAgent.substring(0,200);
      }
      else
      {
      userAgent200=userAgent;
      }
        System.out.println("Olá visitante "+ip);
    Visitante v=new Visitante(ip,Calendar.getInstance(),userAgent200);
    dao.adiciona(v);    
    }
}