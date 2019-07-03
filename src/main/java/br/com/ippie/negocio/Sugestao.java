package br.com.ippie.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="sugestao")
public class Sugestao implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_sugestao")
private int codigo;

@Column(name="nm_sugestao")
@NotNull(message="A sugestão não pode ser nula.")
@Size(min=15,max=500,message="A sugestão precisa ter, no mínimo, 15 caracteres, e, no máximo, 500.")
private String texto;

@Column(name="dt_sugestao")
@NotNull(message="A data de sugestão não pode ser nula!")
private LocalDateTime dataSugestao;

@Column(name="nm_email_sugestao")
private String emailSugestor;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataSugestao() {
        return dataSugestao;
    }

    public void setDataSugestao(LocalDateTime dataSugestao) {
        this.dataSugestao = dataSugestao;
    }

    public String getEmailSugestor() {
        return emailSugestor;
    }

    public void setEmailSugestor(String emailSugestor) {
        this.emailSugestor = emailSugestor;
    }
}