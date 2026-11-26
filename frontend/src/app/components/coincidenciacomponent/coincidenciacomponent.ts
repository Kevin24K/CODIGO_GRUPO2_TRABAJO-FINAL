import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-coincidenciacomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './coincidenciacomponent.html',
  styleUrl: './coincidenciacomponent.css',
})
export class Coincidenciacomponent {}