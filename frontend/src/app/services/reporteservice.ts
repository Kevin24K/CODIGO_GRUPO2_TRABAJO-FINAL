import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ReporteDTO } from '../models/ReporteDTO';
import { TotalDTO } from './TotalDTO';

@Injectable({
  providedIn: 'root',
})
export class Reporteservice {

  private apiURL = `${environment.base}/api/Reportes`

  constructor(private http: HttpClient) {}

  getReportes(): Observable<ReporteDTO[]> {
    return this.http.get<ReporteDTO[]>(this.apiURL);
  }

  getReportesActivos(): Observable<ReporteDTO[]> {
    return this.http.get<ReporteDTO[]>(`${this.apiURL}/activos`);
  }

  listarReportesPorTipo(tipo?: string): Observable<ReporteDTO[]> {
    const filtro = (tipo ?? 'TODOS').trim().toUpperCase();
    if (!filtro || filtro === 'TODOS') {
      return this.getReportesActivos();
    }
    const params = new HttpParams().set('tipo', filtro);
    return this.http.get<ReporteDTO[]>(`${this.apiURL}/por-tipo`, { params });
  }

  insert(dto: ReporteDTO): Observable<ReporteDTO> {
    return this.http.post<ReporteDTO>(this.apiURL, dto)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
  }
  getTotalByCategory(): Observable<TotalDTO[]> {
    return this.http.get<TotalDTO[]>(`${this.apiURL}/total-category`);
  }
}
