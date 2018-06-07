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
    public Orden orden;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    public Producto producto;
    
    public DetalleOrden() {
    }

    public DetalleOrden(DetalleOrdenPK detalleOrdenPK) {
        this.detalleOrdenPK = detalleOrdenPK;
    }

    public DetalleOrden(DetalleOrdenPK detalleOrdenPK, BigDecimal cantidad) {
        this.detalleOrdenPK = detalleOrdenPK;
        this.cantidad = cantidad;
    }

    public DetalleOrden(int idOrden, int idProducto) {
        this.detalleOrdenPK = new DetalleOrdenPK(idOrden, idProducto);
    }

    public DetalleOrdenPK getDetalleOrdenPK() {
        return detalleOrdenPK;
    }

    public void setDetalleOrdenPK(DetalleOrdenPK detalleOrdenPK) {
        this.detalleOrdenPK = detalleOrdenPK;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleOrdenPK != null ? detalleOrdenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleOrden)) {
            return false;
        }
        DetalleOrden other = (DetalleOrden) object;
        if ((this.detalleOrdenPK == null && other.detalleOrdenPK != null) || (this.detalleOrdenPK != null && !this.detalleOrdenPK.equals(other.detalleOrdenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.edu.diseno.definiciones.DetalleOrden[ detalleOrdenPK=" + detalleOrdenPK + " ]";
    }
    
}
