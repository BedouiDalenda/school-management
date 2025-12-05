import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { PageResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private apiUrl = `${environment.apiUrl}/students`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10): Observable<PageResponse> {
    return this.http.get<PageResponse>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  create(student: any): Observable<any> {
    return this.http.post(this.apiUrl, student);
  }

  update(id: number, student: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, student);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  search(query: string): Observable<PageResponse> {
    return this.http.get<PageResponse>(`${this.apiUrl}/search?query=${query}`);
  }

  filterByLevel(level: string): Observable<PageResponse> {
    return this.http.get<PageResponse>(`${this.apiUrl}/filter?level=${level}`);
  }
}
