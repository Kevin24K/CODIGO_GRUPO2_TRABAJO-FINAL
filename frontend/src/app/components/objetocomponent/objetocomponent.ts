import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-objetocomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './objetocomponent.html',
  styleUrl: './objetocomponent.css',
})
export class Objetocomponent {}
