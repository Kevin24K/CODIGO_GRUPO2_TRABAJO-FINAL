import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ReporteDTO } from '../models/ReporteDTO';

@Injectable({
  providedIn: 'root',
})
export class Reporteservice {

  private apiURL = `${environment.base}/api/Reportes`

  constructor(private http: HttpClient) {}

  getReportes(): Observable<ReporteDTO[]> {
    return this.http.get<ReporteDTO[]>(this.apiURL)
  }

  insert(dto: ReporteDTO): Observable<ReporteDTO> {
    return this.http.post<ReporteDTO>(this.apiURL, dto)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
  }
}
