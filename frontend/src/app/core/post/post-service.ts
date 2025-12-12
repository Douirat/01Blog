import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { PostInput, PaginatedPosts } from '../../types/post';
import { Observable } from 'rxjs';
import { VoteRequest, VoteResponse } from '../../types/vote';


@Injectable({
  providedIn: 'root',
})

export class PostService {
  private readonly apiUrl = `${environment.apiUrl}/api/posts`
  private readonly apiUrlProfile = `${environment.apiUrl}/api/posts/profile`
  private readonly voteApiUrl = `${environment.apiUrl}/api/votes`

  constructor(private http: HttpClient) { }

  // method to create a post:
  createPost(postData: PostInput): Observable<any> { // TODO: don't forget to enforce types on the returned value as well.
    const formData = new FormData();
    formData.append('title', postData.title);
    formData.append('content', postData.content);

    if (postData.mediaType) {
      formData.append('mediaType', postData.mediaType);
    }
    if (postData.media) {
      formData.append('media', postData.media);
    }

    return this.http.post(`${this.apiUrl}`, formData);
  }

  // A general full accessed method to fetch posts from backend:
  fetchPosts(page: number, userId: number | undefined = undefined): Observable<PaginatedPosts> {
    let params = new HttpParams().set('page', page.toString());

    if (userId !== undefined) {
      params = params.set('userId', userId.toString());
      return this.http.get<PaginatedPosts>(this.apiUrlProfile, { params });
    }
    return this.http.get<PaginatedPosts>(this.apiUrl, { params });
  }

  createVote(vote: VoteRequest): Observable<VoteResponse> {
    return this.http.post<VoteResponse>(this.voteApiUrl, vote);
  }

}
