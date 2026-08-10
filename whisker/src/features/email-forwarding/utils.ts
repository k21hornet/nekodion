const URL_PATTERN = /https?:\/\/\S+/;

export function extractConfirmationLink(bodyText: string): string | null {
  const match = bodyText.match(URL_PATTERN);
  return match ? match[0] : null;
}
