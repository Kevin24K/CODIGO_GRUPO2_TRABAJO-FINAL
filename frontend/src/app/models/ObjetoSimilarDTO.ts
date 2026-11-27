// HUF17: respuesta de POST /api/Objetos/buscar-similares
export class ObjetoSimilarDTO {
    idObjeto: number = 0;
    nombreObjeto: string = '';
    categoria: string = '';
    descripcionObjeto: string = '';
    descripcionIaObjeto: string = '';
    colorObjeto: string = '';
    marcaObjeto: string = '';
    fotoObjeto: string = '';
    porcentaje: number = 0;   // 0 a 100
    metodo: string = '';      // "IA (Gemini + Smile)" o "Texto (sin IA)"
}
