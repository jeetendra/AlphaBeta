import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {environment} from '../environment';
import {LoginResponse, User} from '../models/chat.model';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.checkAuthStatus());

  constructor(private http: HttpClient, private router: Router) {
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, {email, password})
      .pipe(
        map(response => {
          this.isAuthenticatedSubject.next(true);
          this.currentUserSubject.next(response.user); // Update currentUserSubject with user
          return response;
        })
      );
  }

  getCurrentUser(): Observable<User | null> {
    return this.currentUserSubject.asObservable();
  }

  isAuthenticated() {
    return this.isAuthenticatedSubject.asObservable();
  }

  logout(): void {
    localStorage.removeItem('auth_token'); // Remove token from localStorage
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  private checkAuthStatus(): boolean {
    return !!localStorage.getItem('auth_token'); // Check if token exists
  }
}
