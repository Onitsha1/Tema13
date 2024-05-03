import java.util.Scanner;


public class App {
    static Scanner lector = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        menuPrincipal();

    }
    public static void menuPrincipal (){
        /*Este método muestra el menú con las acciones que podemos realizar */
        String opcionLeida;
        int opcion = -1;
        boolean error = true; 
        
        do {
            try {
                System.out.println("-- Menú principal --");
                System.out.println("1. Agregar colección");
                System.out.println("2. Agregar libro");
                System.out.println("3. Buscar libro");
                System.out.println("4. Actualizar libro");
                System.out.println("5. Eliminar libro no disponible");
                System.out.println("6. Salir");
                System.out.println("Escriba una de las opciones anteriores: ");
                opcionLeida = lector.nextLine();
                opcion = Integer.parseInt(opcionLeida);
                if (opcion >= 1 && opcion <= 6){
                    error = false;
                } else {
                    error = true;
                    System.out.println("No existe esa opción");
                }
            } catch (Exception e){
                error = true;
                System.out.println("Valor no válido");
            }
        switch (opcion) {
            case 1:
                Biblioteca.agregarColeccion();
                break;
            case 2:
                Biblioteca.agregarLibro();
                break;
            case 3:
                Biblioteca.buscarLibro();
                break;
            case 4:
                Biblioteca.actualizarLibro();
                break;
            case 5:
                Biblioteca.eliminarLibro();
                break;
            case 6:
            System.out.println("Hasta pronto");
                break;
        }
    } while(error || opcion != 6);
        lector.close();
    }  
}

