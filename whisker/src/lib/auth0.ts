import { Auth0Client } from "@auth0/nextjs-auth0/server";
import { NextResponse } from "next/server";
import { signupUser } from "@/features/user/api";

export const auth0 = new Auth0Client({
  domain: process.env.AUTH0_DOMAIN,
  clientId: process.env.AUTH0_CLIENT_ID,
  clientSecret: process.env.AUTH0_CLIENT_SECRET,
  secret: process.env.AUTH0_SECRET,
  appBaseUrl: process.env.NEXT_PUBLIC_APP_BASE_URL,
  authorizationParameters: {
    audience: process.env.AUTH0_AUDIENCE,
  },

  async onCallback(error, _, session) {
    if (error) {
      return Promise.resolve(
        NextResponse.redirect(
          new URL("/error/access-denied", process.env.NEXT_PUBLIC_APP_BASE_URL),
        ),
      );
    }

    if (session && session.tokenSet.accessToken) {
      const accessToken = session.tokenSet.accessToken;
      await signupUser(accessToken);
    }

    return Promise.resolve(
      NextResponse.redirect(
        new URL("/home", process.env.NEXT_PUBLIC_APP_BASE_URL),
      ),
    );
  },
});
