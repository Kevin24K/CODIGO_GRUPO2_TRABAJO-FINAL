import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Categoriaservice } from '../../../services/categoriaservice';
import { CategoriaDTO } from '../../../models/CategoriaDTO';

@Component({
  selector: 'app-catinsertcomponent',
  imports: [ReactiveFormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSlideToggleModule],
  templateUrl: './catinsertcomponent.html',
  styleUrl: './catinsertcomponent.css',
})
export class Catinsertcomponent {

  form: FormGroup
  guardando = signal(false)

  constructor(private fb: FormBuilder, private cS: Categoriaservice,
    private router: Router, private snack: MatSnackBar) {
    this.form = this.fb.group({
      nombreCategoria: ['', [Validators.required, Validators.maxLength(100)]],
      descripcionCategoria: ['', [Validators.required, Validators.maxLength(255)]],
      activoCategoria: [true],
    })
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    const c: CategoriaDTO = { ...new CategoriaDTO(), ...this.form.value }
    this.guardando.set(true)
    this.cS.insert(c).subscribe({
      next: () => {
        this.snack.open('Categoría registrada', 'OK', { duration: 2500 })
        this.router.navigate(['/categorias/listar'])
      },
      error: (err) => {
        this.guardando.set(false)
        this.snack.open(err.error?.message ?? 'Ocurrió un error al registrar', 'OK', { duration: 4000 })
      },
    })
  }
}
