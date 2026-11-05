import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CategoriaDTO } from '../../../models/CategoriaDTO';
import { Categoriaservice } from '../../../services/categoriaservice';

@Component({
  selector: 'app-catlistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './catlistcomponent.html',
  styleUrl: './catlistcomponent.css',
})
export class Catlistcomponent implements OnInit {

  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5']
  dataSource: MatTableDataSource<CategoriaDTO> = new MatTableDataSource<CategoriaDTO>()

  constructor(private cS: Categoriaservice, private snack: MatSnackBar) {}

  ngOnInit(): void {
    this.obtenerCategorias()
  }

  obtenerCategorias() {
    this.cS.getCategorias().subscribe({
      next: data => {
        this.dataSource.data = data
      },
      error: () => {
        this.snack.open('Ocurrió un error al listar las categorías', 'OK', { duration: 3000 })
      },
    })
  }

  eliminarCategoria(c: CategoriaDTO) {
    if (!confirm(`¿Eliminar la categoría "${c.nombreCategoria}"?`)) return
    this.cS.delete(c.idCategoria).subscribe({
      next: () => {
        this.snack.open('Categoría eliminada', 'OK', { duration: 2500 })
        this.obtenerCategorias()
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener objetos asociados', 'OK', { duration: 4000 })
      },
    })
  }
}
