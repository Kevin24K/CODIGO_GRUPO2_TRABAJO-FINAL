import { Injectable } from '@angular/core';

// Resultado de comprimir una foto
export interface FotoComprimida {
  dataUrl: string      // data:image/jpeg;base64,... (lo que se guarda en la BD)
  kbOriginal: number
  kbFinal: number
  ancho: number
  alto: number
}

@Injectable({
  providedIn: 'root',
})
export class Imagenservice {

  // HUF19: comprime en el navegador con la Canvas API (no usa IA)
  comprimir(archivo: Blob, maxLado = 800, calidad = 0.7): Promise<FotoComprimida> {
    return new Promise((resolve, reject) => {
      const url = URL.createObjectURL(archivo)
      const img = new Image()
      img.onload = () => {
        // 1. reducir tamaño manteniendo la proporción
        const escala = Math.min(1, maxLado / Math.max(img.width, img.height))
        const ancho = Math.round(img.width * escala)
        const alto = Math.round(img.height * escala)
        // 2. dibujar en un canvas más pequeño
        const canvas = document.createElement('canvas')
        canvas.width = ancho
        canvas.height = alto
        canvas.getContext('2d')!.drawImage(img, 0, 0, ancho, alto)
        URL.revokeObjectURL(url)
        // 3. exportar como JPEG con menor calidad
        const dataUrl = canvas.toDataURL('image/jpeg', calidad)
        resolve({
          dataUrl,
          kbOriginal: Math.round(archivo.size / 1024),
          kbFinal: this.pesoKb(dataUrl),
          ancho,
          alto,
        })
      }
      img.onerror = () => {
        URL.revokeObjectURL(url)
        reject('No se pudo leer la imagen')
      }
      img.src = url
    })
  }

  // Peso aproximado de una foto en base64
  pesoKb(dataUrl: string): number {
    if (!dataUrl) return 0
    const base64 = dataUrl.substring(dataUrl.indexOf(',') + 1)
    return Math.round((base64.length * 3) / 4 / 1024)
  }
}
