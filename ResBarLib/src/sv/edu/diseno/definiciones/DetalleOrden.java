/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.definiciones;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jcpleitez
 */
@Entity
@Table(name = "DetalleOrden", catalog = "resbar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleOrden.findAll", query = "SELECT d FROM DetalleOrden d")
    , @NamedQuery(name = "DetalleOrden.findByIdOrden", query = "SELECT d FROM DetalleOrden d WHERE d.detalleOrdenPK.idOrden = :idOrden")
    , @NamedQuery(name = "DetalleOrden.findByIdProducto", query = "SELECT d FROM DetalleOrden d WHERE d.detalleOrdenPK.idProducto = :idProducto")
    , @NamedQuery(name = "DetalleOrden.findByCantidad", query = "SELECT d FROM DetalleOrden d WHERE d.cantidad = :cantidad")})
public class DetalleOrden implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleOrdenPK detalleOrdenPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "cantidad")
    public BigDecimal cantidad;
    @JoinColumn(name = "idOrden", referencedColumnName = "idOrden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    protected Orden orden;

    /**
     * La propiedad producto tiene un objeto de la clase producto.
     */
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    public Producto producto;
    
}
