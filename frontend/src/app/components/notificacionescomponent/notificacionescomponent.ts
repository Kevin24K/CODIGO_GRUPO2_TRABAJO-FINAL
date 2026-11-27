import { Component, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialog } from '@angular/material/dialog';
import { Menucomponent } from '../menucomponent/menucomponent';
import { Notificacionservice } from '../../services/notificacionservice';
import { Pushservice } from '../../services/pushservice';
import { NotificacionDetalleDTO } from '../../models/NotificacionDTO';
import { ObjetoDTO } from '../../models/ObjetoDTO';
import { Fotodialogcomponent } from '../fotodialogcomponent/fotodialogcomponent';

@Component({
  selector: 'app-notificacionescomponent',
  imports: [Menucomponent, RouterLink, DatePipe, MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './notificacionescomponent.html',
  styleUrl: './notificacionescomponent.css',
})
export class Notificacionescomponent implements OnInit {

  lista = signal<NotificacionDetalleDTO[]>([])
  cargando = signal(true)
  error = signal('')

  constructor(public nS: Notificacionservice, public pS: Pushservice, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.cargar()
  }

  cargar() {
    this.cargando.set(true)
    this.nS.getMias().subscribe({
      next: data => {
        this.lista.set(data)
        this.cargando.set(false)
        this.error.set('')
      },
      error: () => {
        this.cargando.set(false)
        this.error.set('No se pudieron cargar tus notificaciones. ¿Está corriendo el backend?')
      },
    })
  }

  activando = signal(false)

  async activarPush() {
    this.activando.set(true)
    await this.pS.activar()
    this.activando.set(false)
  }

  marcarLeida(n: NotificacionDetalleDTO) {
    this.nS.marcarLeida(n.idNotificacion).subscribe(() =>
      this.lista.update(l => l.map(x => x.idNotificacion === n.idNotificacion ? { ...x, leidaNotificacion: true } : x)))
  }

  marcarTodas() {
    this.nS.marcarTodas().subscribe(() =>
      this.lista.update(l => l.map(x => ({ ...x, leidaNotificacion: true }))))
  }

  hayNoLeidas(): boolean {
    return this.lista().some(n => !n.leidaNotificacion)
  }

  verFoto(n: NotificacionDetalleDTO) {
    const o = { ...new ObjetoDTO(), idObjeto: n.idObjetoEncontrado, nombreObjeto: n.objetoEncontrado,
      fotoObjeto: n.fotoEncontrado, descripcionIaObjeto: n.descripcionIaEncontrado }
    this.dialog.open(Fotodialogcomponent, { data: { objeto: o, modo: 'ver' }, width: '560px', maxWidth: '95vw' })
  }
}
