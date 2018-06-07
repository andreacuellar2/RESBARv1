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
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import sv.edu.diseno.excepciones.ErrorAplicacion;
import sv.edu.diseno.provider.EntityManagerProvider;

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
    ,  @NamedQuery(name = "Orden.updateDetalleOrden", query = "UPDATE DetalleOrden do SET do.cantidad = :cantidad WHERE do.orden.idOrden = :idOrden AND do.producto.idProducto = :idProducto")
    , @NamedQuery(name = "Orden.deleteDetalleOrden", query = "DELETE FROM DetalleOrden do WHERE do.orden.idOrden = :idOrden AND do.producto.idProducto = :idProducto")
    , @NamedQuery(name = "Orden.findByMesero", query = "SELECT o FROM Orden o WHERE o.mesero = :mesero")
    , @NamedQuery(name = "Orden.findByMesa", query = "SELECT o FROM Orden o WHERE o.mesa = :mesa")
    , @NamedQuery(name = "Orden.findByCliente", query = "SELECT o FROM Orden o WHERE o.cliente = :cliente")
    , @NamedQuery(name = "Orden.findByFupdateDetalleOrdenecha", query = "SELECT o FROM Orden o WHERE o.fecha = :fecha")
    , @NamedQuery(name = "Orden.findByComentario", query = "SELECT o FROM Orden o WHERE o.comentario = :comentario")
    , @NamedQuery(name = "Orden.calcularTotal", query = "SELECT SUM (p.precio*do.cantidad) FROM DetalleOrden do INNER JOIN do.producto p WHERE do.orden.idOrden = :idOrden ")   
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
     * Método: CalcularTotal() Almacena el total de consumo de la orden, para
     * ello recorre toda su colección DETALLE multiplicando el precio unitario
     * por la cantidad y luego sumándolo para al final actualizar la propiedad
     * total de la orden con el valor correcto.
     */
    public void CalcularTotal() {
        EntityManager eml = EntityManagerProvider.getEntityManager();
        try {
            Query q = eml.createNamedQuery("Orden.calcularTotal");
            q.setParameter("idOrden", this.idOrden);
            this.total = (BigDecimal) q.getSingleResult();

        } catch (Exception ex) {
            throw new ErrorAplicacion("Orden.CalcularTotal()$Error al calcular el total de la orden"+ ex.getMessage());
        } finally {
            if (eml.isOpen()) {
                eml.close();
            }
        }
    }

    /**
     * Método: AgregarProducto(:producto,cant:double) Permite agregar más
     * productos a la orden, toma el objeto producto y la cantidad para
     * construir un objeto DetalleOrden, y luego ver si ese producto ya está
     * agregado a la orden, si ya está agregado a la orden, entonces solo se
     * suma la cantidad, sino se agrega a la colección DETALLE de la orden y se
     * invoca calcular total.
     */
    public void AgregarProducto(Producto producto, double cant) {

        if (cant <= 0) {
            throw new ErrorAplicacion("Orden.AgregarProducto()$La cantidad debe ser mayor a cero");
        }

        EntityManager eml = EntityManagerProvider.getEntityManager();

        DetalleOrden detalleOrden = new DetalleOrden();
        detalleOrden.cantidad = new BigDecimal(cant);

        DetalleOrdenPK detalleOrdenPK = new DetalleOrdenPK();
        detalleOrdenPK.idOrden = this.idOrden;
        detalleOrdenPK.idProducto = producto.idProducto;

        detalleOrden.detalleOrdenPK = detalleOrdenPK;

        boolean encontrado = false;

        for (DetalleOrden d : this.detalleOrdenList) {
            if (Objects.equals(d.producto.idProducto, detalleOrden.producto.idProducto)) {
                encontrado = true;
                d.cantidad.add(new BigDecimal(cant));
            }
        }

        if (!encontrado) {
            this.detalleOrdenList.add(detalleOrden);
        }

        EntityTransaction et = eml.getTransaction();
        try {
            if (!et.isActive()) {
                et.begin();
            }
            eml.merge(this);
            et.commit();
            this.CalcularTotal();
        } catch (Exception ex) {
            if (et.isActive()) {
                et.rollback();
            }
            throw new ErrorAplicacion("Orden.AgregarProducto()$Algo fallo intentando agregar un nuevo producto");
        } finally {
            if (eml.isOpen()) {
                eml.close();
                this.CalcularTotal();
            }
        }
    }

    /**
     * Método: EliminarProducto(:producto,cant:double) Permite eliminar
     * productos de una orden y actualiza el total de la orden.
     */
    public void EliminarProducto(Producto producto, double cant) {
        if (cant < 0) {
            throw new ErrorAplicacion("Orden.EliminarProducto()$La cantidad debe ser mayor a cero");
        }
        
        EntityManager eml = EntityManagerProvider.getEntityManager();
        try {
            if (cant > 0) {
                Query q = eml.createNamedQuery("Orden.updateDetalleOrden");
                q.setParameter("idOrden", this.idOrden);
                q.setParameter("idProducto", producto.idProducto);
                q.setParameter("cantidad", cant);
            } else if (cant == 0) {
                Query q = eml.createNamedQuery("Orden.deleteDetalleOrden");
                q.setParameter("idOrden", this.idOrden);
                q.setParameter("idProducto", producto.idProducto);
            }
        } catch (Exception ex) {
            throw new ErrorAplicacion("Orden.EliminarProducto()$Error al eliminar productos de la orden");
        } finally {
            if (eml.isOpen()) {
                eml.close();
                this.CalcularTotal();
            }
        }
    }
    
}
