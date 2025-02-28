import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { JoinComponent } from './join/join.component';
import { provideHttpClient } from '@angular/common/http';
import { AuthRoutingModule } from './auth-routing.module';
@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    AuthRoutingModule,
    LoginComponent,
    JoinComponent,
  ],
  providers: [provideHttpClient()],
  exports: [],
})
export class AuthModule {}
