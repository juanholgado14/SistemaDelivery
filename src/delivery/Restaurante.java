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
		return Map.copyOf(menu);
	}

	public void agregarProducto(Producto producto) {
		
		Objects.requireNonNull(producto, "El producto no puede ser null");
		
		if (menu.containsKey(producto.getId()) ) {
			throw new IllegalArgumentException("El menu ya contiene ese producto " + producto.getNombre());
		}
		
		menu.put(producto.getId(), producto);
	}
	
	@Override
	public String toString() {
		return "ID: "+ id + 
				"\nNombre: " + nombre+ 
				"\nDireccion:" + direccion;
	}
}
