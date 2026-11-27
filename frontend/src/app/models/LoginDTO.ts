export class LoginRequestDTO {
    correo: string = '';
    contrasena: string = '';
}

export class LoginResponseDTO {
    token: string = '';
    correo: string = '';
}
