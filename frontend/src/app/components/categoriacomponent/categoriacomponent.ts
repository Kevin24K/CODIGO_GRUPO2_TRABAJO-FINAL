import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-categoriacomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './categoriacomponent.html',
  styleUrl: './categoriacomponent.css',
})
export class Categoriacomponent {}
