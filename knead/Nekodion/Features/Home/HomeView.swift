import SwiftUI
import Charts

struct HomeView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: HomeViewModel

    init(authService: AuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: HomeViewModel(authService: authService))
    }

    var body: some View {
        ZStack {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            if viewModel.isLoading {
                ProgressView()
            } else {
                ScrollView {
                    totalAssetsCard
                    monthlySummaryCard
                    unreadTransactionsCard
                    recentTransactionsCard
                    Spacer()
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }

    private var totalAssetsCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .frame(height: 100)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            HStack {
                VStack(alignment: .leading) {
                    Text("総資産")
                        .font(.headline)
                        .fontWeight(.semibold)
                    HStack(spacing: 4) {
                        Text(viewModel.isTotalAssetsHidden ? "¥ ---,---" : formatYen(viewModel.totalAssets))
                            .font(.largeTitle)
                            .fontWeight(.semibold)
                        Button {
                            viewModel.isTotalAssetsHidden.toggle()
                        } label: {
                            Image(systemName: viewModel.isTotalAssetsHidden ? "eye.slash" : "eye")
                        }
                        .foregroundStyle(.secondary)
                        .accessibilityLabel("総資産の表示切替")
                    }
                }
                Spacer()
                Image(.nekoIcon).resizable().frame(width: 100, height: 100)
            }
            .padding()
        }
        .padding(.horizontal)
    }

    private var monthlySummaryCard: some View {
        let totalIncome = viewModel.monthlySummary?.totalIncome ?? 0
        let totalExpense = viewModel.monthlySummary?.totalExpense ?? 0
        let month = viewModel.monthlySummary?.month ?? Calendar.current.component(.month, from: Date())
        let isHidden = viewModel.isTotalAssetsHidden

        return ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .frame(height: 220)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                Text("\(month)月収支")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundStyle(Color.gray)
                    .padding(.horizontal)
                HStack {
                    ZStack {
                        Chart {
                            ForEach(viewModel.expenseItems) { item in
                                SectorMark(
                                    angle: .value("金額", item.amount),
                                    innerRadius: .ratio(0.58),
                                    angularInset: 2
                                )
                                .foregroundStyle(item.color)
                            }
                        }
                        VStack(spacing: 2) {
                            Text("支出合計")
                                .font(.caption2)
                                .foregroundStyle(.secondary)
                            Text(isHidden ? "¥ ---,---" : formatYen(totalExpense))
                                .font(.caption)
                                .fontWeight(.bold)
                        }
                    }
                    .frame(width: 140, height: 140)

                    VStack {
                        HStack {
                            Text("収入")
                                .foregroundStyle(Color.gray)
                            Spacer()
                            Text(isHidden ? "¥ ---,---" : formatYen(totalIncome))
                                .font(.title2)
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.blue)
                        }
                        HStack {
                            Text("支出")
                                .foregroundStyle(Color.gray)
                            Spacer()
                            Text(isHidden ? "¥ ---,---" : formatYen(totalExpense))
                                .font(.title2)
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        HStack {
                            Text("収支")
                                .foregroundStyle(Color.gray)
                            Spacer()
                            Text(isHidden ? "¥ ---,---" : formatBalance(totalIncome - totalExpense))
                                .font(.title2)
                                .fontWeight(.semibold)
                                .foregroundStyle(totalIncome - totalExpense >= 0 ? Color.blue : Color.red)
                        }
                    }
                }
                .padding(.horizontal)
            }
        }
        .padding(.horizontal)
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

    private var recentTransactionsCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                Text("最近の入出金")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundStyle(Color.gray)
                    .padding()

                if viewModel.recentTransactionGroups.isEmpty {
                    Text("まだ入出金がありません")
                        .font(.callout)
                        .foregroundStyle(Color.gray)
                        .padding()
                } else {
                    TransactionGroupList(groups: viewModel.recentTransactionGroups)
                }
            }
        }
        .padding(.horizontal)
    }
}

#Preview {
    HomeView(authService: AuthenticationService())
}
