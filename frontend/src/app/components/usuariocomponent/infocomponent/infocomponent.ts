import { Component} from '@angular/core';
import { Barracomponents } from './barracomponents/barracomponents';
import { Circularcomponents } from './circularcomponents/circularcomponents';
import { Similitudbarracomponent } from './similitudbarracomponent/similitudbarracomponent';

@Component({
  selector: 'app-infocomponent',
  imports: [Barracomponents, Circularcomponents, Similitudbarracomponent],
  templateUrl: './infocomponent.html',
  styleUrl: './infocomponent.css',
})
export class Infocomponent {

}
