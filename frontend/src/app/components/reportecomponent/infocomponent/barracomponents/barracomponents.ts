import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TotalDTO } from '../../../../models/TotalDTO';
import { ChartData, ChartOptions } from 'chart.js';
import { Reporteservice } from '../../../../services/reporteservice';
import { BaseChartDirective } from 'ng2-charts';

type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-barracomponents',
  imports: [BaseChartDirective],
  templateUrl: './barracomponents.html',
  styleUrl: './barracomponents.css',
})
export class Barracomponents {
  lista: TotalDTO[] = [];
    cargando = true;
    sinReportes = false;
  
    public chartType: TipoGrafico = 'bar';
    public circularType: 'doughnut' = 'doughnut';
  
    public chartData: ChartData<TipoGrafico> = {
      labels: [],
      datasets: [
        {
          label: 'Total de reportes por categoría',
          data: [],
          backgroundColor: '#204B62',
          borderWidth: 1
        }
      ]
    };
  
    public chartOptions: ChartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      interaction: {
        mode: 'index',
        intersect: false,
      },
      plugins: {
        legend: {
          display: true,
          position: 'top'
        },
        title: {
          display: true,
          text: 'Total de reportes por categoría'
        },
        tooltip: {
          enabled: true,
          callbacks: {
            label: (ctx) => {
              const total = ctx.parsed.y ?? 0;
              const sufijo = total === 1 ? 'reporte' : 'reportes';
              return ` ${total} ${sufijo}`;
            },
          },
        },
      },
    };
    constructor(private rS: Reporteservice, private cdr: ChangeDetectorRef) { }
  
    ngOnInit(): void {
      this.obtenerTotal();
    }
  
    obtenerTotal(): void {
      this.cargando = true;
      this.sinReportes = false;
  
      this.rS.getTotalByCategory().subscribe({
        next: (data: TotalDTO[]) => {
          this.lista = data;
          this.sinReportes = this.lista.length === 0;
  
          this.chartData = {
            labels: data.map(item => item.nombreCategoria),
            datasets: [
              {
                label: 'Total de reportes por categoría',
                data: data.map(item => item.quantityTotal),
                backgroundColor: '#204B62',
                borderWidth:  1,
              }
            ]
          };
          this.cargando = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Error al cargar los datos:', error);
          this.lista = [];
          this.sinReportes = true;
          this.cargando = false;
          this.cdr.detectChanges();
        }
      });
    }
  }