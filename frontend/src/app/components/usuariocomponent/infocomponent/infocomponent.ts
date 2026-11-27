import { Component} from '@angular/core';
import { Barracomponents } from './barracomponents/barracomponents';
import { Circularcomponents } from './circularcomponents/circularcomponents';

@Component({
  selector: 'app-infocomponent',
  imports: [Barracomponents, Circularcomponents],
  templateUrl: './infocomponent.html',
  styleUrl: './infocomponent.css',
})
export class Infocomponent {

}