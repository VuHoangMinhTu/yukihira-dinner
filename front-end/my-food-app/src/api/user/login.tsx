import { request } from "../api";
export interface UserResponseDTO {
    code: number;
    result: string;
}

export const login = async (username: string, password: string): Promise<UserResponseDTO> => {
  try {
    const response = await request("/api/auth/login", { username, password }, "POST");
    return response;
  } catch (error) {
    throw new Error(`Login failed: ${error}`);
  }
};
