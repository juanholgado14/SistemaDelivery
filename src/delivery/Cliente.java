package delivery;

import java.util.concurrent.atomic.AtomicInteger;

public class Cliente {

	private static final AtomicInteger contadorId = new AtomicInteger();

	private final String nombre;
	
	private final String mail;
	
	private final int id;
	
	public Cliente(String nombre, String mail) {
		
		this.nombre = ValidadorTexto.validarTextoObligatorio(nombre, "nombre");
		
		this.mail = ValidadorTexto.validarMail(mail, "mail");
		
		this.id = contadorId.incrementAndGet();
	}

	public String getNombre() {
		return nombre;
	}

	public String getMail() {
		return mail;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ( !( obj instanceof Cliente) )
			return false;
		
		Cliente other = (Cliente) obj;
		return id == other.id;
	}
	
	@Override
	public String toString() {
		return "N*ID: " + id +
				"\nNombre: " + nombre + 
				"\nMail: " + mail;
	}
}
