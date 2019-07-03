package br.com.ippie.negocio;

/**
 *
 * @author Ayran
 */
public interface RepositorioDeLogins 
{

    /**
     * Retorna o objeto Login contendo o email (já informado no parâmetro) e a
     * senha criptografada do usuário. Caso o email não exista, retorna null.
     * @param email O email do usuário
     * @return O Login do usuário dono do email passado como parãmetro, ou null,
     * caso o email não exista.
     */
    public Login existeUsuario(String email);

    /**
     * Dada um login contendo um email e uma senha, retorna um objeto Usuario 
     * que seja dono daquele login completamente preenchido, ou null, caso o
     * login não exista.
     * @param l
     * @return O usuário dono daquele login ou null caso o login não exista.
     */
    public Usuario existeUsuario(Login l);

    /**
     * Troca senha atual do usuário cujo o email está armazenado no Login 
     * passado como parâmetro pela senha armazenada neste mesmo Login.
     * @param l O login contendo o email atual do usuário e a senha que 
     * substituirá a senha atual.
     */
    public void editaSenha(Login l);
}