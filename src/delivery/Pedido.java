package delivery;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido {

	private static final AtomicInteger idContador = new AtomicInteger();
	
	private final int id;
		
	private final Restaurante restaurante;
	
	private final Cliente cliente;
	
	private EstadoPedido estado;
	
	private final LocalDate fechaInicio;
	
	private final List<EventoPedido> historial = new ArrayList<>();
	
	private final List<ItemPedido> items = new ArrayList<>(); 
	
	public Pedido(Restaurante restaurante, Cliente cliente, Clock clock) {
		
		this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser null");
	 	this.restaurante = Objects.requireNonNull(restaurante, "El restaurante no puede ser null");
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		this.fechaInicio = LocalDate.now(clock);
		
		this.id = idContador.incrementAndGet();
		
		this.estado = EstadoPedido.CREADO;
		
		historial.add(EventoPedido.de(fechaInicio,  TipoEventoPedido.CREADO));
	}

	public int getId() {
		return id;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public List<EventoPedido> getHistorial() {
		return List.copyOf(historial);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public List<ItemPedido> getItems() {
		return List.copyOf(items);
	}
	
	public void confirmar(Clock clock) {
		
		exigirEstado(EstadoPedido.CREADO, "Confirmado");
		transicionarA(EstadoPedido.CONFIRMADO, TipoEventoPedido.CONFIRMADO, clock);
		
	}
	
	public void enPreparacion(Clock clock) {
		
		exigirEstado(EstadoPedido.CONFIRMADO, "En preparacion");
		transicionarA(EstadoPedido.EN_PREPARACION, TipoEventoPedido.EN_PREPARACION, clock);
	}
	
	public void listoParaRetirar(Clock clock) {
		
		
		exigirEstado(EstadoPedido.EN_PREPARACION, "Listo para retirar");
		transicionarA(EstadoPedido.LISTO_PARA_RETIRAR, TipoEventoPedido.LISTO_PARA_RETIRAR, clock);
	}
	
	public void enCamino(Clock clock) {
		
		exigirEstado(EstadoPedido.LISTO_PARA_RETIRAR, "En camino");
		transicionarA(EstadoPedido.EN_CAMINO, TipoEventoPedido.EN_CAMINO, clock);
	}
	
	public void entregado(Clock clock) {
		
		exigirEstado(EstadoPedido.EN_CAMINO, "Entregado");
		transicionarA(EstadoPedido.ENTREGADO, TipoEventoPedido.ENTREGADO, clock);
		
	}
	
	public void cancelarPedido(Clock clock) {
		
		exigirEstado(EnumSet.of(EstadoPedido.CREADO, EstadoPedido.CONFIRMADO), "cancelar");
		transicionarA(EstadoPedido.CANCELADO, TipoEventoPedido.CANCELADO, clock);
	}
	
	private void exigirEstado(EstadoPedido requerido, String accion) {
		exigirEstado(EnumSet.of(requerido), accion);
	}
	
	private void exigirEstado(Set<EstadoPedido> permitidos, String accion) {
		
		Objects.requireNonNull(permitidos, "Permitidos no puede ser null");
		
		if (!permitidos.contains(estado)) {
			throw new IllegalStateException("El pedido no esta en un estado permitido "
					+ "ya esta en estado: " + estado + "." + "Estados permitidos: " +
					estadosPermitidos(permitidos));
		}
		
	}
	
	private String estadosPermitidos(Set<EstadoPedido> permitidos) {
		
		if(permitidos.size() == 1) {
			return permitidos.iterator().next().toString();
		}
		return permitidos.toString();
	}
	
	private void transicionarA(EstadoPedido nuevoEstado, TipoEventoPedido tipo
			,Clock clock) {
		
		Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser null");
		Objects.requireNonNull(tipo, "El tipo de evento no puede ser null");
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		if (estado == nuevoEstado) {
			throw new IllegalArgumentException("El estado esta en : " + estado + 
					"no se puede modificar");
		}
		
		if (esTerminal(estado)) {
			throw new IllegalStateException("El estado ya esta en estado"
					+ "terminal: " + estado);
		}
		
		LocalDate ahora = LocalDate.now(clock);
		
		estado = nuevoEstado;
		
		historial.add(EventoPedido.de(ahora, tipo));
	}
	
	private boolean esTerminal(EstadoPedido nuevoEstado) {
		
		return nuevoEstado == EstadoPedido.CANCELADO || 
				nuevoEstado == EstadoPedido.ENTREGADO;
	}
	
	@Override
	public String toString() {
		
		return "Id: " + id +
				"\nCliente: " + cliente.getNombre() + 
				"\nRestaurante" + restaurante.getNombre() + 
				"\nEstado: " + estado + 
				"\nFecha: " + fechaInicio;
}

}