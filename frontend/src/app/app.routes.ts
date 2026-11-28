import { Routes } from '@angular/router';
import { Logincomponent } from './components/logincomponent/logincomponent';
import { Homecomponent } from './components/homecomponent/homecomponent';
import { Categoriacomponent } from './components/categoriacomponent/categoriacomponent';
import { Catlistcomponent } from './components/categoriacomponent/catlistcomponent/catlistcomponent';
import { Catinsertcomponent } from './components/categoriacomponent/catinsertcomponent/catinsertcomponent';
import { Objetocomponent } from './components/objetocomponent/objetocomponent';
import { Olistcomponent } from './components/objetocomponent/olistcomponent/olistcomponent';
import { Oinsertcomponent } from './components/objetocomponent/oinsertcomponent/oinsertcomponent';
import { Reportecomponent } from './components/reportecomponent/reportecomponent';
import { Rlistcomponent } from './components/reportecomponent/rlistcomponent/rlistcomponent';
import { Rinsertcomponent } from './components/reportecomponent/rinsertcomponent/rinsertcomponent';
import { Usuariocomponent } from './components/usuariocomponent/usuariocomponent';
import { Ulistcomponent } from './components/usuariocomponent/ulistcomponent/ulistcomponent';
import { Uinsertcomponent } from './components/usuariocomponent/uinsertcomponent/uinsertcomponent';
import { Busquedacomponent } from './components/busquedacomponent/busquedacomponent';
import { Landingcomponent } from './components/landingcomponent/landingcomponent';
import { authGuard,adminGuard } from './guards/authguard';
import { Infocomponent } from './components/usuariocomponent/infocomponent/infocomponent';
import { Coincidenciacomponent } from './components/coincidenciacomponent/coincidenciacomponent';
import { Clistcomponent } from './components/coincidenciacomponent/clistcomponent/clistcomponent';
import { Cinsertcomponent } from './components/coincidenciacomponent/cinsertcomponent/cinsertcomponent';
import { Notificacioncomponent } from './components/notificacioncomponent/notificacioncomponent';
import { Nlistcomponent } from './components/notificacioncomponent/nlistcomponent/nlistcomponent';
import { Ninsertcomponent } from './components/notificacioncomponent/ninsertcomponent/ninsertcomponent';
export const routes: Routes = [
    {
        path: 'login',
        component: Logincomponent
    },
     {
        path: '', // Landing pública
        component: Landingcomponent,
        pathMatch: 'full'
    },
    {
        path: '',
        component: Homecomponent,
        canActivate: [authGuard]
    },
    {
        path: 'categorias',
        component: Categoriacomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Catlistcomponent },
            {path:'insertar',component:Catinsertcomponent}
        ]
    },
    {
        path: 'objetos',
        component: Objetocomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Olistcomponent },
            { path: 'insertar', component: Oinsertcomponent },
        ]
    },
    {
        path: 'reportes',
        component: Reportecomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Rlistcomponent },
            { path: 'insertar', component: Rinsertcomponent },
        ]
    },
    {
        path: 'usuarios',
        component: Usuariocomponent,
        canActivate: [authGuard, adminGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Ulistcomponent },
            { path: 'insertar', component: Uinsertcomponent },
            { path: 'informacion', component:Infocomponent},
        ]
    },
     {
        path: 'coincidencias',
        component: Coincidenciacomponent,
        canActivate: [authGuard, adminGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Clistcomponent },
            { path: 'insertar', component: Cinsertcomponent },
        ]
    },
    {
        path: 'buscar', // HUF17: búsqueda de coincidencias por descripción
        component: Busquedacomponent,
        canActivate: [authGuard],
    },
    {
        path: 'notificaciones',
        component: Notificacioncomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Nlistcomponent },
            { path: 'insertar', component: Ninsertcomponent },
        ]
    },
    {
        path: '**',
        redirectTo: '',
    }
];
