/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.definiciones;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jcpleitez
 */
@Entity
@Table(name = "Producto", catalog = "resbar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")
    , @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.idProducto = :idProducto")
    , @NamedQuery(name = "Producto.findAllByIdProducto", query = "SELECT p.idProducto FROM Producto p ORDER BY p.idProducto DESC")
    , @NamedQuery(name = "Producto.findByIdCategoria", query = "SELECT p FROM Producto p WHERE p.idCategoria.idCategoria = :idCategoria")
    , @NamedQuery(name = "Producto.findByNombreLike", query = "SELECT DISTINCT p FROM Producto p WHERE (UPPER(p.nombre) LIKE UPPER(:nombre)) OR (p.idProducto LIKE :idCategoria)")    
    , @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio")
    , @NamedQuery(name = "Producto.findByArea", query = "SELECT p FROM Producto p WHERE p.area = :area")})

public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProducto")
    public Integer idProducto;
    @Basic(optional = false)
    @Column(name = "nombre")
    public String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precio")
    public BigDecimal precio;
    @Basic(optional = false)
    @Column(name = "area")
    public Character area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    public List<DetalleOrden> detalleOrdenList;
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    public Categoria idCategoria;
    
}
