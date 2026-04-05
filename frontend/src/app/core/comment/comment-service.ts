import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../environment/environment';
import { CommentRequest, CommentResponse, Comment, PaginatedCommentsDTO } from '../../types/comment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private readonly apiUrl = `${environment.apiUrl}/api/comments`
  private commentSource = new Subject<Comment>();
  commentSource$ = this.commentSource.asObservable();

  constructor(private http: HttpClient) { }

  // a method to request the creation of a comment:
  createComment(comment: CommentRequest): Observable<Comment> {
    return this.http.post<Comment>(this.apiUrl, comment);
  }

    getComments(postId: number, page: number): Observable<PaginatedCommentsDTO> {
    const params = new HttpParams()
      .set('postId', postId)
      .set('page', page)

    return this.http.get<PaginatedCommentsDTO>(this.apiUrl, { params });
  }

  imitateComment(comment: Comment):void{
    this.commentSource.next(comment);
  }
}
