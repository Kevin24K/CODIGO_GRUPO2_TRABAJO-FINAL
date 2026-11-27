import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ObjetoDTO, ObjetoFotoDTO, ObjetoInsertDTO } from '../models/ObjetoDTO';
import { ObjetoSimilarDTO } from '../models/ObjetoSimilarDTO';

@Injectable({
  providedIn: 'root',
})
export class Objetoservice {

  private apiURL = `${environment.base}/api/Objetos`

  constructor(private http: HttpClient) {}

  getObjetos(): Observable<ObjetoDTO[]> {
    return this.http.get<ObjetoDTO[]>(this.apiURL)
  }

  insert(dto: ObjetoInsertDTO): Observable<ObjetoInsertDTO> {
    return this.http.post<ObjetoInsertDTO>(this.apiURL, dto)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
  }

  // HUF19: agregar o cambiar la foto de un objeto ya registrado
  updateFoto(id: number, dto: ObjetoFotoDTO): Observable<ObjetoDTO> {
    return this.http.put<ObjetoDTO>(`${this.apiURL}/${id}/foto`, dto)
  }

  // HUF17: objetos parecidos a lo que escribe quien perdió algo
  buscarSimilares(texto: string, top: number = 8): Observable<ObjetoSimilarDTO[]> {
    return this.http.post<ObjetoSimilarDTO[]>(`${this.apiURL}/buscar-similares`, { texto, top })
  }
}
