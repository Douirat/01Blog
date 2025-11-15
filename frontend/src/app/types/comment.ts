/*
Vote type definition for a blog postding system.
*/
// Full Comment type returned by the backend
export interface Comment {
  id: number;
  content: string;
  userId?: number;   // Populated by backend and needed only when retreived.
  createdAt: string; 
}