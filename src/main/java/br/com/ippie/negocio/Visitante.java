package br.com.ippie.negocio;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="visitante")
public class Visitante implements Serializable 
{
    @Id
    @GeneratedValue
    @Column(name="cd_visitante")
    private long codigo;
    @Column(name="nm_ip_visitante",length = 100, nullable = false)
    @NotNull(message="O IP não pode ser nulo.")
    @Size(max=100,message="O Tamanho máximo do IP é 100 caracteres.")
    private String ip;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name="dt_visita", nullable = false)
    @NotNull(message="A data de visita não pode ser nula.")
    private Calendar dataVisita;
    @Column(name="nm_user_agent",length = 200,nullable = false)
    @NotNull(message="O User Agent não pode ser nulo.")
    private String userAgent;

    public Visitante() {
    }

    public Visitante(String ip, Calendar dataVisita, String userAgent) {
        this.ip = ip;
        this.dataVisita = dataVisita;
        this.userAgent = userAgent;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Calendar getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(Calendar dataVisita) {
        this.dataVisita = dataVisita;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}