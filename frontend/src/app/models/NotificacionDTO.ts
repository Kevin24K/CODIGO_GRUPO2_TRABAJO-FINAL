// Notificaciones: respuesta de GET /api/Notificaciones/mias
export class NotificacionDetalleDTO {
    idNotificacion: number = 0;
    tituloNotificacion: string = '';
    mensajeNotificacion: string = '';
    tipoNotificacion: string = '';
    leidaNotificacion: boolean = false;
    fechaCreacionN: string = '';
    idCoincidencia: number = 0;
    porcentaje: number = 0;
    estadoCoincidencia: string = '';
    idReportePerdido: number = 0;
    objetoPerdido: string = '';
    idReporteEncontrado: number = 0;
    idObjetoEncontrado: number = 0;
    objetoEncontrado: string = '';
    categoriaEncontrado: string = '';
    descripcionEncontrado: string = '';
    descripcionIaEncontrado: string = '';
    colorEncontrado: string = '';
    marcaEncontrado: string = '';
    fotoEncontrado: string = '';
    fechaEncontrado: string = '';
}

// Lo que se envía en POST /api/Notificaciones (NotificacionDTCinsert)
export class NotificacionInsertDTO {
    idNotificacion?: number;
    tituloNotificacion: string = '';
    mensajeNotificacion: string = '';
    tipoNotificacion: string = '';
    leidaNotificacion: boolean = false;
    fechaCreacionN: string = '';
    idUsuario: number = 0;
    idCoincidencia: number = 0;
    porcentaje: number = 0;
    estadoCoincidencia: string = '';
    idReportePerdido: number = 0;
    objetoPerdido: string = '';
    idReporteEncontrado: number = 0;
    idObjetoEncontrado: number = 0;
    objetoEncontrado: string = '';
    categoriaEncontrado: string = '';
    descripcionEncontrado: string = '';
    descripcionIaEncontrado: string = '';
    colorEncontrado: string = '';
    marcaEncontrado: string = '';
    fotoEncontrado: string = '';
    fechaEncontrado: string = '';
}
