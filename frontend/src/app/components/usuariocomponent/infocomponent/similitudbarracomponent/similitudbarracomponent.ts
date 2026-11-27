import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { Coincidenciaservice } from '../../../../services/coincidenciaservice';
import { SumaSimilitudEstadoDTO } from '../../../../models/SumaSimilitudEstadoDTO';

@Component({
  selector: 'app-similitudbarracomponent',
  imports: [BaseChartDirective],
  templateUrl: './similitudbarracomponent.html',
  styleUrl: './similitudbarracomponent.css',
})
export class Similitudbarracomponent implements OnInit {
  lista: SumaSimilitudEstadoDTO[] = [];
  cargando = true;
  sinDatos = false;
  mensajeError = '';

  public chartType: 'bar' = 'bar';

  public chartData: ChartData<'bar'> = {
    labels: [],
    datasets: [
      {
        label: 'Suma total de similitud (%)',
        data: [],
        backgroundColor: '#D97706',
        borderWidth: 1,
      },
    ],
  };

  public chartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
      mode: 'index',
      intersect: false,
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
      title: {
        display: true,
        text: 'Suma total de similitud por estado de coincidencia',
      },
      tooltip: {
        enabled: true,
        callbacks: {
          label: (ctx) => {
            const valor = Number(ctx.parsed.y ?? 0);
            return ` Suma total de similitud: ${valor.toFixed(1)}%`;
          },
        },
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Estado de coincidencia',
        },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: 'Suma total de similitud (%)',
        },
      },
    },
  };

  constructor(
    private cS: Coincidenciaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarSumaSimilitud();
  }

  cargarSumaSimilitud(): void {
    this.cargando = true;
    this.sinDatos = false;
    this.mensajeError = '';

    this.cS.obtenerSumaSimilitudPorEstado().subscribe({
      next: (data: SumaSimilitudEstadoDTO[]) => {
        this.lista = data ?? [];
        this.sinDatos = this.lista.length === 0;

        this.chartData = {
          labels: this.lista.map(item => item.nombreEstadoCoincidencia),
          datasets: [
            {
              label: 'Suma total de similitud (%)',
              data: this.lista.map(item => Number(item.sumaSimilitud ?? 0)),
              backgroundColor: '#D97706',
              borderWidth: 1,
            },
          ],
        };

        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar la suma de similitud por estado:', err);
        this.lista = [];
        this.sinDatos = true;
        this.mensajeError =
          'No se pudo cargar la suma de similitud por estado de coincidencia.';
        this.cargando = false;
        this.cdr.detectChanges();
      },
    });
  }
}