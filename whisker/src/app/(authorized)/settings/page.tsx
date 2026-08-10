import {
  getEmailForwardingAddress,
  getEmailForwardingConfirmation,
} from "@/features/email-forwarding/api";
import type {
  EmailForwardingAddress,
  EmailForwardingConfirmation,
} from "@/features/email-forwarding/types";
import { SettingsPage } from "./_components";

export default async function Settings() {
  const addressResult = await getEmailForwardingAddress();
  const forwardingAddress =
    "body" in addressResult
      ? (addressResult.body as EmailForwardingAddress).address
      : null;

  const confirmationResult = await getEmailForwardingConfirmation();
  const confirmation =
    "body" in confirmationResult
      ? (confirmationResult.body as EmailForwardingConfirmation)
      : null;

  return (
    <SettingsPage
      forwardingAddress={forwardingAddress}
      confirmation={confirmation}
    />
  );
}
