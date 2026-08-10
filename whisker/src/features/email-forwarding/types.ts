export type EmailForwardingAddress = {
  address: string;
};

export type EmailForwardingConfirmation = {
  fromAddress: string;
  subject: string;
  bodyText: string;
  receivedAt: string;
};
