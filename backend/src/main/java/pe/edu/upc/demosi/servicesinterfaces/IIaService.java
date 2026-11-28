package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.dtos.ObjetoSimilarDTO;
import pe.edu.upc.demosi.entities.Objeto;

import java.util.List;

// HUF17 / HUF19: servicio de IA (Gemini + Smile)
public interface IIaService {

    // HUF19: Gemini mira la foto y devuelve una breve descripción en español
    String describirImagen(String fotoBase64);

    // HUF17: compara el texto de búsqueda con los objetos activos y los ordena por similitud
    List<ObjetoSimilarDTO> buscarSimilares(String texto, int top);

    // Notificaciones: compara un objeto con varios (perdido vs encontrados o al revés).
    // porcentajes[i] (0 a 100) corresponde a otros.get(i)
    Resultado compararObjetos(Objeto ancla, List<Objeto> otros, boolean anclaEsPerdido);

    record Resultado(double[] porcentajes, String metodo) {}
}
