import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../environments/environment';
import { Loginservice } from './loginservice';
import { Notificacionservice } from './notificacionservice';

export type EstadoPush = 'activo' | 'inactivo' | 'denegado' | 'no-soportado' | 'sin-config' | 'error'

// Notificaciones push con Firebase Cloud Messaging (FCM)
@Injectable({
  providedIn: 'root',
})
export class Pushservice {

  private apiURL = `${environment.base}/api/Notificaciones`
  estado = signal<EstadoPush>('inactivo')
  private escuchando = false

  constructor(private http: HttpClient, private lS: Loginservice, private nS: Notificacionservice) {}

  private hayNavegador(): boolean {
    return typeof window !== 'undefined' && 'serviceWorker' in navigator && 'Notification' in window
  }

  private configurado(): boolean {
    const f = environment.firebase
    return !!(f && f.apiKey && f.projectId && f.messagingSenderId && f.appId && f.vapidKey)
  }

  // Se llama al cargar el menú: si ya dio permiso antes, renueva el token sin preguntar
  iniciar() {
    if (!this.hayNavegador()) { this.estado.set('no-soportado'); return }
    if (!this.configurado()) { this.estado.set('sin-config'); return }
    this.escucharMensajes()
    if (Notification.permission === 'granted') {
      this.activar(false)
    } else if (Notification.permission === 'denied') {
      this.estado.set('denegado')
    }
  }

  // Pide permiso (si hace falta), obtiene el token de Firebase y lo guarda en el backend
  async activar(pedirPermiso = true): Promise<EstadoPush> {
    if (!this.hayNavegador()) return this.fijar('no-soportado')
    if (!this.configurado()) return this.fijar('sin-config')
    try {
      const permiso = pedirPermiso ? await Notification.requestPermission() : Notification.permission
      if (permiso !== 'granted') return this.fijar(permiso === 'denied' ? 'denegado' : 'inactivo')

      // Se cargan solo en el navegador (no en el servidor)
      const { initializeApp, getApps } = await import('firebase/app')
      const { getMessaging, getToken, isSupported } = await import('firebase/messaging')
      if (!(await isSupported())) return this.fijar('no-soportado')

      const registro = await navigator.serviceWorker.register('/firebase-messaging-sw.js')
      const { vapidKey, ...config } = environment.firebase
      const app = getApps()[0] ?? initializeApp(config)
      const token = await getToken(getMessaging(app), { vapidKey, serviceWorkerRegistration: registro })
      if (!token) return this.fijar('error')

      const params = new HttpParams().set('correo', this.lS.getCorreo())
      await firstValueFrom(this.http.post(`${this.apiURL}/push-token`, { token }, { params }))
      localStorage.setItem('fcmToken', token)
      return this.fijar('activo')
    } catch (e) {
      console.warn('No se pudieron activar las notificaciones push', e)
      return this.fijar('error')
    }
  }

  // Al cerrar sesión: este navegador deja de recibir push de ese usuario
  desactivar() {
    if (typeof localStorage === 'undefined') return
    const token = localStorage.getItem('fcmToken')
    if (!token) return
    localStorage.removeItem('fcmToken')
    this.http.delete(`${this.apiURL}/push-token`, { params: new HttpParams().set('token', token) })
      .subscribe({ error: () => {} })
    this.estado.set('inactivo')
  }

  // El service worker avisa cuando llega un push -> se actualiza la campanita
  private escucharMensajes() {
    if (this.escuchando) return
    this.escuchando = true
    navigator.serviceWorker.addEventListener('message', (ev: MessageEvent) => {
      if (ev.data?.tipo === 'lf-push') this.nS.refrescar()
    })
  }

  private fijar(e: EstadoPush): EstadoPush {
    this.estado.set(e)
    return e
  }
}
