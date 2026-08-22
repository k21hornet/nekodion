import Foundation

struct TransactionAPI {
    let apiClient: APIClient

    func getTotalAssets() async throws -> TotalAssetsResponse {
        try await apiClient.get("/api/transactions/total-assets")
    }

    func getMonthlySummary(year: Int, month: Int) async throws -> MonthlySummaryResponse {
        try await apiClient.get("/api/transactions/monthly-summary?year=\(year)&month=\(month)")
    }

    func getMonthlyCategorySummary(year: Int, month: Int) async throws -> [MonthlyCategoryTypeSummaryItem] {
        try await apiClient.get("/api/transactions/monthly-category-summary?year=\(year)&month=\(month)")
    }

    func getTransactions() async throws -> [DailyTransactionResponse] {
        try await apiClient.get("/api/transactions")
    }

    func getUnreadTransactions() async throws -> [DailyTransactionResponse] {
        try await apiClient.get("/api/transactions/unread")
    }
}
