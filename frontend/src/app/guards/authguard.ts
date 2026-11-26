import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Loginservice } from '../services/loginservice';

// Solo deja entrar si hay sesión iniciada
export const authGuard: CanActivateFn = () => {
  const lS = inject(Loginservice)
  const router = inject(Router)
  return lS.estaLogueado() ? true : router.createUrlTree(['/login'])
}

// Solo deja entrar si el usuario tiene ROLE_ADMIN
export const adminGuard: CanActivateFn = () => {
  const lS = inject(Loginservice)
  const router = inject(Router)
  return lS.esAdmin() ? true : router.createUrlTree(['/'])
}
