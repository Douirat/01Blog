export interface VoteResponse {
  postDTO: any;
  success: boolean;
  message: string;
}

export interface VoteRequest{
      postId: number,
      value: boolean
}