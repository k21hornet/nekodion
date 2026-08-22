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

    func markAsRead(ids: [Int]) async throws {
        try await apiClient.patch("/api/transactions/read-all", body: MarkAsReadRequest(transactionIds: ids))
    }

    func getTransaction(id: Int) async throws -> TransactionDetailResponse {
        try await apiClient.get("/api/transactions/\(id)")
    }

    func createTransaction(_ request: TransactionRequest) async throws {
        try await apiClient.post("/api/transactions", body: request)
    }

    func updateTransaction(id: Int, _ request: TransactionRequest) async throws {
        try await apiClient.put("/api/transactions/\(id)", body: request)
    }

    func deleteTransaction(id: Int) async throws {
        try await apiClient.delete("/api/transactions/\(id)")
    }
}
