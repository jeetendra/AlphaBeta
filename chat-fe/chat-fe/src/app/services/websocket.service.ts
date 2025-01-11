import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import {environment} from '../environment';


@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket$: WebSocketSubject<any>;

  constructor() {
    const token = localStorage.getItem("auth_token")
    this.socket$ = webSocket(environment.wsUrl);

  }

  connect() {
    return this.socket$;
  }

  sendMessage(message: any) {
    this.socket$.next(message);
  }
}
