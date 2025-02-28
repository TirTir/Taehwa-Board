import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService, AuthRequestDto, CommonResponse } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent {
  userName = ''; 
  password = '';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  onLogin(): void {
    
    const request: AuthRequestDto = {
      userName: this.userName,
      password: this.password
    };

    this.authService.signin(request).subscribe({
      next: (response: CommonResponse<{ userName: string }>) => {
        if (response.success) {
          alert('로그인 성공!');
          this.router.navigate(['/board']); // 게시판으로 이동
        } else {
          alert('로그인 실패: ' + (response.message || '알 수 없는 오류'));
        }
      },
      error: (err) => {
        console.error('로그인 실패:', err);
        alert('로그인 중 오류가 발생했습니다.');
      }
    });
  }
}