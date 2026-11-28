import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { NotificacionDetalleDTO } from '../models/NotificacionDTO';
import { NotificacionInsertDTO } from '../models/NotificacionDTO';
import { Loginservice } from './loginservice';

@Injectable({
  providedIn: 'root',
})
export class Notificacionservice {

  private apiURL = `${environment.base}/api/Notificaciones`

  // Cantidad de no leídas que muestra la campanita del menú
  noLeidas = signal(0)
  private timer: ReturnType<typeof setInterval> | null = null

  constructor(private http: HttpClient, private lS: Loginservice) {}

  // El backend toma el correo del token; el parámetro sirve si la seguridad está apagada
  private params(): HttpParams {
    return new HttpParams().set('correo', this.lS.getCorreo())
  }
  insert(dto: NotificacionInsertDTO): Observable<NotificacionInsertDTO> {
      return this.http.post<NotificacionInsertDTO>(this.apiURL, dto)
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
    update(dto: NotificacionDetalleDTO): Observable<NotificacionDetalleDTO> {
    return this.http.put<NotificacionDetalleDTO>(this.apiURL, dto);
  }
    getNotificaciones(): Observable<NotificacionDetalleDTO[]> {
    return this.http.get<NotificacionDetalleDTO[]>(this.apiURL);
  }
  getMias(): Observable<NotificacionDetalleDTO[]> {
    return this.http.get<NotificacionDetalleDTO[]>(`${this.apiURL}/mias`, { params: this.params() }).pipe(
      tap(lista => this.noLeidas.set(lista.filter(n => !n.leidaNotificacion).length))
    )
  }

  marcarLeida(id: number): Observable<NotificacionDetalleDTO> {
    return this.http.put<NotificacionDetalleDTO>(`${this.apiURL}/${id}/leida`, {}).pipe(
      tap(() => this.noLeidas.update(n => Math.max(0, n - 1)))
    )
  }

  marcarTodas(): Observable<{ cantidad: number }> {
    return this.http.put<{ cantidad: number }>(`${this.apiURL}/mias/leidas`, {}, { params: this.params() }).pipe(
      tap(() => this.noLeidas.set(0))
    )
  }

  // Vuelve a contar las no leídas (por ejemplo, después de registrar un reporte)
  refrescar() {
    if (!this.lS.getCorreo()) return
    this.http.get<{ cantidad: number }>(`${this.apiURL}/mias/no-leidas`, { params: this.params() })
      .subscribe({ next: r => this.noLeidas.set(r.cantidad), error: () => {} })
  }

  // Revisa cada 30 segundos mientras la app está abierta
  iniciarRevision() {
    this.refrescar()
    if (this.timer || typeof window === 'undefined') return
    this.timer = setInterval(() => this.refrescar(), 30000)
  }
}
