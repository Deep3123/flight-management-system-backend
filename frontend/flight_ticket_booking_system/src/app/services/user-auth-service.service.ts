import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserAuthServiceService {
  private baseUrl: string = 'http://localhost:8080'; // Make sure this is the correct API base URL

  constructor(private http: HttpClient) { }

  // Send the registration data to the backend API
  saveUserData(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/register`, user); // Adjust endpoint if needed
  }

  userLogin(params: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/login`, params); // Adjust endpoint if needed
  }
}
