// Report input interface:
export interface ReportInput{
    postId: number
    reporterId: number
    reason: string
}

// Report interface:
export interface Report {
  id: number
  postId: number        // what was reported
  reporterId: number   // who reported it
  reason: string       // free text
  status: "PENDING" | "RESOLVED" | "DISMISSED"
  createdAt: Date
}
