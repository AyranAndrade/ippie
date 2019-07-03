package br.com.ippie.negocio;

/**
 *
 * @author Ayran
 */
public interface RepositorioDeAcessos 
{

    /**
     * Marca o horário de login do usuário no sistema.
     * @param a Um objeto que, necessariamente, contém o usuário e o horário 
     * de entrada no sistema.
     */
    public void adiciona(Acesso a);
    
    /**
     * Marca o horário de logout do usuário no sistema.
     * @param a Um objeto que, necessariamente, contém o usuário e o horário
     * em que este saiu do sistema. Não precisa ser o mesmo objeto usado no 
     * método adiciona.
     */
    public void finaliza(Acesso a);
}