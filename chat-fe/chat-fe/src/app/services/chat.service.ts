import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../environment';
import { Chat, Message } from '../models/chat.model';
import { WebSocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = environment.apiUrl;
  private messages = new BehaviorSubject<Message[]>([]);

  constructor(
    private http: HttpClient,
    private wsService: WebSocketService
  ) {
    this.initializeWebSocket();
  }

  private initializeWebSocket() {
    this.wsService.connect().subscribe(
      (message: Message) => {
        const currentMessages = this.messages.value;
        this.messages.next([...currentMessages, message]);
      }
    );
  }

  getChats(): Observable<Chat[]> {
    const userId = localStorage.getItem("user_id") || "";
    const headers = new HttpHeaders({
      'X-User-Id': userId, // Set the X-User-Id header
      'Content-Type': 'application/json', // If you're sending JSON data
      // Add other headers as needed
    });

    return this.http.get<Chat[]>(`${this.apiUrl}/chats`, {headers});
  }

  getMessages(chatId: string): Observable<Message[]> {

    return this.http.get<Message[]>(`${this.apiUrl}/chats/${chatId}/messages`);
  }

  async sendMessage(chatId: string, content: string, mediaUrl?: string): Promise<void> {
    const message = { chatId, content, mediaUrl };
    this.wsService.sendMessage(message);
  }

  uploadMedia(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(`${this.apiUrl}/upload`, formData);
  }
}
