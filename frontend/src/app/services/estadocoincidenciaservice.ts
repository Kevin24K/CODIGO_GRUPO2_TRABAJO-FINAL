import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EstadoCoincidenciaDTO } from '../models/EstadoCoincidenciaDTO';

@Injectable({
  providedIn: 'root',
})
export class Estadocoincidenciaservice {
  private apiURL = `${environment.base}/api/EstadoCoincidencia`;

  constructor(private http: HttpClient) {}

  getEstados(): Observable<EstadoCoincidenciaDTO[]> {
    return this.http.get<EstadoCoincidenciaDTO[]>(this.apiURL);
  }
}