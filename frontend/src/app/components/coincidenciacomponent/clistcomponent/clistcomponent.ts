import { Component, OnInit, computed, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CoincidenciaDTO } from '../../../models/CoincidenciaDTO';
import { EstadoCoincidenciaDTO } from '../../..//models/EstadoCoincidenciaDTO';
import { Coincidenciaservice } from '../../../services/coincidenciaservice';
import { Estadocoincidenciaservice } from '../../../services/estadocoincidenciaservice';

@Component({
  selector: 'app-clistcomponent',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule,
    RouterLink,
    DatePipe,
  ],
  templateUrl: './clistcomponent.html',
  styleUrl: './clistcomponent.css',
})
export class Clistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6'];
  dataSource: MatTableDataSource<CoincidenciaDTO> = new MatTableDataSource<CoincidenciaDTO>();

  todas = signal<CoincidenciaDTO[]>([]);
  estados = signal<EstadoCoincidenciaDTO[]>([]);
  soloPendientes = signal(false);

  pendientesCount = computed(() =>
    this.todas().filter(c => this.esPendiente(c)).length
  );

  constructor(
    private cS: Coincidenciaservice,
    private ecS: Estadocoincidenciaservice,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.ecS.getEstados().subscribe({
      next: data => this.estados.set(data),
      error: () => {},
    });
    this.obtenerCoincidencias();
  }

  obtenerCoincidencias() {
    this.cS.getCoincidencias().subscribe({
      next: data => {
        this.todas.set(data);
        this.aplicarFiltro();
      },
      error: (err) => {
        this.snack.open(
          err.status === 403
            ? 'Solo un ADMIN puede gestionar coincidencias'
            : 'Ocurrió un error al listar las coincidencias',
          'OK',
          { duration: 3000 }
        );
      },
    });
  }

  toggleSoloPendientes() {
    this.soloPendientes.set(!this.soloPendientes());
    this.aplicarFiltro();
  }

  private aplicarFiltro() {
    const lista = this.todas();
    this.dataSource.data = this.soloPendientes()
      ? lista.filter(c => this.esPendiente(c))
      : lista;
  }

  private esPendiente(c: CoincidenciaDTO): boolean {
    const estado = (c.nombreEstadoCoincidencia ?? '').toUpperCase();
    return estado === '' || estado.includes('PEND');
  }

  cambiarEstado(c: CoincidenciaDTO, nuevoIdEstado: number) {
    if (c.idEstadoCoincidencia === nuevoIdEstado) return;
    const estadoObj = this.estados().find(e => e.idEstadoCoincidencia === nuevoIdEstado);
    if (!confirm(`¿Cambiar el estado de la coincidencia #${c.idCoincidencia} a "${estadoObj?.nombreEC}"?`)) {
      this.aplicarFiltro();
      return;
    }
    const actualizado: CoincidenciaDTO = {
      ...c,
      idEstadoCoincidencia: nuevoIdEstado,
      nombreEstadoCoincidencia: estadoObj?.nombreEC,
    };
    this.cS.update(actualizado).subscribe({
      next: () => {
        this.snack.open('Estado de coincidencia actualizado', 'OK', { duration: 2500 });
        this.obtenerCoincidencias();
      },
      error: () => {
        this.snack.open('No se pudo actualizar el estado de la coincidencia', 'OK', { duration: 4000 });
        this.obtenerCoincidencias();
      },
    });
  }

  eliminarCoincidencia(c: CoincidenciaDTO) {
    if (!confirm(`¿Eliminar la coincidencia #${c.idCoincidencia}?`)) return;
    this.cS.delete(c.idCoincidencia).subscribe({
      next: () => {
        this.snack.open('Coincidencia eliminada', 'OK', { duration: 2500 });
        this.obtenerCoincidencias();
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener notificaciones asociadas', 'OK', { duration: 4000 });
      },
    });
  }
}