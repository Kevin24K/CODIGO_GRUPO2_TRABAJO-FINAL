import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CategoriaDTO } from '../models/CategoriaDTO';

@Injectable({
  providedIn: 'root',
})
export class Categoriaservice {

  private apiURL = `${environment.base}/api/Categorias`

  constructor(private http: HttpClient) {}

  getCategorias(): Observable<CategoriaDTO[]> {
    return this.http.get<CategoriaDTO[]>(this.apiURL)
  }

  insert(dto: CategoriaDTO): Observable<CategoriaDTO> {
    return this.http.post<CategoriaDTO>(this.apiURL, dto)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`)
  }
}
