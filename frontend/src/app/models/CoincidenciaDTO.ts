// Lo que devuelve GET /api/Coincidencias
export class CoincidenciaDTO {
    idCoincidencia: number = 0;
    porcentajeSimilitudC: number = 0;
    detalleC: string = '';
    fechaGeneracionC: string = '';
    // Campos opcionales por si amplías el DTO en backend para mostrar relaciones
    idReportePerdido?: number;
    idReporteEncontrado?: number;
    idEstadoCoincidencia?: number;
    nombreEstadoCoincidencia?: string;
}

// Lo que se envía en POST /api/Coincidencias (CoincidenciaDTCinsert)
export class CoincidenciaInsertDTO {
    idCoincidencia?: number;
    idReportePerdido: number = 0;
    idReporteEncontrado: number = 0;
    idEstadoCoincidencia: number = 0;
    porcentajeSimilitudC: number = 0;
    detalleC: string = '';
    fechaGeneracionC: string = '';
}