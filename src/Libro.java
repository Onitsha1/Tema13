public class Libro {
    
    private String titulo;
    private String autor;
    private String genero;
    private boolean disponible;

    public Libro (String titulo, String autor, String genero, boolean disponible){
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.disponible = disponible;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getGenero() {
        return genero;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    

    


    
}
