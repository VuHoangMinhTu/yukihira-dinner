import { request } from "../api";
export interface UserDetailsResponseDTO {
    code: number;
    result: {
        "fullName": string,
        "phoneNum": string,
        "dateOfBirth": string,
        "userId":number
    }
}
export const getUserDetails = async (): Promise<UserDetailsResponseDTO| null> => {
  const url = "/api/user-details";
  const res = await request(url,null,"GET");
  if(res?.code === 200){
    return res.data;
  }
  return null;
};
// export const addUserDetails = async (fullName: string, phoneNum: string, dateOfBirth: string | null): Promise<UserDetailsResponseDTO> => {
//   const url = API_ENDPOINTS.USER.DETAILS.ADD;
//   const res = await axiosInstance.post(url, {
//     fullName,
//     phoneNum,
//     dateOfBirth 
//   });
//   return res.data;
// };
export const changeUserPassword = async (newPassword: string, confirmPassword: string): Promise<void> => {
  const url = "/api/user-details/reset-password";
  await request(url, { newPassword, confirmPassword }, "PUT");
};