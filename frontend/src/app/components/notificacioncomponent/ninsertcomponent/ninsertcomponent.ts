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
import { Notificacionservice } from '../../../services/notificacionservice';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Coincidenciaservice } from '../../../services/coincidenciaservice';
import { NotificacionInsertDTO } from '../../../models/NotificacionDTO';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { CoincidenciaDTO } from '../../../models/CoincidenciaDTO';

@Component({
  selector: 'app-ninsertcomponent',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSlideToggleModule,
  ],
  templateUrl: './ninsertcomponent.html',
  styleUrl: './ninsertcomponent.css',
})
export class Ninsertcomponent implements OnInit {
  form: FormGroup;
  usuarios = signal<UsuarioDTO[]>([]);
  coincidencias = signal<CoincidenciaDTO[]>([]);
  guardando = signal(false);

  tipos = ['COINCIDENCIA', 'ALERTA', 'RECORDATORIO', 'SISTEMA'];

  constructor(
    private fb: FormBuilder,
    private nS: Notificacionservice,
    private uS: Usuarioservice,
    private cS: Coincidenciaservice,
    private router: Router,
    private snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      tituloNotificacion: ['', [Validators.required, Validators.maxLength(100)]],
      mensajeNotificacion: ['', [Validators.required, Validators.maxLength(255)]],
      tipoNotificacion: ['COINCIDENCIA', [Validators.required, Validators.maxLength(50)]],
      idUsuario: [null, Validators.required],
      idCoincidencia: [null, Validators.required],
      leidaNotificacion: [false],
    });
  }

  ngOnInit(): void {
    this.uS.getUsuarios().subscribe({
      next: data => this.usuarios.set(data.filter(u => u.activo)),
      error: () => this.snack.open('No se pudieron cargar los usuarios', 'OK', { duration: 3000 }),
    });

    this.cS.getCoincidencias().subscribe({
      next: data => this.coincidencias.set(data),
      error: () => this.snack.open('No se pudieron cargar las coincidencias', 'OK', { duration: 3000 }),
    });
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const dto: NotificacionInsertDTO = {
      ...new NotificacionInsertDTO(),
      ...this.form.value,
      fechaCreacionN: this.fechaHoraLocal(new Date()), // LocalDateTime requerido por @NotNull en NotificacionDTCinsert
    };

    this.guardando.set(true);
    this.nS.insert(dto).subscribe({
      next: () => {
        this.snack.open('Notificación registrada', 'OK', { duration: 2500 });
        this.router.navigate(['/notificaciones/listar']);
      },
      error: (err) => {
        this.guardando.set(false);
        this.snack.open(err.error?.message ?? 'Ocurrió un error al registrar', 'OK', { duration: 4000 });
      },
    });
  }

  private fechaHoraLocal(d: Date): string {
    const dos = (n: number) => String(n).padStart(2, '0');
    const fecha = `${d.getFullYear()}-${dos(d.getMonth() + 1)}-${dos(d.getDate())}`;
    return `${fecha}T${dos(d.getHours())}:${dos(d.getMinutes())}:${dos(d.getSeconds())}`;
  }
}