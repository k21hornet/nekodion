import Combine
import Foundation

@MainActor
final class TransactionFormViewModel: ObservableObject {
    let mode: TransactionRoute

    @Published var direction: String = "OUT"
    @Published var selectedCategoryId: Int?
    @Published var selectedAccountId: Int?
    @Published var amountText: String = "0"
    @Published var transactionName: String = ""
    @Published var selectedDate: Date = Date()
    @Published var description: String = ""
    @Published var isAggregated: Bool = true

    @Published var categoryGroups: [CategoryTypeResponse] = []
    @Published var accountGroups: [AccountSummaryResponse] = []

    @Published var isLoading = true
    @Published var isSaving = false
    @Published var isDeletable = true
    @Published var validationMessage: String?

    private let authService: AuthenticationService
    private let transactionAPI: TransactionAPI
    private let categoryAPI: CategoryAPI
    private let accountAPI: AccountAPI

    init(mode: TransactionRoute, authService: AuthenticationService) {
        self.mode = mode
        self.authService = authService
        let apiClient = APIClient(authService: authService)
        self.transactionAPI = TransactionAPI(apiClient: apiClient)
        self.categoryAPI = CategoryAPI(apiClient: apiClient)
        self.accountAPI = AccountAPI(apiClient: apiClient)
    }

    var isEditMode: Bool {
        if case .edit = mode { return true }
        return false
    }

    var filteredCategories: [CategoryTypeResponse] {
        let wantIncome = direction == "IN"
        return categoryGroups.filter { $0.isIncome == wantIncome }
    }

    var flattenedAccounts: [AccountItem] {
        accountGroups.flatMap { $0.accounts }
    }

    func loadInitialData() async {
        isLoading = true
        do {
            async let categoriesResponse = categoryAPI.getCategories()
            async let accountsResponse = accountAPI.getAccounts()

            if case .edit(let id) = mode {
                async let detailResponse = transactionAPI.getTransaction(id: id)
                let (categories, accounts, detail) = try await (categoriesResponse, accountsResponse, detailResponse)
                categoryGroups = categories
                accountGroups = accounts
                applyDetail(detail)
            } else {
                let (categories, accounts) = try await (categoriesResponse, accountsResponse)
                categoryGroups = categories
                accountGroups = accounts
            }
        } catch {
            authService.reportError("入出金フォームの読み込みに失敗しました", error)
        }
        isLoading = false
    }

    private func applyDetail(_ detail: TransactionDetailResponse) {
        direction = detail.direction
        selectedCategoryId = detail.categoryId
        selectedAccountId = detail.accountId
        amountText = NSDecimalNumber(decimal: detail.amount).stringValue
        transactionName = detail.transactionName
        selectedDate = parseTransactionDateTime(detail.transactionDateTime) ?? Date()
        description = detail.description ?? ""
        isAggregated = detail.isAggregated
        isDeletable = detail.isDeletable
    }

    func directionChanged() {
        let availableIds = filteredCategories.flatMap { $0.categories.map(\.categoryId) }
        if let selectedCategoryId, !availableIds.contains(selectedCategoryId) {
            self.selectedCategoryId = nil
        }
    }

    func validate() -> Bool {
        guard selectedCategoryId != nil else {
            validationMessage = "カテゴリを選択してください"
            return false
        }
        guard !transactionName.trimmingCharacters(in: .whitespaces).isEmpty else {
            validationMessage = "取引名を入力してください"
            return false
        }
        guard let amount = Decimal(string: amountText), amount > 0 else {
            validationMessage = "金額を正しく入力してください"
            return false
        }
        validationMessage = nil
        return true
    }

    func save() async -> Bool {
        guard validate(), let categoryId = selectedCategoryId, let amount = Decimal(string: amountText) else {
            return false
        }
        isSaving = true
        defer { isSaving = false }

        let request = TransactionRequest(
            accountId: selectedAccountId,
            categoryId: categoryId,
            transactionType: "NORMAL",
            direction: direction,
            transactionName: transactionName,
            amount: amount,
            transactionDateTime: formatTransactionDateTime(selectedDate),
            description: description.isEmpty ? nil : description,
            isAggregated: isAggregated
        )

        do {
            switch mode {
            case .create:
                try await transactionAPI.createTransaction(request)
            case .edit(let id):
                try await transactionAPI.updateTransaction(id: id, request)
            }
            return true
        } catch {
            authService.reportError("入出金の保存に失敗しました", error)
            return false
        }
    }

    func delete() async -> Bool {
        guard case .edit(let id) = mode else { return false }
        isSaving = true
        defer { isSaving = false }
        do {
            try await transactionAPI.deleteTransaction(id: id)
            return true
        } catch {
            authService.reportError("入出金の削除に失敗しました", error)
            return false
        }
    }
}
