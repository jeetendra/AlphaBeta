import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
import { Chat, Message } from '../../models/chat.model';

@Component({
  selector: 'app-chat-window',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="h-full flex flex-col">
      <div class="p-4 border-b">
        <h3 class="text-lg font-semibold">{{ chat.name || 'Chat' }}</h3>
      </div>

      <div class="flex-1 overflow-y-auto p-4">
        <div *ngFor="let message of messages"
             [class.flex-row-reverse]="message.sender_id === currentUserId"
             class="flex mb-4">
          <div [class.ml-auto]="message.sender_id === currentUserId"
               class="max-w-[70%] bg-white rounded-lg p-3 shadow">
            <p>{{ message.content }}</p>
            <img *ngIf="message.media_url"
                 [src]="message.media_url"
                 class="max-w-full rounded-lg mt-2">
            <span class="text-xs text-gray-500">
              {{ message.created_at | date:'short' }}
            </span>
          </div>
        </div>
      </div>

      <div class="p-4 border-t">
        <div class="flex items-center">
          <input type="file"
                 (change)="handleFileInput($event)"
                 class="hidden"
                 #fileInput
                 accept="image/*,video/*">
          <button (click)="fileInput.click()"
                  class="p-2 text-gray-500 hover:text-gray-700">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13">
              </path>
            </svg>
          </button>
          <input [(ngModel)]="newMessage"
                 (keyup.enter)="sendMessage()"
                 placeholder="Type a message..."
                 class="flex-1 mx-2 p-2 border rounded-lg">
          <button (click)="sendMessage()"
                  class="bg-blue-500 text-white px-4 py-2 rounded-lg">
            Send
          </button>
        </div>
      </div>
    </div>
  `
})
export class ChatWindowComponent implements OnInit {
  @Input() chat!: Chat;
  messages: Message[] = [];
  newMessage = '';
  currentUserId = '';
  selectedFile: File | null = null;

  constructor(
    private chatService: ChatService
  ) {}

  ngOnInit() {
    if (this.chat) {
      this.loadMessages();
    }
  }

  loadMessages() {
    this.chatService.getMessages(this.chat.id).subscribe(
      messages => this.messages = messages,
      error => console.error('Error loading messages:', error)
    );
  }

  async sendMessage() {
    if (!this.newMessage.trim() && !this.selectedFile) return;

    try {
      let mediaUrl: string | undefined;

      if (this.selectedFile) {
        mediaUrl = await this.chatService.uploadMedia(this.selectedFile).toPromise();
      }

      await this.chatService.sendMessage(this.chat.id, this.newMessage, mediaUrl);
      this.newMessage = '';
      this.selectedFile = null;
    } catch (error) {
      console.error('Error sending message:', error);
    }
  }

  handleFileInput(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
    }
  }
}
