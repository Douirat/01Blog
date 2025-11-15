import { Comment } from "./comment";
/*
Create a typescript type definition for a blog post object.
*/ 

// the input type describing the input data only:
export interface PostInput{
    title: string;
    content: string;
    mediaType?: 'image' | 'video';
    media?: File 
}

// Full Post type returned by backend (includes user, comments, votes)
export interface Post {
  id: number;
  title: string;
  content: string;
  mediaType?: string;
  media: File | null;
  user: number;          // Populated by backend
  createdAt: string;   // ISO string
  comments?: Comment[];
  likes: number;
  dislikes: number;
}