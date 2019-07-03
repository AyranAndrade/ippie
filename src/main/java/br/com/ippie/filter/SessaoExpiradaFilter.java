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
import javax.servlet.http.HttpSession;

/**
 *
 * @author ayran
 */
@WebFilter(urlPatterns={"/faces/restrito/*","/restrito/*"})
public class SessaoExpiradaFilter implements Filter 
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException 
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException
    {
    HttpSession session=((HttpServletRequest)request).getSession(false);
      if(session!=null && !session.isNew())
      {
      chain.doFilter(request, response);
      }
      else 
      {
      String caminhoBase=((HttpServletRequest)request).getContextPath();
      ((HttpServletResponse)response).sendRedirect(caminhoBase);
      }
    }

    @Override
    public void destroy() 
    {
    }
}