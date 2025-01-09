import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {firstValueFrom} from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
      <div class="max-w-md w-full space-y-8 p-8 bg-white rounded-lg shadow">
        <h2 class="text-center text-3xl font-bold">Sign in to Chat</h2>
        <form (ngSubmit)="onSubmit()" class="mt-8 space-y-6">
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
            <input
              [(ngModel)]="email"
              name="email"
              type="email"
              required
              class="mt-1 block w-full rounded-md border border-gray-300 p-2"
            >
          </div>
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input
              [(ngModel)]="password"
              name="password"
              type="password"
              required
              class="mt-1 block w-full rounded-md border border-gray-300 p-2"
            >
          </div>
          <div>
            <button
              type="submit"
              class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
            >
              Sign in
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class LoginComponent {
  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
  }

  async onSubmit() {
    try {
      const response = await firstValueFrom(this.authService.login(this.email, this.password));
      const token = response.token; // Assuming the response contains a token
      localStorage.setItem('auth-_token', token);
      localStorage.setItem('user_id' , response.user.id);
      this.router.navigate(['/chats']);
    } catch (error) {
      console.error('Login failed:', error);
      // Display user-friendly error message
    }
  }
}
