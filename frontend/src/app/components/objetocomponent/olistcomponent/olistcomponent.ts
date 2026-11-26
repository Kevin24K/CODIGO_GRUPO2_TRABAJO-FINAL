import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Fotodialogcomponent } from '../../fotodialogcomponent/fotodialogcomponent';
import { ObjetoDTO } from '../../../models/ObjetoDTO';
import { Objetoservice } from '../../../services/objetoservice';

@Component({
  selector: 'app-olistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, MatTooltipModule, RouterLink],
  templateUrl: './olistcomponent.html',
  styleUrl: './olistcomponent.css',
})
export class Olistcomponent implements OnInit {

  displayedColumns: string[] = ['c0', 'c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7', 'c8']
  dataSource: MatTableDataSource<ObjetoDTO> = new MatTableDataSource<ObjetoDTO>()

  constructor(private oS: Objetoservice, private snack: MatSnackBar, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.obtenerObjetos()
  }

  obtenerObjetos() {
    this.oS.getObjetos().subscribe({
      next: data => {
        this.dataSource.data = data
      },
      error: () => {
        this.snack.open('Ocurrió un error al listar los objetos', 'OK', { duration: 3000 })
      },
    })
  }

  eliminarObjeto(o: ObjetoDTO) {
    if (!confirm(`¿Eliminar el objeto "${o.nombreObjeto}"?`)) return
    this.oS.delete(o.idObjeto).subscribe({
      next: () => {
        this.snack.open('Objeto eliminado', 'OK', { duration: 2500 })
        this.obtenerObjetos()
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener reportes asociados', 'OK', { duration: 4000 })
      },
    })
  }

  // HUF18: ver la foto comprimida y su descripción IA
  verFoto(o: ObjetoDTO) {
    this.dialog.open(Fotodialogcomponent, { data: { objeto: o, modo: 'ver' }, width: '560px', maxWidth: '95vw' })
  }

  // HUF19: agregar o cambiar la foto de un objeto ya registrado
  subirFoto(o: ObjetoDTO) {
    this.dialog.open(Fotodialogcomponent, { data: { objeto: o, modo: 'subir' }, width: '520px', maxWidth: '95vw' })
      .afterClosed().subscribe(actualizado => {
        if (actualizado) this.obtenerObjetos()
      })
  }
}
