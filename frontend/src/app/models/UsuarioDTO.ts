// Lo que devuelve GET /api/Usuarios
export class UsuarioDTO {
    idUsuario: number = 0;
    nombre: string = '';
    apellido: string = '';
    correo: string = '';
    fechaRegistro: string = '';
    activo: boolean = true;
    idRol: number = 0;
    nombreRol: string = '';
}

// Lo que se envía en POST /api/Usuarios
export class UsuarioInsertDTO {
    nombre: string = '';
    apellido: string = '';
    correo: string = '';
    contrasena: string = '';
    idRol: number = 0;
}
