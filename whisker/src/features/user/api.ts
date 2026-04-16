import { fetcher } from "@/util/fetcher";

// 新規ユーザーがログインした時にユーザー情報を登録
export async function signupUser(accessToken: string) {
  return await fetcher.post(`/auth/signup`, undefined, accessToken);
}
