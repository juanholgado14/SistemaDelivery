package delivery;

import java.util.concurrent.atomic.AtomicInteger;

public class Repartidor {
 
	private static final AtomicInteger contadorId = new AtomicInteger();
	
	private final int id;
	
	private final String nombre;
	
	private final String telefono;
	
	public Repartidor(String nombre, String telefono) {
		
		this.nombre = ValidadorTexto.validarTextoObligatorio(nombre, "nombre");
		
		this.telefono = ValidadorTexto.validarTextoObligatorio(telefono, "telefono");
		
		this.id = contadorId.incrementAndGet();
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}
	
	@Override
	public String toString() {
		return "ID: " + id +
				"\nNombre: " + nombre + 
				"\nTelefono: " + telefono;
	}
}
