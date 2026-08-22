import Combine
import Foundation

@MainActor
final class TransactionViewModel: ObservableObject {
    @Published var isLoading = true
    @Published var unreadTransactions: [DailyTransactionResponse] = []
    @Published var transactions: [DailyTransactionResponse] = []

    private let authService: AuthenticationService
    private let transactionAPI: TransactionAPI

    init(authService: AuthenticationService) {
        self.authService = authService
        self.transactionAPI = TransactionAPI(apiClient: APIClient(authService: authService))
    }

    func load() async {
        do {
            async let unreadResponse = transactionAPI.getUnreadTransactions()
            async let allResponse = transactionAPI.getTransactions()

            let (unread, all) = try await (unreadResponse, allResponse)
            unreadTransactions = unread
            transactions = all
        } catch {
            authService.reportError("入出金の取得に失敗しました", error)
        }
        isLoading = false
    }

    func markAllAsRead() async {
        let ids = unreadTransactions.flatMap { $0.dailyTransactions.map(\.id) }
        guard !ids.isEmpty else { return }
        do {
            try await transactionAPI.markAsRead(ids: ids)
            await load()
        } catch {
            authService.reportError("既読処理に失敗しました", error)
        }
    }
}
