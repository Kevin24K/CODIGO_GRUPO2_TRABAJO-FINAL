// Lo que devuelve GET /api/Notificaciones y se envía en PUT /api/Notificaciones
export class NotificacionDTO {
    idNotificacion: number = 0;
    tituloNotificacion: string = '';
    mensajeNotificacion: string = '';
    tipoNotificacion: string = '';
    leidaNotificacion: boolean = false;
    fechaCreacionN: string = ''; // LocalDateTime -> "2026-11-26T19:45:00"
    idUsuario: number = 0;
    idCoincidencia: number = 0;
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
}