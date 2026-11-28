package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Reporte;

// Notificaciones: busca coincidencias entre reportes PERDIDO y ENCONTRADO
// y avisa al dueño del objeto perdido cuando la similitud llega al umbral (80 %).
public interface ICoincidenciaAutomaticaService {

    // Se llama al registrar un reporte. Devuelve cuántas coincidencias nuevas se crearon.
    int procesarReporte(Reporte reporte);

    // Se llama cuando cambia la foto (y la descripción IA) de un objeto.
    int procesarObjeto(Long idObjeto);
}
