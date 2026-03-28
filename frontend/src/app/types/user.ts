// TypeScript interface representing a User entity:
export interface User {
    readonly id?: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string; // ISO format date string
    avatar?: string; // URL to avatar image (optional)
    nickname: string;
    isAdmin?: boolean;
}

// the UserDTO frontend type representer:
export interface UserDTO {
    readonly id: string;
    email: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string; // ISO format date string
    avatar?: File; // image (optional)
    nickname: string;
    isAdmin?: boolean;
    banned: boolean;
}

// create an interface to represent the paginated users:
export interface PaginatedUsers {

    content: UserDTO[];

    last: boolean;

    totalPages: number;

    totalElements: number;
}

// Typescript interface representing the user response:
export interface UserResponse {
    readonly user: UserDTO;
    readonly token: string;
}

// Typescript interface to represent the login payload:
export interface LoginPayload {
    emailOrUsername: string;
    password: string;
}

export interface RegistrationFormData {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    nickname: string;
    dateOfBirth: string;
    avatar?: File;
}

