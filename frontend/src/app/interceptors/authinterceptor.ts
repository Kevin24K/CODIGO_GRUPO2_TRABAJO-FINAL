import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { Loginservice } from '../services/loginservice';

// Agrega "Authorization: Bearer <token>" a todas las peticiones al backend
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const lS = inject(Loginservice)
  const router = inject(Router)
  const token = lS.getToken()

  const request = token && !req.url.endsWith('/login')
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req

  return next(request).pipe(
    catchError((err: HttpErrorResponse) => {
      // 401 = token vencido o inválido -> volver al login
      if (err.status === 401 && !req.url.endsWith('/login')) {
        lS.logout()
        router.navigate(['/login'])
      }
      return throwError(() => err)
    })
  )
}
