import { CategoriaDTO } from './CategoriaDTO';

// Lo que devuelve GET /api/Objetos (trae la categoría completa)
export class ObjetoDTO {
    idObjeto: number = 0;
    nombreObjeto: string = '';
    descripcionObjeto: string = '';
    colorObjeto: string = '';
    marcaObjeto: string = '';
    activoObjeto: boolean = true;
    categoria: CategoriaDTO = new CategoriaDTO();
}

// Lo que se envía en POST /api/Objetos (solo el id de la categoría)
export class ObjetoInsertDTO {
    nombreObjeto: string = '';
    descripcionObjeto: string = '';
    colorObjeto: string = '';
    marcaObjeto: string = '';
    activoObjeto: boolean = true;
    idCategoria: number = 0;
}
