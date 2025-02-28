import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BoardService } from '../../services/board.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
interface WriteArticle {
  title: string;
  content: string;
}

@Component({
  selector: 'app-board-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './board-create.component.html',
  styleUrls: ['./board-create.component.css'],
})
export class BoardCreateComponent {
  // 폼에 바인딩할 데이터 모델
  article: WriteArticle = {
    title: '',
    content: '',
  };

  constructor(private boardService: BoardService, private router: Router) {}

  onSubmit(): void {
    if (!this.article.title || !this.article.content) {
      alert('제목, 내용을 모두 입력해주세요.');
      return;
    }

    this.boardService
      .createArticle({
        title: this.article.title,
        content: this.article.content,
      })
      .subscribe({
        next: (res) => {
          alert('게시글이 성공적으로 작성되었습니다.');
          this.router.navigate(['/board']);
        },
        error: (err) => {
          console.error('게시글 작성 중 오류 발생:', err);
          alert('게시글 작성에 실패했습니다.');
        },
      });
  }

  goBack(): void {
    this.router.navigate(['/board']);
  }
}
