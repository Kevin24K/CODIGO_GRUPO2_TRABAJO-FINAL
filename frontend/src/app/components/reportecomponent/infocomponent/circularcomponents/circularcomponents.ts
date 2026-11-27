import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { Reporteservice } from '../../../../services/reporteservice';
import { BaseChartDirective } from 'ng2-charts';

type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-circularcomponents',
  imports: [BaseChartDirective],
  templateUrl: './circularcomponents.html',
  styleUrl: './circularcomponents.css',
})
export class Circularcomponents implements OnInit {
  cargando = true;
  sinReportes = false;
  tasaRecuperacion = 0;

  public circularType: 'doughnut' = 'doughnut';

  public circularData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [
      {
        label: 'Reportes',
        data: [],
        backgroundColor: ['#1B6B5A', '#D97706', '#204B62', '#64748B'],
        borderWidth: 1,
      },
    ],
  };

  public circularOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'bottom',
      },
      title: {
        display: true,
        text: 'Tasa de recuperación y estado de reportes',
      },
      tooltip: {
        enabled: true,
        callbacks: {
          label: (ctx) => {
            const valor = ctx.parsed ?? 0;
            const datos = ctx.dataset.data as number[];
            const total = datos.reduce((acc, n) => acc + (n ?? 0), 0);
            const pct = total > 0 ? ((valor / total) * 100).toFixed(1) : '0.0';
            const sufijo = valor === 1 ? 'reporte' : 'reportes';
            return ` ${ctx.label}: ${valor} ${sufijo} (${pct}%)`;
          },
        },
      },
    },
  };

  constructor(
    private rS: Reporteservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerResumenRecuperacion();
  }

  obtenerResumenRecuperacion(): void {
    this.cargando = true;
    this.sinReportes = false;

    this.rS.getReportes().subscribe({
      next: (reportes: any[]) => {
        const listaReportes = reportes ?? [];
        if (listaReportes.length === 0) {
          this.tasaRecuperacion = 0;
          this.sinReportes = true;
          this.cargando = false;
          this.cdr.detectChanges();
          return;
        }

        let recuperados = 0;
        let pendientes = 0;
        let enProceso = 0;
        let otros = 0;

        for (const r of listaReportes) {
          const tipo = String(r.tipoReporte ?? r.tipo_reporte ?? '').toUpperCase();
          const estado = String(
            r.estadoReporte?.nombreEReporte ??
              r.nombreEstadoReporte ??
              r.estado ??
              ''
          ).toUpperCase();

          if (tipo.startsWith('ENCONTR') || estado.includes('RECUPER') || estado.includes('RESUELT')) {
            recuperados++;
          } else if (estado.includes('PROCES') || estado.includes('COINCID')) {
            enProceso++;
          } else if (tipo.startsWith('PERD') || estado.includes('PEND') || estado.includes('ABIER')) {
            pendientes++;
          } else {
            otros++;
          }
        }

        const total = listaReportes.length;
        this.tasaRecuperacion = total > 0 ? Number(((recuperados / total) * 100).toFixed(1)) : 0;

        const segmentos = [
          { label: 'Recuperados / Encontrados', value: recuperados },
          { label: 'Pendientes / Perdidos', value: pendientes },
          { label: 'En proceso / Coincidencia', value: enProceso },
          { label: 'Otros estados', value: otros },
        ].filter((s) => s.value > 0);

        this.sinReportes = segmentos.length === 0;
        this.circularData = {
          labels: segmentos.map((s) => s.label),
          datasets: [
            {
              label: 'Reportes',
              data: segmentos.map((s) => s.value),
              backgroundColor: ['#1B6B5A', '#D97706', '#204B62', '#64748B'],
              borderWidth: 1,
            },
          ],
        };

        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar el resumen de recuperación:', error);
        this.tasaRecuperacion = 0;
        this.sinReportes = true;
        this.cargando = false;
        this.cdr.detectChanges();
      },
    });
  }
}