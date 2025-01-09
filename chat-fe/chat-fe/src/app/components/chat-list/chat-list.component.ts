import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ChatService } from '../../services/chat.service';
import { Chat } from '../../models/chat.model';
import { ChatWindowComponent } from '../chat-window/chat-window.component';

@Component({
  selector: 'app-chat-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ChatWindowComponent],
  template: `
    <div class="h-screen flex">
      <div class="h-full w-80 border-r border-gray-200 bg-white">
        <div class="p-4 border-b">
          <h2 class="text-lg font-semibold">Chats</h2>
          <button (click)="createNewChat()" class="mt-2 w-full bg-blue-500 text-white p-2 rounded">
            New Chat
          </button>
        </div>
        <div class="overflow-y-auto h-[calc(100%-4rem)]">
          <div *ngFor="let chat of chats"
               (click)="selectChat(chat)"
               class="p-4 border-b hover:bg-gray-50 cursor-pointer">
            <div class="flex items-center">
              <div *ngIf="chat.group" class="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                <span class="text-sm">{{ chat.name?.charAt(0) }}</span>
              </div>
              <div class="ml-3">
                <p class="font-medium">{{ chat.group ? chat.name : chat.participants[0].username }}</p>
<!--                <p class="text-sm text-gray-500">{{ chat.last_message?.content }}</p>-->
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-1">
        <app-chat-window *ngIf="selectedChat" [chat]="selectedChat"></app-chat-window>
      </div>
    </div>
  `
})
export class ChatListComponent implements OnInit {
  chats: Chat[] = [];
  selectedChat: Chat | null = null;

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    this.loadChats();
  }

  async loadChats() {
    this.chatService.getChats().subscribe(
      chats => this.chats = chats,
      error => console.error('Error loading chats:', error)
    );
  }

  selectChat(chat: Chat) {
    this.selectedChat = chat;
  }

  createNewChat() {
    // Handle new chat creation
  }
}
