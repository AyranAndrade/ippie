package br.com.ippie.negocio;

import java.util.Collection;

/**
 *
 * @author ayran
 */
public interface RepositorioDeUsuarios 
{
    public Collection<Usuario> busca(String nome);
    public void adiciona(Usuario u);
    public void exclui(Usuario u);
    public void editaSocial(Usuario u);
    public void desbloqueia(Usuario u, Assunto t);
    public void editaAssuntosFavoritos(Usuario u);
    public void editaDescricao(Usuario u);
    public void editaFotoCabecalho(Usuario u);
    public void editaFotoPerfil(Usuario u);
    public void bloqueia(Usuario bloqueador, Usuario bloqueado);
    public void bloqueia(Usuario u, Conteudo p);
    public void bloqueia(Usuario u, Assunto t);
    public void desbloqueia(Usuario bloqueador, Usuario bloqueado);
    public void desbloqueia(Usuario u, Conteudo p);

    /**
     * Exclui o assunto da lista de assuntos favoritos deste usuário.
     * @param usuario O usuário que sofreá a subtração de assunto.
     * @param t O assunto que será subtraído.
     */
    public void excluiEsteAssuntoDosFavoritos(Usuario usuario, Assunto t);

    /**
     * Adiciona o assunto a lista de favoritos do usuário.
     * @param usuario O usuário que ganhará um assunto.
     * @param t O assunto que será adicionado.
     */
    public void adicionaEsteAssuntoAosFavoritos(Usuario usuario, Assunto t);

    public long quantosUsuariosTem();
}