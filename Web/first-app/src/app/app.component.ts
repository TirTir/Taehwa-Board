  import { Component } from '@angular/core';
  import { Router } from '@angular/router'
  import { AuthService } from './auth/services/auth.service';

  @Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: false,
    styleUrl: './app.component.css',
  })

  export class AppComponent {
    title = 'first-app';  
    isLoggedIn = false;

    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {
      this.authService.isLoggedIn$.subscribe((isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      });
    }

    onLogout(event: Event): void {
      event.preventDefault();
      this.authService.logout().subscribe(() => {
        this.isLoggedIn = false;
        this.router.navigate(['/auth/login']);
      });
    }
  }
