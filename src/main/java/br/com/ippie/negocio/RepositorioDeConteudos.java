package br.com.ippie.negocio;

import java.util.Collection;

/**
 *
 * @author Ayran
 */
public interface RepositorioDeConteudos 
{
    public Collection<Conteudo> rolDeConteudos(Usuario u);
    public Collection<Conteudo> perfil(Usuario u);
    public Collection<Conteudo> apenasConteudosComFotos(Usuario u);
    public Collection<Conteudo> notificacoes(Usuario u);
    
    /**
     * Marca um conteúdo como uma notificação. Uma notificação é um conteúdo 
     * cujo usuário interagiu, seja aprovando, reprovando, etc, ou comentando.
     * @param u O usuário que rececebrá a notificação.
     * @param c O conteúdo que será adicionado como notificação para aquele 
     * usuário.
     */
    public void notifica(Usuario u, Conteudo c);

    /**
     * Marca todas as notificações de um usuário como sendo vistas.
     * @param u O usuário que viu que acessou a página de notificações.
     */
    public void notificacoesVista(Usuario u);

    /**
     * Informa se há notificações cujo o usuário ainda não viu.
     * @param u O usuário na qual se deseja saber se há notificações ou não.
     * @return Retorna false, caso não haja notificações não vistas, e retorna 
     * true, caso haja notificações não vistas.
     */
    public boolean haNotificacoesNaoVistas(Usuario u);
    public boolean esteConteudoEhNotificacao(Conteudo c, Usuario u);
    
    public Collection<Comentario> comentarios(Conteudo p);
    public void comenta(Conteudo p, Comentario c);
    
    public void adiciona(Conteudo p);
    
    public void edita(Comentario c);
    public void edita(Conteudo p);
    
    public void aprova(Conteudo p, Aprovacao a);
    public void aprova(Comentario c, Aprovacao a);
    
    public void reprova(Conteudo p, Reprovacao r);
    public void reprova(Comentario c, Reprovacao r);
    
    public void pesames(Conteudo p, Pesame pe);
    public void concorda(Conteudo p, Concordar c);
    public void discorda(Conteudo p, Discordar d);
    
    public void exclui(Conteudo p);
    public void exclui(Conteudo p, Aprovacao a);
    public void exclui(Conteudo p, Reprovacao r);
    public void exclui(Comentario c, Aprovacao a);
    public void exclui(Comentario c, Reprovacao r);
    public void exclui(Comentario c);
    public void exclui(Conteudo p, Pesame pe);
    public void exclui(Conteudo p, Concordar c);
    public void exclui(Conteudo p, Discordar d);
    
    public boolean estaAprovado(Conteudo p, Aprovacao a);
    public boolean estaReprovado(Conteudo p, Reprovacao r);
    public boolean estaConcordado(Conteudo p, Concordar c);
    public boolean estaDiscordado(Conteudo p, Discordar d);
    public boolean estaComPesame(Conteudo p, Pesame pe);
    
    public int quantasAprovacoes(Conteudo p);
    public int quantasReprovacoes(Conteudo p);
    public int quantosPesames(Conteudo p);
    public int quantosConcordar(Conteudo p);
    public int quantosDiscordar(Conteudo p);
    public int quantosComentarios(Conteudo p);
}