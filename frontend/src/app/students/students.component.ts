import { Component, OnInit } from '@angular/core';
import { StudentService } from '../services/student.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Student } from '../models/models';

@Component({
  selector: 'app-students',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './students.component.html'
})

export class StudentsComponent implements OnInit {
  searchQuery = '';
  selectedLevel = '';
  levels = ['FRESHMAN', 'SOPHOMORE', 'JUNIOR', 'SENIOR', 'GRADUATE'];
  students: Student[] = [];
  currentStudent: Student = {
    username: '', level: ''
  };
  
  // Form & UI state
  showForm = false;
  editMode = false;
  loading = false; // pour désactiver boutons et afficher spinner
  errorMessage = ''; // pour afficher les erreurs

  constructor(
    private studentService: StudentService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    this.loading = true;
    this.errorMessage = '';
    this.studentService.getAll().subscribe({
      next: res => { this.students = res.content; this.loading = false; },
      error: err => { this.errorMessage = err?.error?.message || 'Failed to load students'; this.loading = false; }
    });
  }

  search() {
    this.loading = true;
    this.errorMessage = '';
    if (this.searchQuery) {
      this.studentService.search(this.searchQuery).subscribe({
        next: res => { this.students = res.content; this.loading = false; },
        error: err => { this.errorMessage = err?.error?.message || 'Search failed'; this.loading = false; }
      });
    } else {
      this.loadStudents();
    }
  }

  filter() {
    this.loading = true;
    this.errorMessage = '';
    if (this.selectedLevel) {
      this.studentService.filterByLevel(this.selectedLevel).subscribe({
        next: res => { this.students = res.content; this.loading = false; },
        error: err => { this.errorMessage = err?.error?.message || 'Filter failed'; this.loading = false; }
      });
    } else {
      this.loadStudents();
    }
  }

  openForm(student?: Student) {
    this.showForm = true;
    this.errorMessage = '';
    if (student) {
      this.editMode = true;
      this.currentStudent = { ...student };
    } else {
      this.editMode = false;
      this.currentStudent = { username: '', level: '' };
    }
  }

  closeForm() {
    this.showForm = false;
    this.errorMessage = '';
  }

  save() {
    this.loading = true;
    this.errorMessage = '';
    const obs = this.editMode
      ? this.studentService.update(this.currentStudent.id!, this.currentStudent)
      : this.studentService.create(this.currentStudent);

    obs.subscribe({
      next: () => { this.loadStudents(); this.closeForm(); this.loading = false; },
      error: err => { this.errorMessage = err?.error?.message || 'Save failed'; this.loading = false; }
    });
  }

  delete(id?: number) {
  if (!id) return; // ne fait rien si id est undefined ou 0
  
    if (!confirm('Delete this student?')) return;

    this.loading = true;
    this.errorMessage = '';
    this.studentService.delete(id).subscribe({
      next: () => { this.loadStudents(); this.loading = false; },
      error: err => { this.errorMessage = err?.error?.message || 'Delete failed'; this.loading = false; }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
