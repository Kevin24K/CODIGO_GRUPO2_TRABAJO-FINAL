import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ObjetoDTO, ObjetoInsertDTO } from '../models/ObjetoDTO';

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
}
