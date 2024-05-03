import java.sql.*;

public class Biblioteca {


    private static Connection getConnection() throws SQLException {
        /*Este método realiza la conexión al servidor. Más adelante lo introduciremos a una variable 
         * para poder trabajar de una manera más cómoda.
         */
        
        String url = "jdbc:postgresql://localhost:5432/Biblioteca";
        String usuario = "postgres";
        String contrasenia = "PasswordSQL";
        return DriverManager.getConnection(url, usuario, contrasenia);
    }
    
    public static String pedirTitulo(){
        /*Con el objetivo de no repetir líneas de código, se crean los siguientes métodos para 
         * poder utilizar el valor que devuelven como variable en otros métodos.
         */
        String tituloLibro;
        System.out.println("Escriba el título del libro: ");
        tituloLibro = App.lector.nextLine();
        return tituloLibro;
    }
    public static String pedirAutor(){
        String autorLibro;
        
        System.out.println("Escriba el autor del libro: ");
        autorLibro = App.lector.nextLine();
        return autorLibro;
    }

    public static String pedirGenero(){
        String generoLibro;
        System.out.println("Escriba el genero del libro: ");
        generoLibro = App.lector.nextLine();
        return generoLibro;
    }

    public static boolean pedirDisponibilidad(){
        boolean disponible = false;
        boolean error = true;
        do {
        System.out.println("Escriba 'Si' para disponible o 'No' para no disponible: ");
        String respuesta = App.lector.nextLine();
        
        if(respuesta.equals("Si") || respuesta.equals("No")){
            error = false;
            if(respuesta.equals("Si")){
                disponible = true;
                return disponible;
            } else if (respuesta.equals("No")) {
                disponible = false;
                return disponible; 
            }
        } else {
            error = true;
            System.out.println("Valor inválido");
        }       
        } while (error);
        return disponible;
    }

    public static String pedirColeccion(){
        String coleccion;
        System.out.println("Escriba el nombre de la coleccion: ");
        coleccion = App.lector.nextLine();
        return coleccion;
    }


    public static void agregarLibro() {

        System.out.println("-- Agregar libro --");
        
        Connection con = null; /*Se declara esta variable como null para que sea accesible por el catch porque la inicializaremos más adelante
        en el bloque try, sino el compilador no sería capaz de encontrar ninguna referencia a esta variable */
          
        try {
            con = getConnection(); // Como dijimos, guardamos el establecimiento de conexión en una variable.
            PreparedStatement statement = con.prepareStatement("INSERT INTO libros (id_coleccion, titulo, autor, genero, disponibilidad) VALUES ((SELECT id_coleccion FROM catalogo WHERE nombre_coleccion = ?),?,?,?, true);");
            /* Esta instrucción SQL agrega en la tabla libros en las columnas que se indican, los valores de las 4 variables que se muestran
            más adelante */
            con.setAutoCommit(false); // Esto hace que no se haga un commit automáticamente.
            /*El primer parámetro de lo que se muestra a continuación indica en qué orden se insertan los valores siendo el primer "?" en la instrucción
             * el argumento 1 y el segundo "?" el argumento 2, y así sucesivamente, además del valor en el segundo parámetro.*/
            statement.setString(1, pedirColeccion());
            statement.setString(2, pedirTitulo());
            statement.setString(3, pedirAutor());
            statement.setString(4, pedirGenero());
            statement.executeUpdate(); //Se ejecuta la instrucción
            System.out.println("Libro agregado correctamente.");
            con.commit(); //Si no salta ninguna Excepción SQL, se guardarán los datos en el base de datos.
            con.close(); // Se cierra la conexión a la base de datos.
        } catch (SQLException e) {
            System.err.println("Error al agregar libro: " + e.getMessage()); //Nos mostrará este mensaje junto con el error que nos indique la base de datos.
            try {
                con.rollback(); //Si  se produce una excepción, los datos introduccidos se revierten y se vuelve al estado de antes de introducir los datos.
                } catch (SQLException el){ /* El método rollback() necesita un manejo de excepción SQL y al estar fuera del try no se puede
                    beneficiar del catch que hay arriba suya, por lo tanto debemos establecer un bloque try-catch para este método.*/
                }
        }   
    }

    public static void agregarColeccion(){
        
        System.out.println("-- Agregar colección --");
        Connection con = null;
        try {
            con = getConnection(); 
            PreparedStatement statement = con.prepareStatement("INSERT INTO catalogo (nombre_coleccion) VALUES (?);");
            con.setAutoCommit(false); 
            statement.setString(1, pedirColeccion());
            statement.executeUpdate();
            System.out.println("Colección agregada correctamente.");
            con.commit(); 
            con.close(); 
        } catch (SQLException e) {
            System.err.println("Error al agregar colección: " + e.getMessage()); 
            try {
                con.rollback(); 
                } catch (SQLException el){ 
                }
        } 
    }

    public static void buscarLibro() {

        /*En este método se hace una búsqueda en las columnas de la tabla libros a partir de un string que introduzca un usuario */
        System.out.println("-- Buscar libro --");
        System.out.println("Ingrese el título, autor o género del libro a buscar:");
        String textoBuscado = App.lector.nextLine();
        Connection con = null;

        try {
            con = getConnection();
            PreparedStatement statement = con.prepareStatement ("SELECT libros.*, catalogo.nombre_coleccion FROM libros JOIN catalogo ON libros.id_coleccion = catalogo.id_coleccion WHERE titulo LIKE ? OR autor LIKE ? OR genero LIKE ?;");
            statement.setString(1, "%" + textoBuscado + "%");
            statement.setString(2, "%" + textoBuscado + "%");
            statement.setString(3, "%" + textoBuscado + "%");
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Libros que coinciden con su búsqueda: \n");

            while (resultSet.next()) {
                /*Imprime los valores de cada fila por cada libro que coincida con los criterios de búsqueda */
                System.out.println("Título: " + resultSet.getString("titulo"));
                System.out.println("Autor: " + resultSet.getString("autor"));
                System.out.println("Género: " + resultSet.getString("genero"));
                System.out.println("Colección: " + resultSet.getString("nombre_coleccion"));
                /* Como disponibilidad es un booleano, para que no salta true o false en pantalla, se hace un if para que 
                 * imprima disponible si es true y no disponible si es false */
                if(resultSet.getBoolean("disponibilidad") == true){
                    System.out.println("Disponible");
                } else {
                    System.out.println("No disponible");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage());
        }
    }

    public static void actualizarLibro() {
        /* Modifica la disponibilidad de un libro a través del input del actor */
        Connection con = null;
        System.out.println("-- Actualizar disponibilidad--");

        try {
            con = getConnection();
            con.setAutoCommit(false); 
            PreparedStatement statement = con.prepareStatement ("UPDATE libros SET disponibilidad = ? WHERE titulo = ? AND autor = ? AND genero = ?;");
            statement.setBoolean(1, pedirDisponibilidad());
            statement.setString(2, pedirTitulo());
            statement.setString(3, pedirAutor());
            statement.setString(4, pedirGenero());
            statement.executeUpdate();
            con.commit();
            System.out.println("Se ha actualizado la disponibilidad del libro correctamente. ");
            con.close();
        } catch (SQLException e) {
                System.err.println("Error al agregar colección: " + e.getMessage()); 
                try {
                    con.rollback(); 
                    } catch (SQLException el){ 
                    }   
        }
    }
    public static void eliminarLibro() {
        Connection con = null;
        System.out.println("-- Eliminar libro no disponible--");

        try {
            con = getConnection();
            con.setAutoCommit(false); 
            /*Elimina un libro cuya disponibilidad sea false y que coincida con los criterios que determine el actor */
            PreparedStatement statement = con.prepareStatement ("DELETE FROM libros WHERE disponibilidad = false AND titulo = ? AND autor = ? AND genero = ?;");
            statement.setString(1, pedirTitulo());
            statement.setString(2, pedirAutor());
            statement.setString(3, pedirGenero());
            int librosEliminados = statement.executeUpdate();
            con.commit();
            System.out.println("Se han eliminado " + librosEliminados +  " libros correctamente.");
            con.close();
        } catch (SQLException e) {
                System.err.println("Error al eliminar el libro: " + e.getMessage()); 
                try {
                    con.rollback(); 
                    } catch (SQLException el){ 
                    }   
        }
    }

}
