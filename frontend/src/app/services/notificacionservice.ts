import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { NotificacionDTO, NotificacionInsertDTO } from '../models/NotificacionDTO';

@Injectable({
  providedIn: 'root',
})
export class Notificacionservice {
  private apiURL = `${environment.base}/api/Notificaciones`;

  constructor(private http: HttpClient) {}

  getNotificaciones(): Observable<NotificacionDTO[]> {
    return this.http.get<NotificacionDTO[]>(this.apiURL);
  }

  insert(dto: NotificacionInsertDTO): Observable<NotificacionInsertDTO> {
    return this.http.post<NotificacionInsertDTO>(this.apiURL, dto);
  }

  update(dto: NotificacionDTO): Observable<NotificacionDTO> {
    return this.http.put<NotificacionDTO>(this.apiURL, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
}