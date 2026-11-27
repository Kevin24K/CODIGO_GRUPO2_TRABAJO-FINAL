package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

// HUF19: body para PUT /api/Objetos/{id}/foto
public class ObjetoFotoDTO {

    @NotBlank(message = "La foto es obligatoria")
    private String fotoObjeto; // data:image/jpeg;base64,... (ya comprimida en el front)

    public String getFotoObjeto() {
        return fotoObjeto;
    }

    public void setFotoObjeto(String fotoObjeto) {
        this.fotoObjeto = fotoObjeto;
    }
}
