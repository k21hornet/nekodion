import Foundation

struct SettingsAPI {
    let apiClient: APIClient

    func getForwardingAddress() async throws -> EmailForwardingAddress {
        try await apiClient.get("/api/email-forwarding/address")
    }

    func getForwardingConfirmation() async throws -> EmailForwardingConfirmation {
        try await apiClient.get("/api/email-forwarding/confirmation")
    }
}
