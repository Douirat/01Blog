/*
Vote type definition for a blog postding system.
*/
// Full Comment type returned by the backend

// The comment author:
export interface CommentAuthor {
  id: number;
  nickName: string;
}

// a contract to receive the comment from the backend:
export interface Comment {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  author: CommentAuthor;
}


// a contract to create a comment:
export interface CommentRequest {
  title: string;
  content: string;
  postId: number;
}

export interface CommentResponse {
    id: number;
    success: boolean;
    message: string;
}
