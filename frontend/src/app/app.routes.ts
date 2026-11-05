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
import { adminGuard, authGuard } from './guards/authguard';

export const routes: Routes = [
    {
        path: 'login',
        component: Logincomponent
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
            { path: 'insertar', component: Catinsertcomponent }
        ]
    },
    {
        path: 'objetos',
        component: Objetocomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Olistcomponent },
            { path: 'insertar', component: Oinsertcomponent }
        ]
    },
    {
        path: 'reportes',
        component: Reportecomponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Rlistcomponent },
            { path: 'insertar', component: Rinsertcomponent }
        ]
    },
    {
        path: 'usuarios',
        component: Usuariocomponent,
        canActivate: [authGuard, adminGuard],
        children: [
            { path: '', redirectTo: 'listar', pathMatch: 'full' },
            { path: 'listar', component: Ulistcomponent },
            { path: 'insertar', component: Uinsertcomponent }
        ]
    },
    {
        path: '**',
        redirectTo: ''
    }
];
