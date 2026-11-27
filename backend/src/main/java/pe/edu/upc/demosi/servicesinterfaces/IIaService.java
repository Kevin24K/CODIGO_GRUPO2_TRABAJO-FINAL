package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.dtos.ObjetoSimilarDTO;

import java.util.List;

// HUF17 / HUF19: servicio de IA (Hugging Face + Smile)
public interface IIaService {

    // HUF19: Hugging Face mira la foto y devuelve una breve descripción en español
    String describirImagen(String fotoBase64);

    // HUF17: compara el texto de búsqueda con los objetos activos y los ordena por similitud
    List<ObjetoSimilarDTO> buscarSimilares(String texto, int top);
}
