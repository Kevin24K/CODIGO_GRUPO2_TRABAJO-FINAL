import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Loginservice } from '../../services/loginservice';

@Component({
  selector: 'app-logincomponent',
  imports: [ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './logincomponent.html',
  styleUrl: './logincomponent.css',
})
export class Logincomponent {

  form: FormGroup
  mensaje = signal('')
  cargando = signal(false)
  verContrasena = signal(false)

  constructor(private fb: FormBuilder, private lS: Loginservice, private router: Router) {
    this.form = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', Validators.required],
    })
  }

  ingresar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    this.cargando.set(true)
    this.mensaje.set('')
    this.lS.login(this.form.value).subscribe({
      next: () => {
        this.cargando.set(false)
        this.router.navigate(['/'])
      },
      error: (err) => {
        this.cargando.set(false)
        this.mensaje.set(err.status === 0
          ? 'No se pudo conectar con el backend. ¿Está corriendo en el puerto 8080?'
          : 'Correo o contraseña incorrectos')
      },
    })
  }
}
