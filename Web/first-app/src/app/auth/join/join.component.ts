import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService, RegisterRequestDto } from '../services/auth.service';

@Component({
  selector: 'app-join',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.css']
})

export class JoinComponent {
  userName = '';
  email = '';
  password = '';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  onJoin(): void {
    const request: RegisterRequestDto = {
      userName: this.userName,
      email: this.email,
      password: this.password
    };

    this.authService.signup(request).subscribe({
      next: (response) => {
        if (response.success) {
          alert('회원가입이 완료되었습니다!');
          this.router.navigate(['/auth/login']);
        } else {
          alert('회원가입에 실패하였습니다: ' + (response.message || ''));
        }
      },
      error: (err) => {
        console.error(err);
        alert('회원가입 중 오류가 발생했습니다.');
      }
    });
  }
}