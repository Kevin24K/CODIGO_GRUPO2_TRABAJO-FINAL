import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ObjetoDTO } from '../../../models/ObjetoDTO';
import { Objetoservice } from '../../../services/objetoservice';

@Component({
  selector: 'app-olistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './olistcomponent.html',
  styleUrl: './olistcomponent.css',
})
export class Olistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7', 'c8'];
  dataSource: MatTableDataSource<ObjetoDTO> = new MatTableDataSource<ObjetoDTO>();
  soloActivos = signal(false);

  constructor(private oS: Objetoservice, private snack: MatSnackBar) {}

  ngOnInit(): void {
    this.obtenerObjetos();
  }

  toggleSoloActivos() {
    this.soloActivos.set(!this.soloActivos());
    this.obtenerObjetos();
  }

  obtenerObjetos() {
    const peticion$ = this.soloActivos() ? this.oS.getActivos() : this.oS.getObjetos();
    peticion$.subscribe({
      next: data => {
        this.dataSource.data = data;
      },
      error: () => {
        this.snack.open(
          this.soloActivos()
            ? 'Ocurrió un error al listar los objetos activos'
            : 'Ocurrió un error al listar los objetos',
          'OK',
          { duration: 3000 }
        );
      },
    });
  }

  eliminarObjeto(o: ObjetoDTO) {
    if (!confirm(`¿Eliminar el objeto "${o.nombreObjeto}"?`)) return;
    this.oS.delete(o.idObjeto).subscribe({
      next: () => {
        this.snack.open('Objeto eliminado', 'OK', { duration: 2500 });
        this.obtenerObjetos();
      },
      error: () => {
        this.snack.open('No se pudo eliminar: puede tener reportes asociados', 'OK', { duration: 4000 });
      },
    });
  }
}