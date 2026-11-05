import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequestDTO, LoginResponseDTO } from '../models/LoginDTO';

@Injectable({
  providedIn: 'root',
})
export class Loginservice {

  private apiURL = `${environment.base}/login`

  constructor(private http: HttpClient) {}

  login(request: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(this.apiURL, request).pipe(
      tap(resp => {
        localStorage.setItem('token', resp.token)
        localStorage.setItem('correo', resp.correo)
      })
    )
  }

  logout() {
    if (this.hayStorage()) {
      localStorage.removeItem('token')
      localStorage.removeItem('correo')
    }
  }

  getToken(): string | null {
    return this.hayStorage() ? localStorage.getItem('token') : null
  }

  getCorreo(): string {
    return this.hayStorage() ? (localStorage.getItem('correo') ?? '') : ''
  }

  // Hay sesión si existe un token y todavía no venció
  estaLogueado(): boolean {
    const token = this.getToken()
    if (!token) return false
    const payload = this.leerPayload(token)
    if (payload?.exp && payload.exp * 1000 < Date.now()) {
      this.logout()
      return false
    }
    return true
  }

  // Los roles vienen en el claim "roles" del JWT, ej: "ROLE_ADMIN"
  esAdmin(): boolean {
    const token = this.getToken()
    if (!token) return false
    const roles: string = this.leerPayload(token)?.roles ?? ''
    return roles.split(',').includes('ROLE_ADMIN')
  }

  private leerPayload(token: string): any {
    try {
      const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
      return JSON.parse(atob(base64))
    } catch {
      return null
    }
  }

  // localStorage no existe cuando Angular renderiza en el servidor (SSR)
  private hayStorage(): boolean {
    return typeof localStorage !== 'undefined'
  }
}
