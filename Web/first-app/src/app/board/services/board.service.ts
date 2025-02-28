import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';

export interface ResArticle {
  id: number;
  title: string;
  userName: string;
  updatedAt: string;
}

export interface ResArticleDetail {
  id: number;
  title: string;
  content: string;
  userName: string;
  updatedAt: string;
}

export interface ReqArticle {
  title: string;
  content: string;
}

export interface ApiResponse<T> {
  message: string;
  data: T;
  success: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class BoardService {
  private apiUrl = 'http://localhost:8080/article';

  constructor(private http: HttpClient) {}

  // 게시글 목록 가져오기
  getArticleList(): Observable<ResArticle[]> {
    return this.http
      .get<ApiResponse<ResArticle[]>>(`${this.apiUrl}/`)
      .pipe(map((response: ApiResponse<ResArticle[]>) => response.data));
  }

  // 특정 게시글 상세보기
  getArticle(id: number): Observable<ResArticleDetail> {
    return this.http
      .get<ApiResponse<ResArticleDetail>>(`${this.apiUrl}/detail/${id}`)
      .pipe(map((response: ApiResponse<ResArticleDetail>) => response.data));
  }

  // 게시글 등록
  createArticle(article: ReqArticle): Observable<ResArticle> {
    return this.http
      .post<ApiResponse<ResArticle>>(`${this.apiUrl}/create`, article, {
        withCredentials: true,
      })
      .pipe(map((response: ApiResponse<ResArticle>) => response.data));
  }

  // 게시글 수정
  updateArticle(id: number, article: ReqArticle): Observable<ResArticle> {
    return this.http
      .put<ApiResponse<ResArticle>>(`${this.apiUrl}/update/${id}`, article, {
        withCredentials: true,
      })
      .pipe(map((response: ApiResponse<ResArticle>) => response.data));
  }

  // 게시글 삭제
  deleteArticle(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.apiUrl}/${id}`, {
        withCredentials: true,
      })
      .pipe(map((response: ApiResponse<void>) => response.data));
  }
}
