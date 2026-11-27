import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioDTO, UsuarioInsertDTO } from '../models/UsuarioDTO';

@Injectable({
  providedIn: 'root',
})
export class Usuarioservice {

  private apiURL = `${environment.base}/api/Usuarios`

  constructor(private http: HttpClient) {}

  getUsuarios(): Observable<UsuarioDTO[]> {
    return this.http.get<UsuarioDTO[]>(this.apiURL)
  }

  insert(dto: UsuarioInsertDTO): Observable<UsuarioInsertDTO> {
    return this.http.post<UsuarioInsertDTO>(this.apiURL, dto)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
  }
}
