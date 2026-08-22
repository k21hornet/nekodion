import Foundation

struct HomeAPI {
    let apiClient: APIClient

    private var transactionAPI: TransactionAPI { TransactionAPI(apiClient: apiClient) }

    func getTotalAssets() async throws -> TotalAssetsResponse {
        try await transactionAPI.getTotalAssets()
    }

    func getMonthlySummary(year: Int, month: Int) async throws -> MonthlySummaryResponse {
        try await transactionAPI.getMonthlySummary(year: year, month: month)
    }

    func getMonthlyCategorySummary(year: Int, month: Int) async throws -> [MonthlyCategoryTypeSummaryItem] {
        try await transactionAPI.getMonthlyCategorySummary(year: year, month: month)
    }

    func getTransactions() async throws -> [DailyTransactionResponse] {
        try await transactionAPI.getTransactions()
    }

    func getUnreadTransactions() async throws -> [DailyTransactionResponse] {
        try await transactionAPI.getUnreadTransactions()
    }
}
