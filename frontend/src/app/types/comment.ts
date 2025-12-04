/*
Vote type definition for a blog postding system.
*/
// Full Comment type returned by the backend

// The comment author:
export interface CommentAuthor {
  id: number;
  nickname: string;
}

// a contract to receive the comment from the backend:
export interface Comment {
  id: number;
  content: string;
  createdAt: string;
  author: CommentAuthor;
}


// a contract to create a comment:
export interface CommentRequest {
  title: string;
  content: string;
  postId: number | null;
}

export interface CommentResponse {
    id: number;
    success: boolean;
    message: string;
}

// ceate a paginated contract to ease data retrieval:
export interface PaginatedCommentsDTO {
  content: Comment[];
  last: boolean;
  totalPages: number;
  totalElements: number;
}