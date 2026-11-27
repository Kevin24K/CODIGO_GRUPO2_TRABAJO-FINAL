import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-reportecomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './reportecomponent.html',
  styleUrl: './reportecomponent.css',
})
export class Reportecomponent {}
