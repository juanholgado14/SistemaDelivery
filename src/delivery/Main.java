package delivery;

import java.awt.ItemSelectable;
import java.time.Clock;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SistemaDelivery sistema = new SistemaDelivery();
		
		Clock clock = Clock.systemDefaultZone();
		
	  Cliente juan = sistema.registrarCliente("Juan Holgado", "juanholgado@gmail.com");
	  Cliente florencia = sistema.registrarCliente("florencia valente", "flor@gmail.com");
	  
	  Restaurante king = sistema.registrarRestaurante("king", "salta 438");
	  Restaurante chulo =sistema.registrarRestaurante("chulo", "justo 1010");
	  
	  Repartidor raul = sistema.registrarRepartidor("raul castro","12345");
	  Repartidor Lucas = sistema.registrarRepartidor("lucas gonzales", "11223");
	  
	 
	  sistema.agregarProductoARestaurante(king.getId(),
			  "Hamburgesa simlpe", 6500, 80, CategoriaProducto.COMIDA);
	  sistema.agregarProductoARestaurante(king.getId(),
			  "Hamburgesa doble", 8500, 60, CategoriaProducto.COMIDA);
	  sistema.agregarProductoARestaurante(king.getId(),
			  "Hamburgesa triple", 9500, 40, CategoriaProducto.COMIDA);
	  sistema.agregarProductoARestaurante(king.getId(),
			  "papas", 1900, 90, CategoriaProducto.COMIDA);
	  sistema.agregarProductoARestaurante(king.getId(),
			  "papas grandes", 2500, 90, CategoriaProducto.COMIDA);
	  sistema.agregarProductoARestaurante(king.getId(),
			  "Coca Cola", 2500, 80, CategoriaProducto.BEBIDA);
	  
	  
	  sistema.agregarProductoARestaurante(chulo.getId(),
			  "Helado 1 bocha", 2350, 20, CategoriaProducto.POSTRE);
	  sistema.agregarProductoARestaurante(chulo.getId(),
			  "Helado 2 bocha", 4350, 20, CategoriaProducto.POSTRE);
	  sistema.agregarProductoARestaurante(chulo.getId(),
			  "Coca Cola", 2350, 100, CategoriaProducto.BEBIDA);
	  sistema.agregarProductoARestaurante(chulo.getId(),
			  "Promo 1", 20000, 10, CategoriaProducto.PROMOCION);
	  
	  /*System.out.println("=== CLIENTES ===");

	  for (Cliente cliente : sistema.obtenerClientes()) {
	      System.out.println(cliente);
	  }
	  
	  System.out.println("=== Productos King===");

	  for (Producto producto : king.obtenerProductos()) {
	      System.out.println(producto);
	  }
	
	*/

	
	 Pedido pedido1 = sistema.crearPedido(juan.getId(), chulo.getId(), clock);
	
	 boolean seguir = true;

	 Scanner ingreso= new Scanner(System.in);
	 
	 do {
		 
		  System.out.println("=== Productos Chulo===");
		   
		for (Producto producto : chulo.obtenerProductos()) {
			  		System.out.println(producto);
			  }
		 System.out.println("\nIngrese item\n ");
		 
		 int idItem = ingreso.nextInt();
		 
		 System.out.println("\nIngrese cantidad:\n");
		 
		 int cantidad = ingreso.nextInt();
		 
		 sistema.agregarItemAPedido(pedido1.getId(),idItem , cantidad);
		 
		 System.out.println("quiere seguir algregando? true/false");
		 
		 seguir = ingreso.nextBoolean();
		 
	}while(seguir);
	 
	 
	 double total = 0;
	 
	total =  sistema.calcularTotalPedido(pedido1.getId());
	 
	System.out.println("total: " + total);
	 sistema.confirmarPedido(pedido1.getId(), clock);
	 
	 
	 sistema.iniciarPreparacion(pedido1.getId(), clock);
	 
	 sistema.marcarListoParaRetirar(pedido1.getId(), clock);
	 
	 sistema.asignarRepartidor(pedido1.getId(), raul.getId(), clock);
	 
	 sistema.iniciarEntrega(pedido1.getId(), clock);
	 
	 sistema.entregar(pedido1.getId(), clock);
	 
	 System.out.println("-----Producto PEDIDOS------");
	 for (Pedido pedido : sistema.obtenerPedidos()) {
		 System.out.println("--------------------------------");
		 System.out.println("Pedido #" + pedido.getId());
		 System.out.println("Cliente: " + pedido.getCliente().getNombre());
		 System.out.println("Restaurante: " + pedido.getRestaurante().getNombre());
		 System.out.println("Estado: " + pedido.getEstado());

		 for (ItemPedido item : pedido.obtenerItems()) {
		     System.out.println("  - " + item);
		 }
		 
		 
	 }
	 
	 
}
}
