// Report input interface:
export interface ReportInput{
    postId: number | null
    reporterId: number | null
    reason: string | null
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

/**
 * Represents a paginated response from the backend for a list of reports.
 * This maps closely to Spring Data's Page<T> structure.
 */
export interface PaginatedReports {

  /**
   * The actual reports returned in the current page.
   * - This is an array of report objects.
   */

  content: Report[];

  /**
   * Indicates whether this is the last page of results.
   * - True if there are no more pages after this one.
   * - Useful for preventing additional requests once all data has been fetched.
   */

  last: boolean;

  /**
   * Total number of pages available given the current page size.
   * - Helps in creating pagination controls (e.g., page numbers, next/previous buttons).
   * - Example: 50 total reports, 10 reports per page → totalPages = 5
   */

  totalPages: number;

  /**
   * Total number of reports across all pages, not just the current page.
   * - Useful for showing "Showing X of Y reports" UI.
   * - Example: 50 reports in total, even if only 10 are in the current page.
   */

  totalElements: number;
}


