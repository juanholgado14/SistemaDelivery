package delivery;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

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
	
	public Producto agregarProductoARestaurante(int idRestaurante, String nombre,
			double precio, int stock, CategoriaProducto categoria) {
		
		Restaurante restaurante= buscarRestaurantePorId(idRestaurante);
		
		Producto producto = new Producto(nombre, precio, stock, categoria);
		
		restaurante.agregarProducto(producto);
		
		return producto;
	}
	
	public Pedido crearPedido(int idCliente, int idRestaurante, Clock clock) {
		
		Restaurante restaurante = buscarRestaurantePorId(idRestaurante);
		Cliente cliente = buscarClientePorId(idCliente);
			
		
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
			throw new IllegalStateException("No existe restaurante con ese id: "
		+ idRestaurante);
		}
		
		return restaurante;
	}
	
	private Cliente buscarClientePorId(int idCliente) {
		
		Cliente cliente = clientes.get(idCliente);
		
		if ( cliente == null) {
			throw new IllegalStateException("No existe cliente con ese id: " 
		+ idCliente);
		}
		return cliente;	
	}
	
	private Pedido buscarPedidoPorId(int idPedido) {
		
		Pedido pedido = pedidos.get(idPedido);
		
		if (pedido == null) {
			throw new IllegalStateException("El pedido no existe con ese id: " 
		+ idPedido);
		}
		return pedido;
		
	}
	
	
}
