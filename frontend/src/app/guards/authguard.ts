import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Loginservice } from '../services/loginservice';

export const authGuard: CanActivateFn = () => {
  const lS = inject(Loginservice);
  const router = inject(Router);
  return lS.estaLogueado() ? true : router.createUrlTree(['/login']);
};

export const adminGuard: CanActivateFn = () => {
  const lS = inject(Loginservice);
  const router = inject(Router);
  return lS.esAdmin() ? true : router.createUrlTree(['/']);
};

export const adminStaffGuard: CanActivateFn = () => {
  const lS = inject(Loginservice);
  const router = inject(Router);
  return lS.esAdminOStaff() ? true : router.createUrlTree(['/']);
};