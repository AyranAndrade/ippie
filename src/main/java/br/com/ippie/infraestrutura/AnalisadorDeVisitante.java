package br.com.ippie.infraestrutura;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ayran
 */
@Component
public class AnalisadorDeVisitante 
{  
    
    public boolean isMobile(String userAgent)
    {
    Pattern pattern=Pattern.compile("(iPhone|iPad|iPod|Android|BlackBerry|Opera Mobi|Opera Mini|IEMobile)");  
    return pattern.matcher(userAgent).find();  
    }
    
    public String getClientIpAddress(HttpServletRequest request) 
    {
    String forwardedFor = request.getHeader("X-Forwarded-For");

    if (forwardedFor!=null && !forwardedFor.trim().equals("")) {
        return forwardedFor.split("\\s*,\\s*", 2)[0]; // It's a comma separated string: client,proxy1,proxy2,...
    }

    return request.getRemoteAddr();    
    }
}