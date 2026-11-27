import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RolDTO } from '../models/RolDTO';

@Injectable({
  providedIn: 'root',
})
export class Rolservice {

  private apiURL = `${environment.base}/api/Roles`

  constructor(private http: HttpClient) {}

  getRoles(): Observable<RolDTO[]> {
    return this.http.get<RolDTO[]>(this.apiURL)
  }
}
