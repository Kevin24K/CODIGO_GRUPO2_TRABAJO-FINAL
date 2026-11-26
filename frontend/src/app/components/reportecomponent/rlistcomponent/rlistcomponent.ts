import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ReporteDTO } from '../../../models/ReporteDTO';
import { Reporteservice } from '../../../services/reporteservice';
import { Objetoservice } from '../../../services/objetoservice';
import { Estadoreporteservice } from '../../../services/estadoreporteservice';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Loginservice } from '../../../services/loginservice';

@Component({
  selector: 'app-rlistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './rlistcomponent.html',
  styleUrl: './rlistcomponent.css',
})
export class Rlistcomponent implements OnInit {

  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7']
  dataSource: MatTableDataSource<ReporteDTO> = new MatTableDataSource<ReporteDTO>()

  // El backend solo devuelve los IDs: con estos mapas mostramos los nombres
  objetos = signal(new Map<number, string>())
  estados = signal(new Map<number, string>())
  usuarios = signal(new Map<number, string>())

  constructor(private rS: Reporteservice, private oS: Objetoservice,
    private eS: Estadoreporteservice, private uS: Usuarioservice,
    private lS: Loginservice, private snack: MatSnackBar) {}

  ngOnInit(): void {
    this.obtenerReportes()
    this.oS.getObjetos().subscribe(data =>
      this.objetos.set(new Map(data.map(o => [o.idObjeto, o.nombreObjeto]))))
    this.eS.getEstados().subscribe(data =>
      this.estados.set(new Map(data.map(e => [e.idEstadoReporte, e.nombreEReporte]))))
    // Listar usuarios solo lo puede hacer un ADMIN
    if (this.lS.esAdmin()) {
      this.uS.getUsuarios().subscribe(data =>
        this.usuarios.set(new Map(data.map(u => [u.idUsuario, `${u.nombre} ${u.apellido}`]))))
    }
  }

  obtenerReportes() {
    this.rS.getReportes().subscribe({
      next: data => {
        this.dataSource.data = data
      },
      error: () => {
        this.snack.open('Ocurrió un error al listar los reportes', 'OK', { duration: 3000 })
      },
    })
  }

  eliminarReporte(r: ReporteDTO) {
    if (!confirm(`¿Eliminar el reporte #${r.idReporte}?`)) return
    this.rS.delete(r.idReporte).subscribe({
      next: () => {
        this.snack.open('Reporte eliminado', 'OK', { duration: 2500 })
        this.obtenerReportes()
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener coincidencias asociadas', 'OK', { duration: 4000 })
      },
    })
  }

  // "2026-11-05" -> "05/11/2026" (sin pasar por Date para no correr el día por la zona horaria)
  formatearFecha(f: string): string {
    return f ? f.split('-').reverse().join('/') : ''
  }

  // "14:30:00" -> "14:30"
  formatearHora(h: string): string {
    return h ? h.substring(0, 5) : ''
  }

  esPerdido(tipo: string): boolean {
    return (tipo ?? '').toUpperCase().startsWith('PERD')
  }
}
