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
	
	private Repartidor repartidor;
	
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

	
	
	public Repartidor getRepartidor() {
		return repartidor;
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
		
		
		exigirEstado(EstadoPedido.CREADO, "confirmar");
		
		if (items.isEmpty()) {
			throw new IllegalStateException("El pedido no puede estar vacio");
		}
		
		transicionarA(EstadoPedido.CONFIRMADO, TipoEventoPedido.CONFIRMADO, clock);
		
		
	}
	
	public void enPreparacion(Clock clock) {
		
		exigirEstado(EstadoPedido.CONFIRMADO, "en preparacion");
		transicionarA(EstadoPedido.EN_PREPARACION, TipoEventoPedido.EN_PREPARACION, clock);
	}
	
	
	public void listoParaRetirar(Clock clock) {
		
		
		exigirEstado(EstadoPedido.EN_PREPARACION, "listo para retirar");
		transicionarA(EstadoPedido.LISTO_PARA_RETIRAR, TipoEventoPedido.LISTO_PARA_RETIRAR, clock);
		
	}
	
	public void asignarRepartidor(Repartidor nuevoRepartidor, Clock clock) {
		
		Objects.requireNonNull(nuevoRepartidor, "El repartidor no puede ser null");
		Objects.requireNonNull(clock, "El clock no puede ser null");
		exigirEstado( EstadoPedido.LISTO_PARA_RETIRAR, "asignar repartidor");

		if (this.repartidor != null) {
			
			throw new IllegalStateException("El pedido ya tiene repartidor asignado");
		}
		
		this.repartidor = nuevoRepartidor;
		
		
		
		LocalDate fecha = LocalDate.now(clock);
		
		historial.add(EventoPedido.de(fecha, TipoEventoPedido.ASIGNADO_REPARTIDOR));
	}
	
	public void enCamino(Clock clock) {
		
		exigirRepartidor();
		exigirEstado( EstadoPedido.LISTO_PARA_RETIRAR, "en camino");
		transicionarA(EstadoPedido.EN_CAMINO, TipoEventoPedido.EN_CAMINO, clock);
	}
	
	private void exigirRepartidor() {
		
		if (repartidor == null) {
			throw new IllegalStateException("No hay repartidor asignado");
		}
	}
	
	public void entregado(Clock clock) {
		
		exigirEstado(EstadoPedido.EN_CAMINO, "entregado");
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
			throw new IllegalStateException("No se puede " + accion +
					 ". Estado actual: " + estado + "." + " Estados permitidos: " +
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
	
	private boolean esTerminal(EstadoPedido estado) {
		
		return estado == EstadoPedido.CANCELADO || 
				estado == EstadoPedido.ENTREGADO;
	}
	

	public void agregarItem(Producto producto,int cantidad) {
		
		Objects.requireNonNull(producto, "El producto no puede ser null");
				
		exigirQuePermitaAgregarItems();
		
		ItemPedido item = new ItemPedido(producto, cantidad);
		
		items.add(item);
	}
	
	public void exigirQuePermitaAgregarItems() {
	    exigirEstado(EstadoPedido.CREADO, "agregar item");
	}
	
	public double calcularTotal() {
		
		double total = 0;
		
		for (ItemPedido item: items) {
			
			total += item.calcularSubtotal();
		}
		
		return total;
	}
	
	@Override
	public String toString() {
		
		return "Id: " + id +
				"\nCliente: " + cliente.getNombre() + 
				"\nRestaurante: " + restaurante.getNombre() + 
				"\nEstado: " + estado + 
				"\nFecha: " + fechaInicio;
}

}