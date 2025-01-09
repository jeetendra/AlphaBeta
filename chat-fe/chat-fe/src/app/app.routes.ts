import {Routes} from '@angular/router';

import {ChatListComponent} from './components/chat-list/chat-list.component';
import {authGuard} from './guards/auth.guard';
import {LoginComponent} from './components/login/login.commponent';

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {
    path: 'chats',
    component: ChatListComponent,
    canActivate: [authGuard]
  },
  {path: '', redirectTo: '/chats', pathMatch: 'full'}
];
