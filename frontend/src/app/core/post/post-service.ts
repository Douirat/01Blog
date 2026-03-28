import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { PostInput, PaginatedPosts } from '../../types/post';
import { Observable } from 'rxjs';
import { VoteRequest, VoteResponse } from '../../types/vote';
import { Post } from '../../types/post';
import { UserDTO } from '../../types/user';

@Injectable({
  providedIn: 'root',
})

export class PostService {
  private readonly apiUrl = `${environment.apiUrl}/api/posts`
  private readonly apiUrlProfile = `${environment.apiUrl}/api/posts/profile`
  private readonly voteApiUrl = `${environment.apiUrl}/api/votes`

  constructor(private http: HttpClient) { }

  // method to create a post:
  createPost(postData: PostInput): Observable<Post> {
    const formData = new FormData();
    formData.append('title', postData.title);
    formData.append('content', postData.content);

    if (postData.mediaType) {
      formData.append('mediaType', postData.mediaType);
    }
    if (postData.media) {
      formData.append('media', postData.media);
    }

    return this.http.post<Post>(`${this.apiUrl}`, formData);
  }

  // A general full accessed method to fetch posts from backend:
  fetchPosts(page: number, userId: number | undefined = undefined): Observable<PaginatedPosts> {
    let params = new HttpParams().set('page', page.toString());

    if (userId !== undefined) {
      params = params.set('userId', userId.toString());
    }
    return this.http.get<PaginatedPosts>(this.apiUrl, { params });
  }

  //  a method to update a post:
  updatePost(postId: number, postData: PostInput): Observable<Post> {
    const formData = new FormData();


    formData.append('title', postData.title);
    formData.append('content', postData.content);

    if (postData.mediaType) {
      formData.append('mediaType', postData.mediaType);
    }
    if (postData.media) {
      formData.append('media', postData.media);
    }

    return this.http.put<Post>(
      `${this.apiUrl}/${postId}`,
      formData
    );
  }

  createVote(vote: VoteRequest): Observable<VoteResponse> {
    return this.http.post<VoteResponse>(this.voteApiUrl, vote);
  }

  // Get user by postId:
getUserByPostId(postId: number): Observable<UserDTO> {
  let params = new HttpParams().set("postId", postId.toString());
  return this.http.get<UserDTO>(this.apiUrl + "/user", { params });
}

// get reported posts:
getReportedPosts(page: number, userId: number | undefined): Observable<PaginatedPosts> {
  let params = new HttpParams()
    .set("page", page.toString());

  if (userId) {
    params = params.set("userId", userId.toString());
  }

  return this.http.get<PaginatedPosts>(this.apiUrl + "/reports", { params });
}

// ban a post:
banPost(postId: number): Observable<string> {
  const params = new HttpParams().set("id", postId.toString());

  return this.http.patch<string>(
    this.apiUrl,
    null,
    { params }
  );
}

}
