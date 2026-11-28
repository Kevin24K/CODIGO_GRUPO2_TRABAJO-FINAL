import { Component ,OnInit,signal} from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatTooltipModule } from '@angular/material/tooltip';
import {NotificacionDetalleDTO} from '../../models/NotificacionDTO';
import {Notificacionservice} from '../../services/notificacionservice';
import { Pushservice } from '../../services/pushservice';
import { Loginservice } from '../../services/loginservice';

@Component({
  selector: 'app-menucomponent',
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, MatBadgeModule, MatTooltipModule, RouterLink],
  templateUrl: './menucomponent.html',
  styleUrl: './menucomponent.css',
})
export class Menucomponent implements OnInit {

  ultimas = signal<NotificacionDetalleDTO[]>([])

  constructor(public lS: Loginservice, public nS: Notificacionservice, private pS: Pushservice, private router: Router) {}

  ngOnInit(): void {
    this.nS.iniciarRevision()
    this.pS.iniciar() // Firebase: push si el usuario ya dio permiso
  }

  // Al abrir la campanita se cargan las 5 más recientes
  cargarUltimas() {
    this.nS.getMias().subscribe({ next: data => this.ultimas.set(data.slice(0, 5)), error: () => this.ultimas.set([]) })
  }

  abrir(n: NotificacionDetalleDTO) {
    if (!n.leidaNotificacion) {
      this.nS.marcarLeida(n.idNotificacion).subscribe()
    }
    this.router.navigate(['/notificaciones'])
  }

  cerrarSesion() {
    this.pS.desactivar()
    this.lS.logout()
    this.router.navigate(['/login'])
  }
}
