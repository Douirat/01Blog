// src/environments/validation-constants.ts
// International standard field length limits.
// Import this in your FormGroup definitions instead of hardcoding numbers.

export const VALIDATION = {

  name: {
    min: 1,
    max: 50,       // ISO/IEC common practice
  },

  email: {
    min: 6,
    max: 254,      // RFC 5321
  },

  password: {
    min: 8,
    max: 128,      // NIST SP 800-63B
  },

  nickname: {
    min: 3,
    max: 39,       // GitHub/Twitter convention
  },

  postTitle: {
    min: 3,
    max: 150,      // Common CMS standard
  },

  postContent: {
    min: 10,
    max: 5000,     // Common CMS standard
  },

  commentTitle: {
    min: 3,
    max: 100,
  },

  commentContent: {
    min: 1,
    max: 1000,
  },

  reportDetails: {
    min: 10,
    max: 500,
  },

} as const;