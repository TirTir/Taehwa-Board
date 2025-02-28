  import { Component, OnInit } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { BoardService, ResArticleDetail } from '../../services/board.service';
  import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';

  @Component({
    selector: 'app-board-detail',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './board-detail.component.html',
    styleUrls: ['./board-detail.component.css']
  })

  export class BoardDetailComponent implements OnInit {
    Article: ResArticleDetail | null = null;
    isOwner: boolean = false;

    constructor(
      private boardService: BoardService,
      private authService: AuthService,
      private route: ActivatedRoute,
      private router: Router
    ) {}

    ngOnInit(): void {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      if (isNaN(id)) {
        console.error("존재하지 않는 게시글 ID");
        return;
      }

      this.boardService.getArticle(id).subscribe({
        next: (data) => {
          console.log("Response 데이터:", data);
          this.Article = data;
          
          const loggedInUser = this.authService.getLoggedInUserName(); // 현재 로그인된 사용자 가져오기
          this.isOwner = loggedInUser === this.Article.userName;
        },
        error: (error) => {
          console.error("API 호출 실패:", error);
        }
      });
    }

    goToDetail(id: number): void {
      this.router.navigate(['/board', id]);
    }

    goBack(): void {
      this.router.navigate(['/board']);
    }

    editArticle(): void {
      if (this.Article) {
        this.router.navigate(['/board/edit', this.Article.id]);
      }
    }

    deleteArticle(): void {
      if (confirm('정말 삭제하시겠습니까?')) {
        if (this.Article) {
          this.boardService.deleteArticle(this.Article.id).subscribe(() => {
            alert('삭제되었습니다.');
            this.router.navigate(['/board']);
          });
        }
      }
    }
  }