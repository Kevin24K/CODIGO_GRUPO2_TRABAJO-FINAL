import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-notificacioncomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './notificacioncomponent.html',
  styleUrl: './notificacioncomponent.css',
})
export class Notificacioncomponent {}