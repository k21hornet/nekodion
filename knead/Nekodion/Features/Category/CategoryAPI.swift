import Foundation

struct CategoryAPI {
    let apiClient: APIClient

    func getCategories() async throws -> [CategoryTypeResponse] {
        try await apiClient.get("/api/categories")
    }
}
