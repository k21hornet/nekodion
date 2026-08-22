import Combine
import Foundation

@MainActor
final class SettingsViewModel: ObservableObject {
    @Published var isLoading = true
    @Published var forwardingAddress: String?
    @Published var confirmation: EmailForwardingConfirmation?

    private let authService: AuthenticationService
    private let settingsAPI: SettingsAPI

    init(authService: AuthenticationService) {
        self.authService = authService
        self.settingsAPI = SettingsAPI(apiClient: APIClient(authService: authService))
    }

    var confirmationLink: URL? {
        guard let confirmation else { return nil }
        return Self.extractLink(from: confirmation.bodyText)
    }

    func load() async {
        let addressResponse = try? await settingsAPI.getForwardingAddress()
        forwardingAddress = addressResponse?.address

        confirmation = try? await settingsAPI.getForwardingConfirmation()

        isLoading = false
    }

    private static func extractLink(from text: String) -> URL? {
        guard let detector = try? NSDataDetector(types: NSTextCheckingResult.CheckingType.link.rawValue) else {
            return nil
        }
        let range = NSRange(text.startIndex..., in: text)
        return detector.firstMatch(in: text, range: range)?.url
    }
}
