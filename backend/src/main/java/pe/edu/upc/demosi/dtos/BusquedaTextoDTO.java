package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// HUF17: lo que escribe la persona que perdió su objeto
public class BusquedaTextoDTO {

    @NotBlank(message = "Escribe qué objeto estás buscando")
    @Size(max = 500, message = "La descripción no debe pasar de 500 caracteres")
    private String texto;

    private Integer top; // cuántos resultados devolver (por defecto 8)

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
}
