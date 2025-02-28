import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BoardRoutingModule } from './board-routing.module';
import { BoardListComponent } from './pages/board-list/board-list.component';
import { BoardDetailComponent } from './pages/board-detail/board-detail.component';
import { BoardCreateComponent } from './pages/board-create/board-create.component';
import { provideHttpClient } from '@angular/common/http';
import { BoardEditComponent } from './pages/board-edit/board-edit.component';

@NgModule({
  imports: [CommonModule, RouterModule, BoardRoutingModule, BoardListComponent, BoardDetailComponent, BoardCreateComponent, BoardEditComponent],
  providers: [provideHttpClient()],
  exports: [],
})
export class BoardModule {}
