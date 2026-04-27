export type DailyTransactionResponse = {
  transactionDateTime: string;
  dailyTransactions: TransactionItem[];
};

export type TransactionItem = {
  id: number;
  amount: number;
  transactionType: string;
  transactionName: string;
  transactionDescription: string;
  categoryName: string;
  categoryTypeName: string;
};

export type TotalAssetsResponse = {
  totalAssets: number;
};

export type MonthlySummaryResponse = {
  year: number;
  month: number;
  totalIncome: number;
  totalExpense: number;
};

export type MonthlyCategoryTypeSummaryItem = {
  categoryTypeName: string;
  isIncome: boolean;
  totalAmount: number;
};

export type TransactionDetailResponse = {
  id: number;
  accountId: number;
  categoryId: number;
  transactionType: string;
  transactionName: string | null;
  amount: number;
  transactionDateTime: string;
  description: string | null;
  isAggregated: boolean;
};
