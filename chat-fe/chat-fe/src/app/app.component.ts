import {Component, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {AuthService} from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: '<router-outlet></router-outlet>',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'TOMoto';

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.authService.isAuthenticated().subscribe(isAuthenticated => {
      if (!isAuthenticated && this.router.url != '/login') {
        this.router.navigate(['/login']);
      }
    });
  }
}
