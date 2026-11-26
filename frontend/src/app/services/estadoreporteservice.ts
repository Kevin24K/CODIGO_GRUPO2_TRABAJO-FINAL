import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EstadoReporteDTO } from '../models/EstadoReporteDTO';

@Injectable({
  providedIn: 'root',
})
export class Estadoreporteservice {

  private apiURL = `${environment.base}/api/EstadoReporte`

  constructor(private http: HttpClient) {}

  getEstados(): Observable<EstadoReporteDTO[]> {
    return this.http.get<EstadoReporteDTO[]>(this.apiURL)
  }
}
