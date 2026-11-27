import { Component, ElementRef, OnDestroy, ViewChild, output, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FotoComprimida, Imagenservice } from '../../services/imagenservice';

@Component({
  selector: 'app-fotocomponent',
  imports: [MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './fotocomponent.html',
  styleUrl: './fotocomponent.css',
})
export class Fotocomponent implements OnDestroy {

  // avisa al padre cuando la foto ya está comprimida
  fotoLista = output<FotoComprimida | null>()

  @ViewChild('video') video?: ElementRef<HTMLVideoElement>

  foto = signal<FotoComprimida | null>(null)
  camaraActiva = signal(false)
  procesando = signal(false)
  error = signal('')
  private stream: MediaStream | null = null

  constructor(private iS: Imagenservice) {}

  // Subir desde archivo (en el celular abre la cámara por capture="environment")
  async alElegirArchivo(event: Event) {
    const input = event.target as HTMLInputElement
    const archivo = input.files?.[0]
    input.value = ''
    if (!archivo) return
    if (!archivo.type.startsWith('image/')) {
      this.error.set('El archivo debe ser una imagen')
      return
    }
    await this.procesar(archivo)
  }

  // Tomar foto con la cámara de la laptop/celular
  async abrirCamara() {
    this.error.set('')
    try {
      this.stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } })
      this.camaraActiva.set(true)
      setTimeout(() => {
        if (this.video) this.video.nativeElement.srcObject = this.stream
      })
    } catch {
      this.error.set('No se pudo abrir la cámara. Revisa los permisos o usa "Subir foto".')
    }
  }

  capturar() {
    const v = this.video?.nativeElement
    if (!v) return
    const canvas = document.createElement('canvas')
    canvas.width = v.videoWidth
    canvas.height = v.videoHeight
    canvas.getContext('2d')!.drawImage(v, 0, 0)
    canvas.toBlob(async blob => {
      this.cerrarCamara()
      if (blob) await this.procesar(blob)
    }, 'image/jpeg', 0.95)
  }

  cerrarCamara() {
    this.stream?.getTracks().forEach(t => t.stop())
    this.stream = null
    this.camaraActiva.set(false)
  }

  quitar() {
    this.foto.set(null)
    this.fotoLista.emit(null)
  }

  private async procesar(archivo: Blob) {
    this.procesando.set(true)
    this.error.set('')
    try {
      const f = await this.iS.comprimir(archivo)
      this.foto.set(f)
      this.fotoLista.emit(f)
    } catch {
      this.error.set('No se pudo comprimir la imagen')
    } finally {
      this.procesando.set(false)
    }
  }

  ahorro(f: FotoComprimida): number {
    return f.kbOriginal > 0 ? Math.max(0, Math.round((1 - f.kbFinal / f.kbOriginal) * 100)) : 0
  }

  ngOnDestroy(): void {
    this.cerrarCamara()
  }
}
