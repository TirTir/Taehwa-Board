import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BoardService } from '../../services/board.service';

interface WriteArticle {
  title: string;
  content: string;
}

@Component({
  selector: 'app-board-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './board-edit.component.html',
  styleUrls: ['./board-edit.component.css'],
})
export class BoardEditComponent implements OnInit {
  articleId!: number;
  article: WriteArticle = {
    title: '',
    content: '',
  };

  constructor(
    private boardService: BoardService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // URL에서 게시글 ID 가져오기
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.articleId = +id;
      this.loadArticle(this.articleId);
    }
  }

  // 기존 게시글 불러오기
  private loadArticle(id: number): void {
    this.boardService.getArticle(id).subscribe({
      next: (res) => {
        this.article.title = res.title;
        this.article.content = res.content;
      },
      error: (err) => {
        console.error('게시글 불러오기 실패:', err);
        alert('게시글 정보를 불러올 수 없습니다.');
        this.router.navigate(['/board']); // 오류 발생 시 목록으로 이동
      },
    });
  }

  // 게시글 수정
  onSubmit(): void {
    if (!this.article.title || !this.article.content) {
      alert('제목과 내용을 입력해주세요.');
      return;
    }

    this.boardService
      .updateArticle(this.articleId, {
        title: this.article.title,
        content: this.article.content,
      })
      .subscribe({
        next: () => {
          alert('게시글이 성공적으로 수정되었습니다.');
          this.router.navigate(['/board']);
        },
        error: (err) => {
          console.error('게시글 수정 중 오류 발생:', err);
          alert('게시글 수정에 실패했습니다.');
        },
      });
  }

  goBack(): void {
    this.router.navigate(['/board']);
  }
}
