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
import { Coincidenciaservice } from '../../../services/coincidenciaservice';
import { Estadocoincidenciaservice } from '../../../services/estadocoincidenciaservice';
import { Reporteservice } from '../../../services/reporteservice';
import { Objetoservice } from '../../../services/objetoservice';
import { CoincidenciaInsertDTO } from '../../../models/CoincidenciaDTO';
import { EstadoCoincidenciaDTO } from '../../../models/EstadoCoincidenciaDTO';
import { ReporteDTO } from '../../../models/ReporteDTO';

@Component({
  selector: 'app-cinsertcomponent',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './cinsertcomponent.html',
  styleUrl: './cinsertcomponent.css',
})
export class Cinsertcomponent implements OnInit {
  form: FormGroup;
  reportesPerdidos = signal<ReporteDTO[]>([]);
  reportesEncontrados = signal<ReporteDTO[]>([]);
  estados = signal<EstadoCoincidenciaDTO[]>([]);
  objetos = signal(new Map<number, string>());
  guardando = signal(false);

  constructor(
    private fb: FormBuilder,
    private cS: Coincidenciaservice,
    private ecS: Estadocoincidenciaservice,
    private rS: Reporteservice,
    private oS: Objetoservice,
    private router: Router,
    private snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      idReportePerdido: [null, Validators.required],
      idReporteEncontrado: [null, Validators.required],
      idEstadoCoincidencia: [null, Validators.required],
      porcentajeSimilitudC: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
      detalleC: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.oS.getObjetos().subscribe(data =>
      this.objetos.set(new Map(data.map(o => [o.idObjeto, o.nombreObjeto])))
    );

    this.rS.getReportes().subscribe({
      next: data => {
        this.reportesPerdidos.set(
          data.filter(r => (r.tipoReporte ?? '').toUpperCase().startsWith('PERD'))
        );
        this.reportesEncontrados.set(
          data.filter(r => (r.tipoReporte ?? '').toUpperCase().startsWith('ENCONTR'))
        );
      },
      error: () => this.snack.open('No se pudieron cargar los reportes', 'OK', { duration: 3000 }),
    });

    this.ecS.getEstados().subscribe({
      next: data => this.estados.set(data),
      error: () => this.snack.open('No se pudieron cargar los estados', 'OK', { duration: 3000 }),
    });
  }

  registrar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const dto: CoincidenciaInsertDTO = {
      ...new CoincidenciaInsertDTO(),
      ...this.form.value,
      fechaGeneracionC: this.fechaHoraLocal(new Date()), // LocalDateTime para Java
    };

    this.guardando.set(true);
    this.cS.insert(dto).subscribe({
      next: () => {
        this.snack.open('Coincidencia registrada', 'OK', { duration: 2500 });
        this.router.navigate(['/coincidencias/listar']);
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