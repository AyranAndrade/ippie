package br.com.ippie.negocio;

import java.util.Collection;

/**
 *
 * @author Ayran
 */
public interface RepositorioDeAssuntos 
{

    /**
     * Adiciona um assunto no sistema, caso o nome dele não esteja ainda em uso.
     * @param t O assunto que se deseja adicionar no sistema.
     */
    public void adiciona(Assunto t);

    /**
     * Edita o nome do assunto e a sua imagem disponível. Não edita o autor do 
     * assunto nem a sua data de criação. Não irá realizar a edição caso o novo 
     * nome do assunto já esteja em uso por um outro assunto.
     * @param t O assunto que se deseja editar. É importante o objeto conter o 
     * atributo codigo, que é a sua identificação única, preenchido corretamente.
     */
    public void edita(Assunto t);

    /**
     * Lista uma coleção contendo assuntos registrados no sistema selecionados 
     * aleatoriamente.
     * @param max O número de assuntos aleatórios, ou, em outras palavras, o 
     * tamanho da Collection retornada neste método.
     * @return Uma coleção contendo asusntos aleatórios.
     */
    public Collection<Assunto> listaAssuntosAleatoriamente(int max);

    /**
     * Lista os assuntos favoritos do usuário.
     * @param u O usuário na qual se deseja obter os assuntos favoritos.
     * @return Uma coleção com os assuntos favoritos daqueel usuário.
     */
    public Collection<Assunto> listaAssuntos(Usuario u);

    /**
     * Exclui o assunto do sistema.
     * @param t O assunto que se deseja excluir do sistema. É importante que o 
     * atributo código esteja preenchido corretamente.
     */
    public void exclui(Assunto t);

    /**
     * Pesquisa assuntos que comecem ou coincidam com o nome fornecido.
     * @param nome Nome ou parte do nome do assunto que se deseja pesquisar.
     * @return Uma coleção de assuntos cujo nome comece com o fornecido.
     */
    public Collection<Assunto> listaAssuntos(String nome);

    /**
     * Retorna uma coleção com os assuntos mais usados pelos usuário do sistema.
     * (O nome é óbvio, né?)
     * @param max Define o tamanho da coleção retornada.
     * @return Uma coleção com os assuntos mais usados pelos usuários.
     */
    public Collection<Assunto> listaAssuntosMaisUsadosPorUsuarios(int max);

    /**
     *
     * @param max
     * @return
     */
    public Collection<Assunto> listaAssuntosMaisUsadosPorConteudos(int max);
    
    public Collection<Conteudo> conteudosComEsteAssunto(Assunto a);

    public Collection<Assunto> listaAssuntosCriadosPor(Usuario u);
    
    public boolean existeEsteAssunto(String nome);
}