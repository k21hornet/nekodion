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

struct TransactionDetailResponse: Decodable {
    let id: Int
    let accountId: Int?
    let categoryId: Int
    let transactionType: String
    let direction: String
    let transactionName: String
    let amount: Decimal
    let transactionDateTime: String
    let description: String?
    let isAggregated: Bool
    let isDeletable: Bool
}

struct MarkAsReadRequest: Encodable {
    let transactionIds: [Int]
}

struct TransactionRequest: Encodable {
    let accountId: Int?
    let categoryId: Int
    let transactionType: String
    let direction: String
    let transactionName: String
    let amount: Decimal
    let transactionDateTime: String
    let description: String?
    let isAggregated: Bool?
}
