import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Objetoservice } from '../../../services/objetoservice';
import { Categoriaservice } from '../../../services/categoriaservice';
import { CategoriaDTO } from '../../../models/CategoriaDTO';
import { ObjetoInsertDTO } from '../../../models/ObjetoDTO';
import { Fotocomponent } from '../../fotocomponent/fotocomponent';
import { FotoComprimida } from '../../../services/imagenservice';

@Component({
  selector: 'app-oinsertcomponent',
  imports: [ReactiveFormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatSlideToggleModule, Fotocomponent],
  templateUrl: './oinsertcomponent.html',
  styleUrl: './oinsertcomponent.css',
})
export class Oinsertcomponent implements OnInit {

  form: FormGroup
  categorias = signal<CategoriaDTO[]>([])
  guardando = signal(false)
  foto = signal<FotoComprimida | null>(null) // HUF19

  constructor(private fb: FormBuilder, private oS: Objetoservice, private cS: Categoriaservice,
    private router: Router, private snack: MatSnackBar) {
    this.form = this.fb.group({
      nombreObjeto: ['', [Validators.required, Validators.maxLength(100)]],
      descripcionObjeto: ['', Validators.required],
      colorObjeto: ['', [Validators.required, Validators.maxLength(50)]],
      marcaObjeto: ['', [Validators.required, Validators.maxLength(100)]],
      idCategoria: [null, Validators.required],
      activoObjeto: [true],
    })
  }

  ngOnInit(): void {
    // Para el combo de categorías
    this.cS.getCategorias().subscribe({
      next: data => this.categorias.set(data.filter(c => c.activoCategoria)),
      error: () => this.snack.open('No se pudieron cargar las categorías', 'OK', { duration: 3000 }),
    })
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    const o: ObjetoInsertDTO = { ...new ObjetoInsertDTO(), ...this.form.value }
    o.fotoObjeto = this.foto()?.dataUrl ?? '' // foto comprimida; la IA la describe en el backend
    this.guardando.set(true)
    this.oS.insert(o).subscribe({
      next: () => {
        this.snack.open(this.foto() ? 'Objeto registrado. La IA describió la foto.' : 'Objeto registrado', 'OK', { duration: 3000 })
        this.router.navigate(['/objetos/listar'])
      },
      error: (err) => {
        this.guardando.set(false)
        this.snack.open(err.error?.message ?? 'Ocurrió un error al registrar', 'OK', { duration: 4000 })
      },
    })
  }
}
