package br.com.ippie.filter;

import br.com.ippie.negocio.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ayran
 */
@WebFilter(urlPatterns={"/faces/restrito/*","/restrito/*"})
public class LoginFilter implements Filter 
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException 
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException
    {
    Usuario u;
      try
      {
      u=(Usuario)((HttpServletRequest)request).getSession(false)
            .getAttribute("usuario");
      }
      catch(NullPointerException ex)
      {
      u=null;
      }
      
    String caminhoBase=((HttpServletRequest)request).getContextPath();
      if(u==null) 
      {
      ((HttpServletResponse)response).sendRedirect(caminhoBase);
      }
      else 
      {
      chain.doFilter(request, response);
      }
    }

    @Override
    public void destroy() 
    {
    }
}