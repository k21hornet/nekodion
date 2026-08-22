import Combine
import Foundation

@MainActor
final class AccountViewModel: ObservableObject {
    @Published var isLoading = true
    @Published var accountGroups: [AccountSummaryResponse] = []

    private let authService: AuthenticationService
    private let accountAPI: AccountAPI

    init(authService: AuthenticationService) {
        self.authService = authService
        self.accountAPI = AccountAPI(apiClient: APIClient(authService: authService))
    }

    func load() async {
        do {
            accountGroups = try await accountAPI.getAccounts()
        } catch {
            authService.reportError("口座の取得に失敗しました", error)
        }
        isLoading = false
    }
}
