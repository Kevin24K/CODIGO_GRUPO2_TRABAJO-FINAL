export class ReporteDTO {
    idReporte: number = 0;
    tipoReporte: string = '';
    fechaeventoR: string = '';        // LocalDate  -> "2026-11-05"
    horaEventoR: string = '';         // LocalTime  -> "14:30:00"
    fechaCreacion: string = '';       // LocalDateTime -> "2026-11-05T14:30:00"
    fechaActualizacionR: string = '';
    idUsuario: number = 0;
    idObjeto: number = 0;
    idEstadoReporte: number = 0;
}
