/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.definiciones;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jcpleitez
 */
@Entity
@Table(name = "Parametro", catalog = "resbar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p")
    , @NamedQuery(name = "Parametro.findByIdParametro", query = "SELECT p FROM Parametro p WHERE p.idParametro = :idParametro")
    , @NamedQuery(name = "Parametro.findByNombre", query = "SELECT p FROM Parametro p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Parametro.findByValor", query = "SELECT p FROM Parametro p WHERE p.valor = :valor")})
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idParametro")
    public Integer idParametro;
    @Basic(optional = false)
    @Column(name = "nombre")
    public String nombre;
    @Basic(optional = false)
    @Column(name = "valor")
    public String valor;
    
}
