package br.com.ippie.infraestrutura;

/**
 *
 * @author ayran
 */
public interface EnviadorDeEmails 
{
    public EnviadorDeEmails servidor(HostnameEmail host);
    public EnviadorDeEmails destinatario(String dest);
    public EnviadorDeEmails assunto(String assunto);
    public EnviadorDeEmails mensagem(String msg);
    public EnviadorDeEmails seuEmail(String email);
    public EnviadorDeEmails suaSenha(String senha);
    public EnviadorDeEmails debuga(boolean debug);
    public void envia() throws IllegalAccessException;
}