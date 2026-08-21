import Foundation

struct APIClient {
    private let authService: AuthenticationService

    init(authService: AuthenticationService) {
        self.authService = authService
    }

    func get(_ path: String) async throws -> Data {
        guard let url = URL(string: AppConfig.apiBaseURL + path) else {
            throw URLError(.badURL)
        }
        let token = try await authService.getAccessToken()
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        let (data, _) = try await URLSession.shared.data(for: request)
        return data
    }
}
