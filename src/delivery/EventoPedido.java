package delivery;

import java.time.LocalDate;
import java.util.Objects;

public class EventoPedido {

	private final LocalDate fecha;
	
	private final TipoEventoPedido tipo;
	
	public EventoPedido(LocalDate fecha, TipoEventoPedido tipo) {
		
		this.fecha = Objects.requireNonNull(fecha, "La fecha no puede ser null");
		
		this.tipo = Objects.requireNonNull(tipo, "El tipo de  evento no puede ser null");
	}
	
	public static  EventoPedido de(LocalDate fecha, TipoEventoPedido tipo) {
		
		return new EventoPedido(fecha, tipo);
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public TipoEventoPedido getTipo() {
		return tipo;
	}
	
	@Override
	public String toString() {
		return fecha + "-" + tipo;
	}
}
