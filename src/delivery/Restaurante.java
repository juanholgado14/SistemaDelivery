package delivery;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurante {

	private static final AtomicInteger idContador = new AtomicInteger();
	
	private final int id; 
	
	private final String nombre;
	
	private final String direccion;
	
	private final Map<Integer , Producto> menu = new LinkedHashMap<>();
	
	public Restaurante(String nombre, String direccion) {
		
		this.nombre = ValidadorTexto.validarTextoObligatorio(nombre, "nombre");
		
		this.direccion = ValidadorTexto.validarTextoObligatorio(direccion, "direccion");
		
		this.id = idContador.incrementAndGet();
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public Map<Integer, Producto> getMenu() {
		return new LinkedHashMap<>(menu);
	}

	public void agregarProducto(Producto producto) {
		
		Objects.requireNonNull(producto, "El producto no puede ser null");
		
		exigirNombreProductoDisponible(producto.getNombre());
		
		menu.put(producto.getId(), producto);
	}
	
	private void exigirNombreProductoDisponible(String nombre) {
		
		for (Producto producto: menu.values()) {
			
			if (producto.getNombre().equalsIgnoreCase(nombre)) {
				throw new IllegalArgumentException("Ya existe un producto "
						+ "con ese nombre en este restaurante");
			}
		}
	}
	
	public Producto buscarProductoPorId(int idProducto) {
		
		Producto producto = menu.get(idProducto);
		
		if (producto == null) {
			throw new IllegalStateException("No existe el producto con ese id: " + 
		idProducto);
		}
		
		return producto;
	}

	
	@Override
	public String toString() {
		return "ID: "+ id + 
				"\nNombre: " + nombre+ 
				"\nDireccion: " + direccion;
	}
}
