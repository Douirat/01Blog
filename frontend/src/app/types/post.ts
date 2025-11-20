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


// Full Post type returned by backend (includes user, comments, votes):
export interface Post {
  id: number;
  title: string;
  content: string;
  mediaType?: 'image' | 'video';
  mediaUrl?: string | null;       // URL to file on backend.
  user: number;                   // ID or full User object (depends).
  createdAt: string;              // ISO date.
  // comments?: Comment[]; this is not valid cause it dos't respect that the user will have to feth the comments separatly based on the scroll 10/10.
  /* likes: number; 
   dislikes: number;
  just like the comments, likes and dislike wil have to be fetched separatly based on the user interaction.
*/
}

/*
"mediaUrl": "http://localhost:8080/uploads/post-14-image.png"
the json only contains the URL to the media file stored on the backend server.

Example Post object:
{
  "id": 1,
  "title": "My Post",
  "content": "Hello world",
  "mediaType": "image",
  "mediaUrl": "http://localhost:8080/uploads/12345-image.png",
  "user": 3,
  "createdAt": "2025-11-16T15:23:00",
  "likes": 5,
  "dislikes": 1,
  "comments": []
}
  after the post is created, the backend returns the URL to access the uploaded media file.
  This URL can be used to display the media content in the frontend.
  The browser fetches the image directly from the backend using this URL.
  exmple: <img src={post.mediaUrl} />
  GET /uploads/post-14-image.png
and the backend serves the file.
*/
