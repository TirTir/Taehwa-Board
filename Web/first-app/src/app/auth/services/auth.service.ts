import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface RegisterRequestDto {
  userName: string;
  email: string;
  password: string;
}

export interface AuthRequestDto {
  userName: string;
  password: string;
}

export interface CommonResponse<T = any> {
  success: boolean;
  code: string;
  data?: T;
  message?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api';

  private loggedIn = new BehaviorSubject<boolean>(this.checkLoginStatus());
  isLoggedIn$ = this.loggedIn.asObservable(); // Observable로 공개

  constructor(private http: HttpClient) {}

  private checkLoginStatus(): boolean {
    return !!localStorage.getItem('userName');
  }

  // 회원가입 API 호출
  signup(request: RegisterRequestDto): Observable<CommonResponse<any>> {
    return this.http.post<CommonResponse<any>>(
      `${this.apiUrl}/signup`,
      request
    );
  }

  // 로그인 API 호출
  signin(
    credentials: AuthRequestDto
  ): Observable<CommonResponse<{ userName: string }>> {
    return this.http
      .post<CommonResponse<{ userName: string }>>(
        `${this.apiUrl}/signin`,
        credentials,
        {
          withCredentials: true,
        }
      )
      .pipe(
        tap((response) => {
          if (response.success) {
            localStorage.setItem('userName', credentials.userName);
            this.loggedIn.next(true);
          }
        })
      );
  }

  // 로그아웃
  logout(): Observable<CommonResponse<any>> {
    return this.http
      .post<CommonResponse<any>>(
        `${this.apiUrl}/logout`,
        {},
        { withCredentials: true }
      )
      .pipe(
        tap(() => {
          localStorage.removeItem('userName'); // 저장된 userName 제거
          this.loggedIn.next(false); // 로그인 상태 false로 변경
        })
      );
  }

  getLoggedInUserName(): string | null {
    return localStorage.getItem('userName');
  }
}
