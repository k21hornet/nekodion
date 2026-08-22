import Foundation

struct APIError: Error, LocalizedError {
    let statusCode: Int
    let message: String

    var errorDescription: String? { message }
}

private struct ErrorResponse: Decodable {
    let status: Int
    let error: String
    let message: String
}

struct APIClient {
    private let authService: AuthenticationService

    init(authService: AuthenticationService) {
        self.authService = authService
    }

    private func makeRequest(_ path: String, method: String) async throws -> URLRequest {
        guard let url = URL(string: AppConfig.apiBaseURL + path) else {
            throw URLError(.badURL)
        }
        let token = try await authService.getAccessToken()
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        return request
    }

    private func validate(_ data: Data, _ response: URLResponse) throws {
        guard let http = response as? HTTPURLResponse else { return }
        guard !(200..<300).contains(http.statusCode) else { return }
        if let decoded = try? JSONDecoder().decode(ErrorResponse.self, from: data) {
            throw APIError(statusCode: http.statusCode, message: decoded.message)
        }
        throw APIError(statusCode: http.statusCode, message: "リクエストに失敗しました (\(http.statusCode))")
    }

    func get(_ path: String) async throws -> Data {
        let request = try await makeRequest(path, method: "GET")
        let (data, response) = try await URLSession.shared.data(for: request)
        try validate(data, response)
        return data
    }

    func get<T: Decodable>(_ path: String) async throws -> T {
        let data = try await get(path)
        return try JSONDecoder().decode(T.self, from: data)
    }

    func send<Body: Encodable>(_ path: String, method: String, body: Body) async throws -> Data {
        var request = try await makeRequest(path, method: method)
        request.httpBody = try JSONEncoder().encode(body)
        let (data, response) = try await URLSession.shared.data(for: request)
        try validate(data, response)
        return data
    }

    func send(_ path: String, method: String) async throws -> Data {
        let request = try await makeRequest(path, method: method)
        let (data, response) = try await URLSession.shared.data(for: request)
        try validate(data, response)
        return data
    }

    func post<Body: Encodable>(_ path: String, body: Body) async throws {
        _ = try await send(path, method: "POST", body: body)
    }

    func put<Body: Encodable>(_ path: String, body: Body) async throws {
        _ = try await send(path, method: "PUT", body: body)
    }

    func patch<Body: Encodable>(_ path: String, body: Body) async throws {
        _ = try await send(path, method: "PATCH", body: body)
    }

    func delete(_ path: String) async throws {
        _ = try await send(path, method: "DELETE")
    }
}
