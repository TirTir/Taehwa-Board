import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoardListComponent } from './pages/board-list/board-list.component';
import { BoardDetailComponent } from './pages/board-detail/board-detail.component';
import { BoardCreateComponent } from './pages/board-create/board-create.component';
import { BoardEditComponent } from './pages/board-edit/board-edit.component';
const routes: Routes = [
  { path: '', component: BoardListComponent },
  { path: 'article', component: BoardCreateComponent },
  { path: ':id', component: BoardDetailComponent },
  { path: 'edit/:id', component: BoardEditComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BoardRoutingModule {}
