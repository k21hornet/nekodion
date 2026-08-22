import Combine
import Foundation

@MainActor
final class HomeViewModel: ObservableObject {
    @Published var isTotalAssetsHidden = false
    @Published var isLoading = true
    @Published var totalAssets: Decimal = 0
    @Published var monthlySummary: MonthlySummaryResponse?
    @Published var categorySummary: [MonthlyCategoryTypeSummaryItem] = []
    @Published var dailyTransactions: [DailyTransactionResponse] = []
    @Published var unreadTransactions: [DailyTransactionResponse] = []

    private let authService: AuthenticationService
    private let homeAPI: HomeAPI

    init(authService: AuthenticationService) {
        self.authService = authService
        self.homeAPI = HomeAPI(apiClient: APIClient(authService: authService))
    }

    var expenseItems: [ExpenseItem] {
        categorySummary
            .filter { !$0.isIncome }
            .map { item in
                ExpenseItem(
                    name: item.categoryTypeName,
                    amount: item.totalAmount,
                    color: CategoryStyle.color(for: item.categoryTypeName)
                )
            }
    }

    var recentTransactionGroups: [DailyTransactionResponse] {
        Array(dailyTransactions.prefix(3))
    }

    func load() async {
        let now = Date()
        let year = Calendar.current.component(.year, from: now)
        let month = Calendar.current.component(.month, from: now)

        do {
            async let totalAssetsResponse = homeAPI.getTotalAssets()
            async let monthlySummaryResponse = homeAPI.getMonthlySummary(year: year, month: month)
            async let categorySummaryResponse = homeAPI.getMonthlyCategorySummary(year: year, month: month)
            async let transactionsResponse = homeAPI.getTransactions()
            async let unreadResponse = homeAPI.getUnreadTransactions()

            let (assets, summary, categories, transactions, unread) = try await (
                totalAssetsResponse, monthlySummaryResponse, categorySummaryResponse, transactionsResponse, unreadResponse
            )
            totalAssets = assets.totalAssets
            monthlySummary = summary
            categorySummary = categories
            dailyTransactions = transactions
            unreadTransactions = unread
        } catch {
            authService.reportError("データの取得に失敗しました", error)
        }
        isLoading = false
    }
}
