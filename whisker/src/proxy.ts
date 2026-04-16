import { NextResponse, type NextRequest } from "next/server";

import { auth0 } from "./lib/auth0";

export async function proxy(request: NextRequest) {
  const response = await auth0.middleware(request);

  // 認証ルートはAuth0に処理を委譲
  if (request.nextUrl.pathname.startsWith("/auth")) {
    return response;
  }

  // ログインエラーページは認証チェックをスキップ
  if (request.nextUrl.pathname === "/error/access-denied") {
    return response;
  }

  // セッションチェック（ルートページは認証なしでアクセス可能）
  const session = await auth0.getSession(request);
  if (!session && request.nextUrl.pathname !== "/") {
    return NextResponse.redirect(
      new URL(
        `/auth/login?returnTo=${request.nextUrl.pathname}`,
        request.nextUrl.origin,
      ),
    );
  }

  // ログイン済みユーザーがルートページにアクセスした場合は/homeにリダイレクト
  if (request.nextUrl.pathname === "/" && session) {
    return NextResponse.redirect(new URL("/home", request.nextUrl.origin));
  }

  return response;
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico, sitemap.xml, robots.txt (metadata files)
     */
    "/((?!_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt|manifest.json).*)",
  ],
};
