package br.com.ippie.bean;

import br.com.ippie.negocio.Configuracao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class ConfiguracaoBean 
{
private final Configuracao config;

    @Autowired
    public ConfiguracaoBean(Configuracao config) 
    {
    this.config = config;
    }
    
    public String fotoCabecalho()
    {
    return config.fotoCabecalhoRelativo();
    }
    
    public String fotoPerfil()
    {
    return config.fotoPerfilRelativo();
    }
    
    public String fotoOuVideoConteudo()
    {
    return config.fotoOuVideoConteudoRelativo();
    }
    
    public String fotoComentario()
    {
    return config.fotoComentarioRelativo();
    }
    
    public String fotoAssunto()
    {
    return config.fotoAssuntoRelativo();
    }
    
    public String fotoAssuntoCadastro()
    {
    return config.fotoAssuntoRelativoCadastro();
    }
}