import SwiftUI

struct TransactionListView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: TransactionViewModel

    init(authService: AuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: TransactionViewModel(authService: authService))
    }

    var body: some View {
        NavigationStack {
            ZStack(alignment: .bottomTrailing) {
                Color.blue.opacity(0.06)
                    .ignoresSafeArea()
                if viewModel.isLoading {
                    ProgressView()
                } else {
                    ScrollView {
                        Text("入出金")
                            .font(.title3.weight(.semibold))
                            .padding(.top, 12)
                        unreadTransactionsCard
                        allTransactionsCard
                    }
                }

                AddTransactionButton()
            }
            .navigationDestination(for: TransactionRoute.self) { route in
                TransactionFormView(mode: route, authService: authService) {
                    Task { await viewModel.load() }
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }

    private var unreadTransactionsCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                HStack {
                    Text("未読の入出金")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .foregroundStyle(Color.gray)
                        .padding()
                    Button("既読をつける") {
                        Task { await viewModel.markAllAsRead() }
                    }
                    .disabled(viewModel.unreadTransactions.isEmpty)
                    .padding()
                }
                if viewModel.unreadTransactions.isEmpty {
                    Text("未読の入出金はありません")
                        .font(.callout)
                        .foregroundStyle(Color.gray)
                        .padding()
                } else {
                    TransactionGroupList(groups: viewModel.unreadTransactions)
                }
            }
        }
        .padding(.horizontal)
    }

    private var allTransactionsCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                Text("入出金一覧")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundStyle(Color.gray)
                    .padding()
                if viewModel.transactions.isEmpty {
                    Text("まだ入出金がありません")
                        .font(.callout)
                        .foregroundStyle(Color.gray)
                        .padding()
                } else {
                    TransactionGroupList(groups: viewModel.transactions)
                }
            }
        }
        .padding(.horizontal)
    }
}

#Preview {
    TransactionListView(authService: AuthenticationService())
}
