package delivery;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Producto {

	private static final AtomicInteger contadorId = new AtomicInteger();
	
	private final int id;
	
	private final  String nombre; 
	
	private final double precio;
	
	private final CategoriaProducto categoria;
	
	private int stock;
	
	public Producto(String nombre, double precio, int stock, CategoriaProducto categoria) {
		
		if (precio <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor a cero");
		}
		
		if (stock<0) {
			throw new IllegalArgumentException("El stock no puede ser negativo");
		}
		
		this.nombre = ValidadorTexto.validarTextoObligatorio(nombre, "nombre");
		
		this.precio = precio;
		
		this.stock = stock;
				
		this.id = contadorId.incrementAndGet();
		
		this.categoria = Objects.requireNonNull(categoria, "La categoria no puede ser null");
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public CategoriaProducto getCategoria() {
		return categoria;
	}

	public int getStock() {
		return stock;
	}

	@Override
	public String toString() {
		return id +	"\nNombre: " + nombre + 
				"\nPrecio: " + precio + 
				"\nStock: " + stock + 
				"\nCategoria: " + categoria;
	}
}
