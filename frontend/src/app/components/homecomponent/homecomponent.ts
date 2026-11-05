import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { Menucomponent } from '../menucomponent/menucomponent';
import { Loginservice } from '../../services/loginservice';

@Component({
  selector: 'app-homecomponent',
  imports: [Menucomponent, RouterLink, MatIconModule, MatCardModule],
  templateUrl: './homecomponent.html',
  styleUrl: './homecomponent.css',
})
export class Homecomponent {
  constructor(public lS: Loginservice) {}
}
