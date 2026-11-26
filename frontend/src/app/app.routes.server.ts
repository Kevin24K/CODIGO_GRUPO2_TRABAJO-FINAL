import { RenderMode, ServerRoute } from '@angular/ssr';

// Todo se renderiza en el navegador: el token JWT vive en localStorage,
// que no existe en el servidor.
export const serverRoutes: ServerRoute[] = [
  {
    path: '**',
    renderMode: RenderMode.Client
  }
];
