# Lost & Found IA — Frontend (Angular 21 + Angular Material)

Frontend del Trabajo Parcial del Grupo 2 (Arquitectura de Aplicaciones Web).
Sigue la misma estructura del demo de la profesora (environments, models, services, components).

## Requisitos
- Node.js 20.19+ o 22+
- El backend Spring Boot corriendo en `http://localhost:8080`

## Cómo levantarlo
```bash
cd frontend
npm install
npm start
```
Abrir http://localhost:4200 e iniciar sesión (usuario de prueba del seed SQL):
- Correo: `admin@lostfound.pe`
- Contraseña: `Admin123!`

Si el backend usa otro puerto, cambiar `base` en `src/environments/environment.ts`
y `src/environments/environment.development.ts`.

## Estructura
```
src/app/
├── models/        DTOs (CategoriaDTO, ObjetoDTO, ReporteDTO, UsuarioDTO, RolDTO, EstadoReporteDTO, LoginDTO)
├── services/      Llamadas HTTP a la API (/login, /api/Categorias, /api/Objetos, /api/Reportes, /api/Usuarios...)
├── interceptors/  authinterceptor: agrega "Authorization: Bearer <token>" a cada petición
├── guards/        authGuard (pide sesión) y adminGuard (pide ROLE_ADMIN)
└── components/
    ├── logincomponent
    ├── menucomponent, homecomponent
    ├── categoriacomponent  (catlistcomponent, catinsertcomponent)
    ├── objetocomponent     (olistcomponent, oinsertcomponent)
    ├── reportecomponent    (rlistcomponent, rinsertcomponent)
    └── usuariocomponent    (ulistcomponent, uinsertcomponent)  -> solo ADMIN
```

## Funcionalidades
| Módulo     | Listar | Registrar | Eliminar |
|------------|:------:|:---------:|:--------:|
| Categorías | ✔ | ✔ | ✔ |
| Objetos    | ✔ | ✔ (con categoría) | ✔ |
| Reportes   | ✔ | ✔ (objeto, usuario, estado, fecha y hora) | ✔ |
| Usuarios   | ✔ | ✔ (con rol) | ✔ |

## Notas
- El backend necesita CORS para `http://localhost:4200` (bean `corsConfigurationSource` en `SecurityConfig`).
- Para registrar reportes deben existir registros en `estado_reporte` (por ejemplo ABIERTO / CERRADO).
- Todas las rutas se renderizan en el navegador (`RenderMode.Client`) porque el token se guarda en `localStorage`.
