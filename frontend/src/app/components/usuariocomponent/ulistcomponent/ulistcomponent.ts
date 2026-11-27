import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-ulistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink, DatePipe],
  templateUrl: './ulistcomponent.html',
  styleUrl: './ulistcomponent.css',
})
export class Ulistcomponent implements OnInit {

  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7']
  dataSource: MatTableDataSource<UsuarioDTO> = new MatTableDataSource<UsuarioDTO>()

  constructor(private uS: Usuarioservice, private snack: MatSnackBar) {}

  ngOnInit(): void {
    this.obtenerUsuarios()
  }

  obtenerUsuarios() {
    this.uS.getUsuarios().subscribe({
      next: data => {
        this.dataSource.data = data
      },
      error: (err) => {
        this.snack.open(err.status === 403
          ? 'Solo un ADMIN puede ver los usuarios'
          : 'Ocurrió un error al listar los usuarios', 'OK', { duration: 3000 })
      },
    })
  }

  eliminarUsuario(u: UsuarioDTO) {
    if (!confirm(`¿Eliminar al usuario ${u.nombre} ${u.apellido}?`)) return
    this.uS.delete(u.idUsuario).subscribe({
      next: () => {
        this.snack.open('Usuario eliminado', 'OK', { duration: 2500 })
        this.obtenerUsuarios()
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener reportes asociados', 'OK', { duration: 4000 })
      },
    })
  }
}
