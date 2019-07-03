package br.com.ippie.negocio;

import java.util.Collection;

/**
 *
 * @author Ayran
 */
public interface RepositorioDeDenuncias 
{

    /**
     * Grava o conteudo denunciado e o usuario que o denunciou.
     * @param p O conteudo denunciado.
     * @param u O usuario que foi autor da denuncia.
     */
    public void denuncia(Conteudo p, Usuario u);

    /**
     * Grava o assunto denunciado e o usuario que foi o autor da denuncia.
     * @param t O assunto denunciado.
     * @param u O usuario que foi autor da denuncia.
     */
    public void denuncia(Assunto t, Usuario u);
    
    /**
     * Grava o assunto denunciado a partir do nome e o usuario que foi o autor 
     * da denuncia.
     * @param nomeAssunto
     * @param u
     */
    public void denuncia(String nomeAssunto, Usuario u);

    /**
     * Retorna todos os conteudos denunciados que estao gravados no sistema.
     * @return Os conteudos denunciados.
     */
    public Collection<Conteudo> conteudosDenunciados();

    /**
     * Retorna todas os assuntos denunciados que estao gravados no sistema.
     * @return As assuntos denuncidas.
     */
    public Collection<Assunto> assuntosDenunciados();
    
    /**
     * Apaga a denuncia feita do sistema, uma vez que ela ja foi verificada e as
     * devidas atitude ja foram tomadas.
     * @param p O conteudo cuja as suas denuncias serao apagadas.
     */
    public void denunciaVerificada(Conteudo p);
    
    /**
     * Apaga a denuncia feita do sistema, uma vez que ela ja foi verificada e as
     * devidas atitude ja foram tomadas.
     * @param t A assunto cuja as suas denuncias serao apagadas.
     */
    public void denunciaVerificada(Assunto t);
}