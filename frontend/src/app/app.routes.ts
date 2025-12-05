import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { StudentsComponent } from './students/students.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
 { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'students', component: StudentsComponent, canActivate: [AuthGuard] }
];
