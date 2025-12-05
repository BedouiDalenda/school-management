import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  loading = false; // Pour désactiver le bouton pendant la requête

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.loading = true; // début de la requête
    this.error = '';

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/students']);
      },
      error: (err) => {
        this.loading = false;
        
        // Afficher le message du backend si disponible
        this.error = err?.error?.message || 'Invalid credentials';
      }
    });
  }
}
