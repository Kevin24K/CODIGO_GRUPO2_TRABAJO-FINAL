import { Component, ElementRef, OnDestroy, ViewChild, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialog } from '@angular/material/dialog';
import { Chart, BarController, BarElement, CategoryScale, LinearScale, Tooltip } from 'chart.js';
import { Menucomponent } from '../menucomponent/menucomponent';
import { Objetoservice } from '../../services/objetoservice';
import { ObjetoSimilarDTO } from '../../models/ObjetoSimilarDTO';
import { ObjetoDTO } from '../../models/ObjetoDTO';
import { Fotodialogcomponent } from '../fotodialogcomponent/fotodialogcomponent';

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Tooltip)

@Component({
  selector: 'app-busquedacomponent',
  imports: [Menucomponent, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './busquedacomponent.html',
  styleUrl: './busquedacomponent.css',
})
export class Busquedacomponent implements OnDestroy {

  @ViewChild('grafico') grafico?: ElementRef<HTMLCanvasElement>

  texto = new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(500)])
  resultados = signal<ObjetoSimilarDTO[]>([])
  buscando = signal(false)
  buscado = signal(false)
  metodo = signal('')
  private chart: Chart | null = null

  ejemplos = ['Mochila negra con llavero azul', 'Celular Samsung con funda roja', 'Billetera marrón de cuero con mi DNI']

  constructor(private oS: Objetoservice, private dialog: MatDialog) {}

  usarEjemplo(e: string) {
    this.texto.setValue(e)
  }

  // HUF17: enviar la descripción y recibir los objetos ordenados por similitud
  buscar() {
    if (this.texto.invalid) {
      this.texto.markAsTouched()
      return
    }
    this.buscando.set(true)
    this.oS.buscarSimilares(this.texto.value!.trim(), 8).subscribe({
      next: data => {
        this.resultados.set(data)
        this.metodo.set(data[0]?.metodo ?? '')
        this.buscando.set(false)
        this.buscado.set(true)
        setTimeout(() => this.dibujarGrafico()) // el canvas aparece después del @if
      },
      error: () => {
        this.buscando.set(false)
        this.buscado.set(true)
        this.resultados.set([])
      },
    })
  }

  // Gráfico de barras horizontal con el % de similitud
  dibujarGrafico() {
    this.chart?.destroy()
    const canvas = this.grafico?.nativeElement
    const datos = this.resultados()
    if (!canvas || datos.length === 0) return
    this.chart = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: datos.map(d => `${d.nombreObjeto} (#${d.idObjeto})`),
        datasets: [{
          label: 'Similitud (%)',
          data: datos.map(d => d.porcentaje),
          backgroundColor: datos.map(d => this.color(d.porcentaje)),
          borderRadius: 6,
        }],
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: { min: 0, max: 100, ticks: { callback: v => `${v} %` }, grid: { color: '#e4eef5' } },
          y: { grid: { display: false } },
        },
        plugins: {
          tooltip: { callbacks: { label: c => ` ${c.parsed.x} % de similitud` } },
        },
      },
    })
  }

  // verde = muy parecido, ámbar = posible, gris = poco parecido
  color(p: number): string {
    if (p >= 70) return '#1b8a4a'
    if (p >= 40) return '#f2a900'
    return '#90a4ae'
  }

  nivel(p: number): string {
    if (p >= 70) return 'Muy probable'
    if (p >= 40) return 'Posible'
    return 'Poco probable'
  }

  verFoto(r: ObjetoSimilarDTO) {
    const o = { ...new ObjetoDTO(), idObjeto: r.idObjeto, nombreObjeto: r.nombreObjeto,
      fotoObjeto: r.fotoObjeto, descripcionIaObjeto: r.descripcionIaObjeto }
    this.dialog.open(Fotodialogcomponent, { data: { objeto: o, modo: 'ver' }, width: '560px', maxWidth: '95vw' })
  }

  ngOnDestroy(): void {
    this.chart?.destroy()
  }
}
