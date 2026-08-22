import Combine
import Foundation

enum MonthlyTransactionType {
    case income
    case expense
}

@MainActor
final class MonthlyViewModel: ObservableObject {
    @Published var selectedTransactionType: MonthlyTransactionType = .expense
    @Published var isLoading = true
    @Published var year: Int
    @Published var month: Int
    @Published var summary: MonthlySummaryResponse?
    @Published var categorySummaries: [MonthlyCategoryTypeSummaryItem] = []

    private let authService: AuthenticationService
    private let transactionAPI: TransactionAPI

    init(authService: AuthenticationService) {
        self.authService = authService
        self.transactionAPI = TransactionAPI(apiClient: APIClient(authService: authService))
        let now = Date()
        self.year = Calendar.current.component(.year, from: now)
        self.month = Calendar.current.component(.month, from: now)
    }

    var categoryItems: [MonthlyCategoryTypeSummaryItem] {
        categorySummaries
            .filter { $0.isIncome == (selectedTransactionType == .income) }
            .sorted { $0.totalAmount > $1.totalAmount }
    }

    var categoryTotal: Decimal {
        categoryItems.reduce(0) { $0 + $1.totalAmount }
    }

    var isCurrentMonth: Bool {
        let now = Date()
        return year == Calendar.current.component(.year, from: now)
            && month == Calendar.current.component(.month, from: now)
    }

    func changeMonth(by offset: Int) {
        var components = DateComponents()
        components.year = year
        components.month = month + offset
        components.day = 1
        guard let date = Calendar.current.date(from: components) else { return }
        year = Calendar.current.component(.year, from: date)
        month = Calendar.current.component(.month, from: date)
    }

    func load() async {
        isLoading = true
        do {
            async let summaryResponse = transactionAPI.getMonthlySummary(year: year, month: month)
            async let categoryResponse = transactionAPI.getMonthlyCategorySummary(year: year, month: month)

            let (summaryResult, categoryResult) = try await (summaryResponse, categoryResponse)
            summary = summaryResult
            categorySummaries = categoryResult
        } catch {
            authService.reportError("月次データの取得に失敗しました", error)
        }
        isLoading = false
    }
}
