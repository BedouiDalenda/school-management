
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
}

export interface Student {
  id?: number;
  username: string;
  level: string;
}

export interface PageResponse {
  content: Student[];
  totalElements: number;
  totalPages: number;
}