import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Rolservice } from '../../../services/rolservice';
import { RolDTO } from '../../../models/RolDTO';
import { UsuarioInsertDTO } from '../../../models/UsuarioDTO';

@Component({
  selector: 'app-uinsertcomponent',
  imports: [ReactiveFormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule],
  templateUrl: './uinsertcomponent.html',
  styleUrl: './uinsertcomponent.css',
})
export class Uinsertcomponent implements OnInit {

  form: FormGroup
  roles = signal<RolDTO[]>([])
  guardando = signal(false)
  verContrasena = signal(false)

  constructor(private fb: FormBuilder, private uS: Usuarioservice, private rS: Rolservice,
    private router: Router, private snack: MatSnackBar) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(50)]],
      apellido: ['', [Validators.required, Validators.maxLength(50)]],
      correo: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      contrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100)]],
      idRol: [null, Validators.required],
    })
  }

  ngOnInit(): void {
    this.rS.getRoles().subscribe({
      next: data => this.roles.set(data),
      error: () => this.snack.open('No se pudieron cargar los roles', 'OK', { duration: 3000 }),
    })
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    const u: UsuarioInsertDTO = { ...new UsuarioInsertDTO(), ...this.form.value }
    this.guardando.set(true)
    this.uS.insert(u).subscribe({
      next: () => {
        this.snack.open('Usuario registrado', 'OK', { duration: 2500 })
        this.router.navigate(['/usuarios/listar'])
      },
      error: (err) => {
        this.guardando.set(false)
        this.snack.open(err.error?.message ?? 'Ocurrió un error al registrar', 'OK', { duration: 4000 })
      },
    })
  }
}
