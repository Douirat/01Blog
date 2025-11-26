import { Comment } from "./comment";
/*
Create a typescript type definition for a blog post object.
*/

// the input type describing the input data only:
export interface PostInput {
  title: string;
  content: string;
  mediaType?: 'image' | 'video';
  media?: File;
}

interface PostAuthor {
  id: number;
  nickName: string;
}

// Full Post type returned by the backend (includes user, comments, votes):
export interface Post {
  id: number;
  title: string;
  content: string;
  mediaType?: 'image' | 'video';
  mediaUrl?: string | null;
  user: PostAuthor;   // full user object (id, nickname, etc.)
  likes: number;
  dislikes: number;
  commentsCount: number;
  createdAt: string;
}

/*
The "mediaUrl" field contains the URL of the media file stored on the backend server.

Example Post object returned by the backend:
{
  "id": 1,
  "title": "My Post",
  "content": "Hello world",
  "mediaType": "image",
  "mediaUrl": "http://localhost:8080/uploads/12345-image.png",
  "user": {
    "id": 3,
    "nickName": "JohnDoe"
  },
  "createdAt": "2025-11-16T15:23:00",
  "likes": 5,
  "dislikes": 1,
  "commentsCount": 0
}

Key points:
1. After creating a post with media, the backend stores the file and returns a URL pointing to it.
2. The frontend can use this URL to display the media:
   Example: <img src={post.mediaUrl} alt="Post media" />
3. The browser fetches the media directly from the backend using this URL.
   Example: GET /uploads/12345-image.png
4. This approach avoids sending the actual media data inside the JSON response.
*/

export interface PaginatedPosts {
  content: Post[];       // Posts in the current page
  last: boolean;         // True if this is the last page
  totalPages: number;    // Total number of pages available
  totalElements: number; // Total number of posts across all pages
}
// add a commented explanation to the paginated interface 