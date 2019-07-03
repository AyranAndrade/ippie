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
@Table(name="candidatura")
public class Candidatura implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_candidatura")
private int codigo;

@Column(name="nm_candidato")
@Size(max=60,message="O nome do candidato não pode ultrapassar 60 caracteres.")
@NotNull(message="O nome do candidato não pode ser nulo.")
private String nomeCandidato;

@Column(name="nm_email")
@Size(min=3,max=254,message="O email não pode ter menos de 3 caracteres nem mais de 254.")
@NotNull(message="O email não pode ser nulo.")
private String emailCandidato;

@Column(name="nm_motivacao")
@NotNull(message="A motivação não pode ser nula.")
@Size(min=15,max=500,message="A motivação precisa ter, pelo menos, 15 caracteres e, no máximo, 500.")
private String motivacaoCandidatura;

@Column(name="dt_candidatura")
@NotNull(message="A data da candidatura não pode ser nula.")
private LocalDateTime dataCandidatura;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public String getEmailCandidato() {
        return emailCandidato;
    }

    public void setEmailCandidato(String emailCandidato) {
        this.emailCandidato = emailCandidato;
    }

    public String getMotivacaoCandidatura() {
        return motivacaoCandidatura;
    }

    public void setMotivacaoCandidatura(String motivacaoCandidatura) {
        this.motivacaoCandidatura = motivacaoCandidatura;
    }

    public LocalDateTime getDataCandidatura() {
        return dataCandidatura;
    }

    public void setDataCandidatura(LocalDateTime dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }
}