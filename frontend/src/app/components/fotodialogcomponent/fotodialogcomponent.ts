import { Component, Inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ObjetoDTO } from '../../models/ObjetoDTO';
import { Objetoservice } from '../../services/objetoservice';
import { FotoComprimida, Imagenservice } from '../../services/imagenservice';
import { Fotocomponent } from '../fotocomponent/fotocomponent';

export interface FotoDialogData {
  objeto: ObjetoDTO
  modo: 'ver' | 'subir'
}

@Component({
  selector: 'app-fotodialogcomponent',
  imports: [MatDialogModule, MatButtonModule, MatIconModule, Fotocomponent],
  templateUrl: './fotodialogcomponent.html',
  styleUrl: './fotodialogcomponent.css',
})
export class Fotodialogcomponent {

  nueva = signal<FotoComprimida | null>(null)
  guardando = signal(false)

  constructor(@Inject(MAT_DIALOG_DATA) public data: FotoDialogData,
    private ref: MatDialogRef<Fotodialogcomponent>,
    private oS: Objetoservice, public iS: Imagenservice, private snack: MatSnackBar) {}

  // HUF19: guardar la foto nueva; el backend pide la descripción a la IA
  guardar() {
    const f = this.nueva()
    if (!f) return
    this.guardando.set(true)
    this.oS.updateFoto(this.data.objeto.idObjeto, { fotoObjeto: f.dataUrl, descripcionIaObjeto: '' }).subscribe({
      next: (actualizado) => {
        this.snack.open('Foto guardada', 'OK', { duration: 2500 })
        this.ref.close(actualizado)
      },
      error: () => {
        this.guardando.set(false)
        this.snack.open('No se pudo guardar la foto', 'OK', { duration: 3500 })
      },
    })
  }
}
