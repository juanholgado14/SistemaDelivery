package delivery;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SistemaDelivery {

	private final Map<Integer, Cliente> clientes ;
	
	private final Map<Integer, Restaurante> restaurantes ;
	
	private final Map<Integer, Repartidor> repartidores ;
	
	private final Map<Integer, Pedido> pedidos;
	
	public SistemaDelivery() {
		
	this.clientes = new HashMap<>();
	
	this.restaurantes = new HashMap<>();
	
	this.repartidores = new HashMap<>();
					
	this.pedidos = new HashMap<>();
	}


	public Cliente registrarCliente(String nombre, String mail) {
		
		exigirMailDisponible(mail);
		
		Cliente cliente = new Cliente(nombre, mail);

		clientes.put(cliente.getId(), cliente);
	
		return cliente;
	}
	
	private void exigirMailDisponible(String mail) {
		
		for (Cliente cliente : clientes.values()) {
			
			if ( cliente.getMail().equalsIgnoreCase(mail)) {
				throw new IllegalArgumentException("Ya existe el cliente con ese mail");
			}
		}
	}
	
	public Restaurante registrarRestaurante(String nombre, String direccion) {
		
		Restaurante restaurante = new Restaurante(nombre, direccion);
			
		restaurantes.put(restaurante.getId(), restaurante);
		
		return restaurante;
	}
	
	public Repartidor registrarRepartidor(String nombre, String telefono) {
		
		exigirTelefonoDisponible(telefono);
		
		Repartidor repartidor = new Repartidor(nombre, telefono);
		
		repartidores.put(repartidor.getId(), repartidor);
		
		return repartidor;
	}
	
	private void exigirTelefonoDisponible(String telefono) {
		
		for(Repartidor repartidor : repartidores.values()) {
			if (repartidor.getTelefono().equalsIgnoreCase(telefono)) {
				throw new IllegalArgumentException("El repartidor ya existe");
			}
		}
	}
	
	public void agregarProductoARestaurante(int idRestaurante, String nombre,
			double precio, int stock, CategoriaProducto categoria) {
		
		Restaurante restaurante= buscarRestaurantePorId(idRestaurante);
		
		Producto producto = new Producto(nombre, precio, stock, categoria);
		
		restaurante.agregarProducto(producto);
		
		
	}
	
	public Pedido crearPedido(int idCliente, int idRestaurante, Clock clock) {
		
		Restaurante restaurante = buscarRestaurantePorId(idRestaurante);
		Cliente cliente = buscarClientePorId(idCliente);
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = new Pedido(restaurante, cliente, clock);
		
				
		pedidos.put(pedido.getId(), pedido);
		
		return pedido;
	}
	
	public void agregarItemAPedido(int idPedido, int idProducto, int cantidad) {

		Pedido pedido = buscarPedidoPorId(idPedido);
		Restaurante restaurante = pedido.getRestaurante();
		Producto producto = restaurante.buscarProductoPorId(idProducto);
		
		producto.exigirStockSuficiente(cantidad);
		
		pedido.agregarItem(producto, cantidad);
		 producto.descontarStock(cantidad);
	}

	private Restaurante buscarRestaurantePorId(int idRestaurante) {
		
		Restaurante restaurante= restaurantes.get(idRestaurante);
		
		if (restaurante == null) {
			throw new IllegalArgumentException("No existe restaurante con ese id: "
		+ idRestaurante);
		}
		
		return restaurante;
	}
	
	private Cliente buscarClientePorId(int idCliente) {
		
		Cliente cliente = clientes.get(idCliente);
		
		if ( cliente == null) {
			throw new IllegalArgumentException("No existe cliente con ese id: " 
		+ idCliente);
		}
		return cliente;	
	}
	
	private Pedido buscarPedidoPorId(int idPedido) {
		
		Pedido pedido = pedidos.get(idPedido);
		
		if (pedido == null) {
			throw new IllegalArgumentException("El pedido no existe con ese id: " 
		+ idPedido);
		}
		
		
		return pedido;
		
	}
	
	public void confirmarPedido(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.confirmar(clock);
	}
	
	public void iniciarPreparacion(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.enPreparacion(clock);
	}
	
	public void marcarListoParaRetirar(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.listoParaRetirar(clock);
	}
	
	public void asignarRepartidor(int idPedido, int idRepartidor, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		Repartidor repartidor = buscarRepartidorPorId(idRepartidor);
		
		exigirRepartidorDisponible(idRepartidor);
		
		pedido.asignarRepartidor(repartidor, clock);
	}
	
	private Repartidor buscarRepartidorPorId(int idRepartidor) {
		
		Repartidor repartidor = repartidores.get(idRepartidor);
		
		if (repartidor == null) {
			throw new IllegalArgumentException("El repartidor no existe con ese id: " 
		+ idRepartidor);
		}
		
		
		return repartidor;
	}
	
	private void exigirRepartidorDisponible(int idRepartidor) {
		
		for (Pedido pedido : pedidos.values()) {
			if (pedido.getRepartidor() != null 
					&& pedido.getRepartidor().getId() == idRepartidor
					&& pedido.getEstado() != EstadoPedido.ENTREGADO 
					&& pedido.getEstado() != EstadoPedido.CANCELADO) {
				
				throw new IllegalStateException("El repartidor ya tiene un pedido"
						+ " asignado.");
			}
		}
	}
	
	public void iniciarEntrega(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.enCamino(clock);
	}
	
	public void entregar(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.entregado(clock);
	}
	
	public void cancelar(int idPedido, Clock clock) {
		
		Objects.requireNonNull(clock, "El clock no puede ser null");
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		pedido.cancelarPedido(clock);
	}
	
	public Collection<Cliente> obtenerClientes() {
	    return new ArrayList<>(clientes.values());
	}
	
	public Collection<Restaurante> obtenerRestaurantes() {
	    return new ArrayList<>(restaurantes.values());
	}
	
	public Collection<Repartidor> obtenerRepartidores() {
	    return new ArrayList<>(repartidores.values());
	}
	
	public Collection<Pedido> obtenerPedidos() {
	    return new ArrayList<>(pedidos.values());
	}
	
	public Collection<Pedido> buscarPedidosPorCliente(int idCliente){
		
		Cliente cliente = buscarClientePorId(idCliente);
		
		Collection<Pedido> pedidosDelCliente = new ArrayList<>();
		
		for ( Pedido pedido : pedidos.values()) {
			if (pedido.getCliente().equals(cliente)) {
				pedidosDelCliente.add(pedido);
			}
		}
		return pedidosDelCliente;
	}
	
	public Collection<Pedido> buscarPedidosPorEstado(EstadoPedido estado){
		
		Objects.requireNonNull(estado, "El estado no puede ser null");
		
		Collection<Pedido> pedidosPorEstado = new ArrayList<>();
		
		for (Pedido pedido: pedidos.values()) {
			if ( pedido.getEstado() == estado) {
				pedidosPorEstado.add(pedido);
			}
		}
		return pedidosPorEstado;
	}
	
	public double calcularTotalPedido(int idPedido) {
		
		Pedido pedido = buscarPedidoPorId(idPedido);
		
		return pedido.calcularTotal();
	}
}
