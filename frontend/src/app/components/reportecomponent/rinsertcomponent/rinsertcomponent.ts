import { Component, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Reporteservice } from '../../../services/reporteservice';
import { Objetoservice } from '../../../services/objetoservice';
import { Estadoreporteservice } from '../../../services/estadoreporteservice';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Loginservice } from '../../../services/loginservice';
import { ObjetoDTO } from '../../../models/ObjetoDTO';
import { EstadoReporteDTO } from '../../../models/EstadoReporteDTO';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { ReporteDTO } from '../../../models/ReporteDTO';

@Component({
  selector: 'app-rinsertcomponent',
  imports: [ReactiveFormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatDatepickerModule],
  providers: [provideNativeDateAdapter(), { provide: MAT_DATE_LOCALE, useValue: 'es-PE' }],
  templateUrl: './rinsertcomponent.html',
  styleUrl: './rinsertcomponent.css',
})
export class Rinsertcomponent implements OnInit {

  form: FormGroup
  objetos = signal<ObjetoDTO[]>([])
  estados = signal<EstadoReporteDTO[]>([])
  usuarios = signal<UsuarioDTO[]>([])
  guardando = signal(false)
  hoy = new Date()
  tipos = ['PERDIDO', 'ENCONTRADO']

  constructor(private fb: FormBuilder, private rS: Reporteservice, private oS: Objetoservice,
    private eS: Estadoreporteservice, private uS: Usuarioservice, private lS: Loginservice,
    private router: Router, private snack: MatSnackBar) {
    this.form = this.fb.group({
      tipoReporte: ['PERDIDO', Validators.required],
      fechaeventoR: [new Date(), Validators.required],
      horaEventoR: ['', Validators.required],
      idObjeto: [null, Validators.required],
      idUsuario: [null, Validators.required],
      idEstadoReporte: [null, Validators.required],
    })
  }

  ngOnInit(): void {
    this.oS.getObjetos().subscribe(data => this.objetos.set(data.filter(o => o.activoObjeto)))
    this.eS.getEstados().subscribe(data => this.estados.set(data))
    if (this.lS.esAdmin()) {
      this.uS.getUsuarios().subscribe(data => this.usuarios.set(data.filter(u => u.activo)))
    }
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    const v = this.form.value
    const ahora = this.fechaHoraLocal(new Date())
    const r: ReporteDTO = {
      ...new ReporteDTO(),
      tipoReporte: v.tipoReporte,
      fechaeventoR: this.soloFecha(v.fechaeventoR),   // LocalDate
      horaEventoR: `${v.horaEventoR}:00`,              // LocalTime
      fechaCreacion: ahora,                            // LocalDateTime
      fechaActualizacionR: ahora,
      idObjeto: v.idObjeto,
      idUsuario: v.idUsuario,
      idEstadoReporte: v.idEstadoReporte,
    }
    this.guardando.set(true)
    this.rS.insert(r).subscribe({
      next: () => {
        this.snack.open('Reporte registrado', 'OK', { duration: 2500 })
        this.router.navigate(['/reportes/listar'])
      },
      error: (err) => {
        this.guardando.set(false)
        this.snack.open(err.error?.message ?? 'Ocurrió un error al registrar', 'OK', { duration: 4000 })
      },
    })
  }

  // Date -> "2026-11-05" usando la hora local (no UTC)
  private soloFecha(d: Date): string {
    const dos = (n: number) => String(n).padStart(2, '0')
    return `${d.getFullYear()}-${dos(d.getMonth() + 1)}-${dos(d.getDate())}`
  }

  // Date -> "2026-11-05T14:30:00" (formato LocalDateTime de Java)
  private fechaHoraLocal(d: Date): string {
    const dos = (n: number) => String(n).padStart(2, '0')
    return `${this.soloFecha(d)}T${dos(d.getHours())}:${dos(d.getMinutes())}:${dos(d.getSeconds())}`
  }
}
