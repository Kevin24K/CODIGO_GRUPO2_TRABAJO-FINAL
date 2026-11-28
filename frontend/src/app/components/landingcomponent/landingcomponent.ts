import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Loginservice } from '../../services/loginservice';

// Página pública de presentación (ruta "/"). Desde aquí se entra al login,
// a reportar un objeto o a buscar uno.
@Component({
  selector: 'app-landingcomponent',
  imports: [RouterLink],
  templateUrl: './landingcomponent.html',
  styleUrl: './landingcomponent.css',
})
export class Landingcomponent {

  logueado: boolean

  constructor(lS: Loginservice) {
    // Solo cuenta como sesión si hay token guardado
    this.logueado = !!lS.getToken() && lS.estaLogueado()
  }

  // Desplazamiento suave a cada sección sin cambiar de ruta
  ir(e: Event, id: string) {
    e.preventDefault()
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}
