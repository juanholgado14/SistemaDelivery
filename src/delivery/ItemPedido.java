package delivery;

import java.util.Objects;

public class ItemPedido {

	private final Producto producto;
		
	private final int cantidad;
	
	private final double precioUnitario;
	
	public ItemPedido(Producto producto, int cantidad) {
		
		this.producto = Objects.requireNonNull(producto, "El producto no puede ser null");
		
		if ( cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
		}
		
		this.cantidad = cantidad;
		
		this.precioUnitario = producto.getPrecio();
	}

	public Producto getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}
	
	public double calcularSubtotal() {
		
		return cantidad*precioUnitario;
	}
	
	@Override
	public String toString() {
		
		return "Producto: " + producto.getNombre() + 
				"\nCantidad: " + cantidad + 
				"\nPrecio Unitario: " + precioUnitario +
				"\nSubtotal: " + calcularSubtotal();
	}
}
