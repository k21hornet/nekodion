import { isRedirectError } from "next/dist/client/components/redirect-error";
import { CREATED, NO_CONTENT } from "./const";
import { auth0 } from "@/lib/auth0";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export const fetcher = {
  async get(path: string, body = {}, accessToken?: string) {
    return await this._call("GET", path, body, accessToken);
  },

  async post(path: string, body: unknown, accessToken?: string) {
    return await this._call("POST", path, body, accessToken);
  },

  async put(path: string, body: unknown, accessToken?: string) {
    return await this._call("PUT", path, body, accessToken);
  },

  async patch(path: string, body: unknown, accessToken?: string) {
    return await this._call("PATCH", path, body, accessToken);
  },

  async delete(path: string, accessToken?: string) {
    return await this._call("DELETE", path, {}, accessToken);
  },

  async _call(
    method: string,
    path: string,
    requestBody: unknown,
    accessToken?: string,
  ) {
    const fullUrl = `${BASE_URL}${path}`;

    const _fetch = async () => {
      let timer: ReturnType<typeof setTimeout> | undefined;
      // タイムアウト処理
      try {
        const controller = new AbortController();
        const abort = () => controller.abort();
        const TIME_LIMIT = 10000; // タイムアウト時間（ミリ秒）
        timer = setTimeout(abort, TIME_LIMIT);

        const customAccessToken =
          accessToken ?? (await auth0.getAccessToken()).token;

        const isFormData = requestBody instanceof FormData;
        const headers: Record<string, string> = {
          Authorization: `Bearer ${customAccessToken}`,
        };
        if (!isFormData) {
          headers["Content-Type"] = "application/json";
        }

        const body =
          method === "GET"
            ? undefined
            : isFormData
              ? requestBody
              : JSON.stringify(requestBody);

        const response = await fetch(fullUrl, {
          method,
          headers,
          body,
          signal: controller.signal,
          cache: "no-store",
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        return { response };
      } catch (error) {
        if (isRedirectError(error)) {
          throw error; // リダイレクトを実行
        }
        if (
          error instanceof Error &&
          (error as { digest?: string }).digest === "DYNAMIC_SERVER_USAGE"
        ) {
          throw error; // Next.jsの動的レンダリング検出を伝播させる
        }
        return { error };
      } finally {
        if (timer) {
          clearTimeout(timer);
        }
      }
    };

    // レスポンスデータの取得
    const { response, error } = await _fetch();
    const status = response?.status as number;

    if (status === NO_CONTENT || status === CREATED) {
      return { statusCode: status };
    }

    if (error) {
      return { error, statusCode: status };
    }

    const contentType = response?.headers.get("Content-Type") ?? "";
    const body = contentType.includes("application/json")
      ? await response?.json()
      : await response?.text();

    return { body, statusCode: status };
  },
};
