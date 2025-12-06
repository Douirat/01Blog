export interface VoteResponse {
  id: number | null;
  success: boolean;
  message: string;
}

export interface VoteRequest{
      postId: number,
      value: boolean
}