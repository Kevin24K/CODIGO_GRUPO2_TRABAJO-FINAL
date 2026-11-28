import { Component, OnInit, computed, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NotificacionDetalleDTO } from '../../../models/NotificacionDTO';
import { Notificacionservice } from '../../../services/notificacionservice';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Loginservice } from '../../../services/loginservice';

@Component({
  selector: 'app-nlistcomponent',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    RouterLink,
    DatePipe,
  ],
  templateUrl: './nlistcomponent.html',
  styleUrl: './nlistcomponent.css',
})
export class Nlistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7', 'c8', 'c9'];
  dataSource: MatTableDataSource<NotificacionDetalleDTO> = new MatTableDataSource<NotificacionDetalleDTO>();

  todas = signal<NotificacionDetalleDTO[]>([]);
  usuarios = signal(new Map<number, string>());
  soloNoLeidas = signal(false);

  noLeidasCount = computed(() =>
    this.todas().filter(n => !n.leidaNotificacion).length
  );

  constructor(
    private nS: Notificacionservice,
    private uS: Usuarioservice,
    public lS: Loginservice,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.obtenerNotificaciones();
    if (this.lS.esAdmin()) {
      this.uS.getUsuarios().subscribe({
        next: data =>
          this.usuarios.set(new Map(data.map(u => [u.idUsuario, `${u.nombre} ${u.apellido}`]))),
        error: () => {},
      });
    }
  }

  obtenerNotificaciones() {
    this.nS.getNotificaciones().subscribe({
      next: data => {
        this.todas.set(data);
        this.aplicarFiltro();
      },
      error: () => {
        this.snack.open('Ocurrió un error al listar las notificaciones', 'OK', { duration: 3000 });
      },
    });
  }

  toggleSoloNoLeidas() {
    this.soloNoLeidas.set(!this.soloNoLeidas());
    this.aplicarFiltro();
  }

  private aplicarFiltro() {
    const lista = this.todas();
    this.dataSource.data = this.soloNoLeidas()
      ? lista.filter(n => !n.leidaNotificacion)
      : lista;
  }

  alternarLeida(n: NotificacionDetalleDTO) {
    const actualizada: NotificacionDetalleDTO = {
      ...n,
      leidaNotificacion: !n.leidaNotificacion,
    };

    this.nS.update(actualizada).subscribe({
      next: () => {
        this.snack.open(
          actualizada.leidaNotificacion ? 'Marcada como leída' : 'Marcada como no leída',
          'OK',
          { duration: 2000 }
        );
        this.obtenerNotificaciones();
      },
      error: () => {
        this.snack.open('No se pudo actualizar el estado de la notificación', 'OK', { duration: 3500 });
      },
    });
  }

  eliminarNotificacion(n: NotificacionDetalleDTO) {
    if (!confirm(`¿Eliminar la notificación "${n.tituloNotificacion}"?`)) return;
    this.nS.delete(n.idNotificacion).subscribe({
      next: () => {
        this.snack.open('Notificación eliminada', 'OK', { duration: 2500 });
        this.obtenerNotificaciones();
      },
      error: () => {
        this.snack.open('Ocurrió un error al eliminar la notificación', 'OK', { duration: 4000 });
      },
    });
  }
}