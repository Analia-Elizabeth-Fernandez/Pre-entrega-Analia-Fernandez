import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ArrayList<Articulo> productosDB = obtenerArticulo();
    private static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bienvenidos a Libreria Anivel");
        label:
        while (true) {
            System.out.println("""
                    Ingrese el número para elegir opción:
                    0 - Finalizar aplicación
                    1 - Agregar articulo
                    2 - Listar articulos
                    3 - Búsqueda articulo
                    4 - Editar nombre del articulo
                    5 - Eliminar articulo
                    6 - nueva funcion
                    """);
            int opcionUsuario = entrada.nextInt();
            entrada.nextLine();

            switch (opcionUsuario) {
                case 1 -> crearProducto(productosDB);
                case 2 -> listarProductos(productosDB);
                case 3 -> buscarProductoPorNombre(productosDB);
                case 4 -> editarProducto(productosDB);
                case 5 -> borrarProducto(productosDB);
                case 6 -> filtroPorPrecio(productosDB);
                case 0 -> {
                    System.out.println("Gracias por usar la app de Anivel!");
                    break label; // corta el bucle donde se ejecuta
                }
                default -> System.out.println("Opción incorrecta por favor, intente de nuevo");
            }
        }
    }

    public static void crearProducto(ArrayList<Articulo> productos) {
        System.out.println("Creando Nuevo Articulo de librería");

        System.out.print("Ingrese el nombre del nuevo articulo de librería: ");
        String nombre = entrada.nextLine();

        System.out.print("Ingrese la marca: ");
        String marca = entrada.nextLine();

        System.out.print("Ingrese el precio: ");
        double precio = entrada.nextDouble();
        entrada.nextLine();

        System.out.print("Ingrese la descripción: ");
        String descripcion = entrada.nextLine();

        // podriamos tener bucles que validen los datos

        System.out.print("Ingrese la categoría: ");
        String categoria = entrada.nextLine();

        // TODO: cambiarlo cuando veamos static
        productos.add(new Articulo(nombre, marca, precio, descripcion, categoria));

        System.out.print(" Articulo agregado con éxito! ");
        pausa();
    }

    public static void listarProductos(ArrayList<Articulo> productos) {
        System.out.println("====================================================================================================================================");
        System.out.println("                       LISTA DE ARTICULOS DE LIBRERIA                                                                               ");
        System.out.println("====================================================================================================================================");

        if (productos == null || productos.isEmpty()) {
            System.out.println("⚠️  No hay articulos para mostrar.");
        } else {
            System.out.printf("| %-3s | %-35s | %-20s | %-10s | %-10s | %-35s |%n",
                    "ID", "Nombre", "Marca", "Precio", "Categoría", "Descripción");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------");

            for (Articulo producto : productos) {
                System.out.printf("| %3d | %-35s | %-20s | $%9.2f | %-10s | %-35s |%n",
                        producto.getId(),
                        producto.getNombre(),
                        producto.getMarca(),
                        producto.getPrecio(),
                        producto.getCategoria(),
                        producto.getDescripcion());
            }
        }

        System.out.println("======================================================================================================================================");
        pausa();
    }



    public static void buscarProductoPorNombre(ArrayList<Articulo> productos) {
        System.out.println("Ingrese nombre del articulo: ");
        String busqueda = entrada.nextLine();
        ArrayList<Articulo> productoEncontrados = new ArrayList<>();

        for (Articulo producto : productos) {
            if (estaIncluido(producto.getNombre(), busqueda)) {
                productoEncontrados.add(producto);
            }
        }

        listarProductos(productoEncontrados);
    }

    public static void editarProducto(List<Articulo> productos) {
        // el listado de productos tiene las direcciones de memoria de los productos originales
        // aca obtenemos la direccion de memoria que nos permite modificar el objeto original
        // que es uno de los que esta en el listado
        Articulo producto = obtenerProductoPorId(productos);
        // TODO: validar que encontramos el producto
        if (producto == null) {
            System.out.println("No se puede editar el articulo.");
            pausa();
            return; // cuando hacemos el return en una funcion void, estamos cortando la ejecucion de la funcion
        }

        String nombreOriginal = producto.getNombre();
        System.out.println("Articulo a editar:");
        System.out.println(nombreOriginal);

        // TODO: validar que el usuario quiere editar el producto que se encontro
        System.out.print("Ingrese el nuevo nombre: ");
        entrada.nextLine();
        String nuevoNombre = entrada.nextLine();


        // actualizamos el nombre en el producto
        producto.setNombre(nuevoNombre);

        System.out.printf("El nombre del articulo cambio de %s a %s", nombreOriginal, nuevoNombre);
    }

    public static void borrarProducto(List<Articulo> productos) {
        Articulo producto = obtenerProductoPorId(productos);
        // TODO: validar que encontramos el producto
        if (producto == null) {
            System.out.println("No se pudo borrar el articulo");
            pausa();
            return; //
        }
        String nombreOriginal = producto.getNombre();
        System.out.println("Articulo a borrar:");
        System.out.println(nombreOriginal);
        // TODO: validar que el usuario quiere borrar el producto que se encontro

        // aca borramos el producto
        productos.remove(producto);
        System.out.println("Borrado exitosamente!");
    }

    public static void filtroPorPrecio(List<Articulo> productos) {
        double precioFiltro = entrada.nextDouble();

        ArrayList<Articulo> productosFiltrados = new ArrayList<>();

        for (Articulo producto : productos) {
            if (producto.getPrecio() <= precioFiltro) {
                productosFiltrados.add(producto);
            }
        }

        listarProductos(productosFiltrados);
    }

    /* UTILIDADES */
    /* Busqueda por id - ahora mismo solo funciona con el indice, en el futuro se va a cambiar */

    public static Articulo obtenerProductoPorId(List<Articulo> productos) {
        // TODO: validacion de datos
        System.out.println("Ingrese el id del articulo: ");
        int idBusqueda = entrada.nextInt();
        entrada.nextLine();

        for (Articulo producto : productos) {
            if (producto.coincideId(idBusqueda)) {
                return producto;
            }
        }

        System.out.println("No pudimos encontrar el articulo con el id: " + idBusqueda);
        return null; // el null representa que no encontramos el producto
    }

    public static boolean estaIncluido(String nombreCompleto, String nombreParcial) {
        String nombreCompletoFormateado = formatoBusqueda(nombreCompleto);

        return nombreCompletoFormateado.contains(formatoBusqueda(nombreParcial));
    }

    public static String formatoBusqueda(String texto) {
        return texto.trim().toLowerCase();
    }

    public static void pausa() {
        System.out.println("Pulse ENTER para continuar...");
        entrada = new Scanner(System.in);
        entrada.nextLine();
        for (int i = 0; i < 20; ++i) {
            System.out.println();
        }
        // TODO: limpiar la pantalla de la consola
    }

    public static ArrayList<Articulo> obtenerArticulo() {
        ArrayList<Articulo> productos = new ArrayList<>();

        productos.add(new Articulo(
                "Tizas",
                "Eureka",
                25156.95,
                "Tizas color pastel para pizarra",
                "Accesorio"));

        productos.add(new Articulo(
                "Tijera multiuso",
                "Bremen",
                5722.99,
                "Tijera multiuso de acero inoxidable",
                "Accesorio"));

        productos.add(new Articulo(
                "Acuarelas escolares",
                "Filgo",
                2993.99,
                "Set de acuarelas escolares x 12",
                "Colorear"));

        productos.add(new Articulo(
                "Crayones de cera",
                "Filgo",
                1987.99,
                "Caja de crayones de cera x 6",
                "Colorear"));

        productos.add(new Articulo(
                "Marcadores escolares",
                "Faber Castell",
                12219.99,
                "Set de marcadores escolares x 30",
                "Colorear"));

        productos.add(new Articulo(
                "Lapices de colores",
                "Faber-Castell",
                4154.99,
                "Caja de lapices escolares x 12",
                "Colorear"));

        productos.add(new Articulo(
                "Lapiz corrector",
                "Paper mate",
                5565,
                "Corrector",
                "Borrador"));

        productos.add(new Articulo(
                "Lapicera azul",
                "Bic",
                4178.99,
                "Set x 3",
                "Escritura"));

        productos.add(new Articulo(
                "Lapicera negra",
                "Bic",
                4178.99,
                "Set x 3",
                "Escritura"));

        productos.add(new Articulo(
                "Lapiz",
                "Staedtler Norris",
                2560,
                "Lapiz negro 2 HB",
                "Escritura"));


        return productos;
    }

}