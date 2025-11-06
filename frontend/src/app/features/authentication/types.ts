// TypeScript interface representing a User entity:
export interface User{
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
export interface UserDTO{
    readonly id: string;
    email: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string; // ISO format date string
    avatar?: string; // URL to avatar image (optional)
    nickname: string;
    isAdmin?: boolean;
}

// Typescript interface representing the user response:
export interface UserResponse{
    readonly user: UserDTO;
    readonly token: string;
}

// Typescript interface to represent the login payload:
export interface LoginPayload{
    email: string;
    password: string;
}
