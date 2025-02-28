import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BoardService, ResArticle } from '../../services/board.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-board-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './board-list.component.html',
  styleUrls: ['./board-list.component.css']
})
export class BoardListComponent implements OnInit {
  Article: ResArticle[] = [];

  constructor(private boardService: BoardService, private router: Router) {}

  ngOnInit(): void {
    this.boardService.getArticleList().subscribe({
      next: (response) => {
        console.log("Response 데이터:", response);
        this.Article = response.slice(0, 10);
      },
      error: (error) => {
        console.error("API 호출 실패:", error);
      }
    });
  }
  

  goToDetail(id: number): void {
    this.router.navigate(['/board', id]);
  }
}
