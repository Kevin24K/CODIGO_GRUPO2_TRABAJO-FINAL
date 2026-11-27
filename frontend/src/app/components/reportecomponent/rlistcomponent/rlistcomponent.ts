import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ReporteDTO } from '../../../models/ReporteDTO';
import { Reporteservice } from '../../../services/reporteservice';
import { Objetoservice } from '../../../services/objetoservice';
import { Estadoreporteservice } from '../../../services/estadoreporteservice';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Loginservice } from '../../../services/loginservice';

export type TipoFiltroReporte = 'TODOS' | 'PERDIDO' | 'ENCONTRADO';

@Component({
  selector: 'app-rlistcomponent',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatButtonToggleModule,
    RouterLink,
  ],
  templateUrl: './rlistcomponent.html',
  styleUrl: './rlistcomponent.css',
})
export class Rlistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7'];
  dataSource: MatTableDataSource<ReporteDTO> = new MatTableDataSource<ReporteDTO>();

  tipoSeleccionado = signal<TipoFiltroReporte>('TODOS');
  cargando = signal<boolean>(false);

  objetos = signal(new Map<number, string>());
  estados = signal(new Map<number, string>());
  usuarios = signal(new Map<number, string>());

  constructor(
    private rS: Reporteservice,
    private oS: Objetoservice,
    private eS: Estadoreporteservice,
    private uS: Usuarioservice,
    public lS: Loginservice,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargarReportesPorTipo(this.tipoSeleccionado());

    this.oS.getObjetos().subscribe({
      next: data => this.objetos.set(new Map(data.map(o => [o.idObjeto, o.nombreObjeto]))),
      error: () => {},
    });

    this.eS.getEstados().subscribe({
      next: data => this.estados.set(new Map(data.map(e => [e.idEstadoReporte, e.nombreEReporte]))),
      error: () => {},
    });

    if (this.lS.esAdmin()) {
      this.uS.getUsuarios().subscribe({
        next: data =>
          this.usuarios.set(new Map(data.map(u => [u.idUsuario, `${u.nombre} ${u.apellido}`]))),
        error: () => {},
      });
    }
  }

  onCambiarTipo(nuevoTipo: TipoFiltroReporte): void {
    if (!nuevoTipo) return;
    this.tipoSeleccionado.set(nuevoTipo);
    this.cargarReportesPorTipo(nuevoTipo);
  }

  cargarReportesPorTipo(tipo: TipoFiltroReporte = this.tipoSeleccionado()): void {
    this.cargando.set(true);
    this.rS.listarReportesPorTipo(tipo).subscribe({
      next: data => {
        const listaActivos = (data ?? []).filter(r => (r as any).activo !== false);
        const filtrados =
          tipo === 'TODOS'
            ? listaActivos
            : listaActivos.filter(r => (r.tipoReporte ?? '').trim().toUpperCase() === tipo);

        this.dataSource.data = filtrados;
        this.cargando.set(false);

        if (filtrados.length === 0) {
          this.snack.open(
            tipo === 'TODOS'
              ? 'No hay reportes activos registrados'
              : `No existen reportes activos de tipo ${tipo}`,
            'OK',
            { duration: 3000 }
          );
        }
      },
      error: () => {
        this.dataSource.data = [];
        this.cargando.set(false);
        this.snack.open('Ocurrió un error al filtrar los reportes por tipo', 'OK', {
          duration: 3500,
        });
      },
    });
  }

  obtenerReportes(): void {
    this.cargarReportesPorTipo(this.tipoSeleccionado());
  }

  eliminarReporte(r: ReporteDTO): void {
    if (!confirm(`¿Eliminar el reporte #${r.idReporte}?`)) return;
    this.rS.delete(r.idReporte).subscribe({
      next: () => {
        this.snack.open('Reporte eliminado', 'OK', { duration: 2500 });
        this.cargarReportesPorTipo(this.tipoSeleccionado());
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener coincidencias asociadas', 'OK', {
          duration: 4000,
        });
      },
    });
  }

  formatearFecha(f: string): string {
    return f ? f.split('-').reverse().join('/') : '';
  }

  formatearHora(h: string): string {
    return h ? h.substring(0, 5) : '';
  }

  esPerdido(tipo: string): boolean {
    return (tipo ?? '').toUpperCase().startsWith('PERD');
  }
}