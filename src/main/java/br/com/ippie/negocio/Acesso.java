package br.com.ippie.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ayran
 */
@Entity
@Table(name="acesso")
public class Acesso implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_acesso")
private long codigo;

@Column(name="dt_entrada",nullable=false)
@NotNull(message="A data de entrada do usuário no sistema não pode ser nulo!")
private LocalDateTime dataEntrada;

@Column(name="dt_saida")
private LocalDateTime dataSaida;

@ManyToOne(optional=false)
@JoinColumn(name="cd_usuario")
@NotNull(message="O autor do acesso não pode ser nulo!")
private Usuario usuario;

    public Acesso(LocalDateTime dataEntrada, Usuario usuario) {
        this.dataEntrada = dataEntrada;
        this.usuario = usuario;
    }

    public Acesso(LocalDateTime dataEntrada, LocalDateTime dataSaida, Usuario usuario) {
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.usuario = usuario;
    }

    public Acesso() {
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.dataEntrada);
        hash = 37 * hash + Objects.hashCode(this.usuario);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Acesso other = (Acesso) obj;
        if (!Objects.equals(this.dataEntrada, other.dataEntrada)) {
            return false;
        }
    return Objects.equals(this.usuario, other.usuario);
    }
}