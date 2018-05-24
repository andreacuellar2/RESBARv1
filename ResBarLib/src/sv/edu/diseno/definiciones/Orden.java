/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.definiciones;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jcpleitez
 */
@Entity
@Table(name = "Orden", catalog = "resbar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orden.findAll", query = "SELECT o FROM Orden o")
    , @NamedQuery(name = "Orden.findAllActivas", query = "SELECT o FROM Orden o WHERE o.activa = TRUE")
    , @NamedQuery(name = "Orden.findAllActivasText", query = "SELECT o FROM Orden o WHERE (o.activa = TRUE) AND (o.cliente LIKE :cliente OR o.mesero LIKE :mesero OR o.mesa LIKE :mesa OR o.comentario LIKE :comentario)")
    , @NamedQuery(name = "Orden.findAllActivasByIdOrden", query = "SELECT o.idOrden FROM Orden o ORDER BY o.idOrden DESC")    
    , @NamedQuery(name = "Orden.findByFechaBetWeen", query = "SELECT o FROM Orden o WHERE o.fecha BETWEEN :fecha1 AND :fecha2")
    , @NamedQuery(name = "Orden.findByIdOrden", query = "SELECT o FROM Orden o WHERE o.idOrden = :idOrden")
    , @NamedQuery(name = "Orden.findByMesero", query = "SELECT o FROM Orden o WHERE o.mesero = :mesero")
    , @NamedQuery(name = "Orden.findByMesa", query = "SELECT o FROM Orden o WHERE o.mesa = :mesa")
    , @NamedQuery(name = "Orden.findByCliente", query = "SELECT o FROM Orden o WHERE o.cliente = :cliente")
    , @NamedQuery(name = "Orden.findByFecha", query = "SELECT o FROM Orden o WHERE o.fecha = :fecha")
    , @NamedQuery(name = "Orden.findByComentario", query = "SELECT o FROM Orden o WHERE o.comentario = :comentario")
    , @NamedQuery(name = "Orden.findByTotal", query = "SELECT o FROM Orden o WHERE o.total = :total")
    , @NamedQuery(name = "Orden.findByActiva", query = "SELECT o FROM Orden o WHERE o.activa = :activa")})
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idOrden")
    public Integer idOrden;
    @Basic(optional = false)
    @Column(name = "mesero")
    public String mesero;
    @Basic(optional = false)
    @Column(name = "mesa")
    public String mesa;
    @Basic(optional = false)
    @Column(name = "cliente")
    public String cliente;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    public Date fecha;
    @Column(name = "comentario")
    public String comentario;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total")
    public BigDecimal total;
    @Basic(optional = false)
    @Column(name = "activa")
    public boolean activa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orden")
    public List<DetalleOrden> detalleOrdenList;
   
    /**
     *Calcula el total de consumo de la orden.
     */
    public void CalcularTotal(){
      BigDecimal totalCalculado = null;
        for (DetalleOrden detalleOrden : detalleOrdenList) {
            totalCalculado = totalCalculado.add(detalleOrden.cantidad.multiply(detalleOrden.producto.precio));
        }
      this.total = totalCalculado;
    }
    
    /**
     * Permite agregar más productos a la orden
     * @param producto el producto a agregar a la orden.
     * @param cant la cantidad de producto a agregar.
     */
    public void AgregarProducto(Producto producto, BigDecimal cant){
        for (DetalleOrden detalleOrden : detalleOrdenList) {
            if (producto == detalleOrden.producto) {
               detalleOrden.cantidad = detalleOrden.cantidad.add(cant);
            }else{
                DetalleOrden nuevo = new DetalleOrden();
                nuevo.producto = producto;
                nuevo.cantidad = cant;
                detalleOrdenList.add(nuevo);
            }           
        }
        CalcularTotal();
    }
    
    /**
     * Permite eliminar productos de una orden y actualiza el total de la orden.
     * @param producto el producto a eliminar.
     * @param cant la cantidad de producto a eliminar.
     */
    public void EliminarProducto(Producto producto, BigDecimal cant){
        for (DetalleOrden detalleOrden : detalleOrdenList) {
            if (producto == detalleOrden.producto) {
               detalleOrden.cantidad =  detalleOrden.cantidad.subtract(cant);
               if(detalleOrden.cantidad.intValue()<=0){
                detalleOrdenList.remove(detalleOrden);
               }
               
            }
        }
        CalcularTotal();
    }
    
}
