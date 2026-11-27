import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CoincidenciaDTO, CoincidenciaInsertDTO } from '../models/CoincidenciaDTO';
import { SumaSimilitudEstadoDTO } from '../models/SumaSimilitudEstadoDTO';

@Injectable({
  providedIn: 'root',
})
export class Coincidenciaservice {
  private apiURL = `${environment.base}/api/Coincidencias`;

  constructor(private http: HttpClient) {}

  getCoincidencias(): Observable<CoincidenciaDTO[]> {
    return this.http.get<CoincidenciaDTO[]>(this.apiURL);
  }

  getById(id: number): Observable<CoincidenciaDTO> {
    return this.http.get<CoincidenciaDTO>(`${this.apiURL}/${id}`);
  }

  insert(dto: CoincidenciaInsertDTO): Observable<CoincidenciaInsertDTO> {
    return this.http.post<CoincidenciaInsertDTO>(this.apiURL, dto);
  }

  update(dto: CoincidenciaDTO): Observable<CoincidenciaDTO> {
    return this.http.put<CoincidenciaDTO>(this.apiURL, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  obtenerSumaSimilitudPorEstado(): Observable<SumaSimilitudEstadoDTO[]> {
    return this.http.get<SumaSimilitudEstadoDTO[]>(`${this.apiURL}/suma-similitud-estado`);
  }
}