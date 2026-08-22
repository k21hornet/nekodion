import Foundation

struct AccountAPI {
    let apiClient: APIClient

    func getAccounts() async throws -> [AccountSummaryResponse] {
        try await apiClient.get("/api/accounts")
    }
}
