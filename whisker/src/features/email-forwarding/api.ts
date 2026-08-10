import { fetcher } from "@/util/fetcher";

export async function getEmailForwardingAddress() {
  return await fetcher.get("/email-forwarding/address");
}

export async function getEmailForwardingConfirmation() {
  return await fetcher.get("/email-forwarding/confirmation");
}
