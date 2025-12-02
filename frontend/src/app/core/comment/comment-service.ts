import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { CommentRequest, CommentResponse, Comment } from '../../types/comment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private readonly apiUrl = `${environment.apiUrl}/api/comments`

  constructor(private http: HttpClient) { }

  // a method to request the creation of a comment:
  createComment(comment: CommentRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(this.apiUrl, comment);
  }
}
