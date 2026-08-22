import Foundation

struct TotalAssetsResponse: Decodable {
    let totalAssets: Decimal
}

struct MonthlySummaryResponse: Decodable {
    let year: Int
    let month: Int
    let totalIncome: Decimal
    let totalExpense: Decimal
}

struct MonthlyCategoryTypeSummaryItem: Decodable {
    let categoryTypeName: String
    let isIncome: Bool
    let totalAmount: Decimal
}

struct DailyTransactionResponse: Decodable {
    let transactionDateTime: String
    let dailyTransactions: [TransactionItem]
}

struct TransactionItem: Decodable, Identifiable {
    let id: Int
    let amount: Decimal
    let transactionType: String
    let direction: String
    let transactionName: String?
    let transactionDescription: String?
    let categoryName: String
    let categoryTypeName: String
}
