import SwiftUI
import Charts

struct MonthlyView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: MonthlyViewModel

    init(authService: AuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: MonthlyViewModel(authService: authService))
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            ScrollView {
                HStack {
                    Button {
                        viewModel.changeMonth(by: -1)
                    }
                    label: {
                        Image(systemName: "chevron.left")
                    }
                    .frame(maxWidth: .infinity)
                    .accessibilityLabel("前の月")

                    Text("\(viewModel.year)年\(viewModel.month)月")
                        .frame(maxWidth: .infinity)

                    Button {
                        viewModel.changeMonth(by: 1)
                    }
                    label: {
                        Image(systemName: "chevron.right")
                    }
                    .frame(maxWidth: .infinity)
                    .disabled(viewModel.isCurrentMonth)
                    .accessibilityLabel("次の月")
                }
                .font(.title3.weight(.semibold))
                .padding(.horizontal)
                .padding(.top, 12)

                Picker("入出金の種類", selection: $viewModel.selectedTransactionType) {
                    Text("支出")
                        .tag(MonthlyTransactionType.expense)
                    Text("収入")
                        .tag(MonthlyTransactionType.income)
                }
                .pickerStyle(.segmented)
                .padding(.horizontal)

                if viewModel.isLoading {
                    ProgressView()
                        .padding(.top, 40)
                } else {
                    monthlySummaryRow
                        .padding(.horizontal)
                        .padding(.top, 12)

                    if viewModel.categoryItems.isEmpty {
                        Text("この月のデータはありません")
                            .font(.callout)
                            .foregroundStyle(Color.gray)
                            .padding(.top, 40)
                    } else {
                        categoryChart
                        categoryBreakdownCard
                    }
                }
            }

            Button {
            } label: {
                Image(systemName: "plus")
                    .font(.title2.weight(.bold))
                    .foregroundStyle(.white)
                    .frame(width: 56, height: 56)
                    .background(Color.blue)
                    .clipShape(Circle())
                    .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
            }
            .padding(.trailing, 20)
            .padding(.bottom, 24)
            .accessibilityLabel("追加")
        }
        .task(id: "\(viewModel.year)-\(viewModel.month)") {
            await viewModel.load()
        }
    }

    private var monthlySummaryRow: some View {
        let totalIncome = viewModel.summary?.totalIncome ?? 0
        let totalExpense = viewModel.summary?.totalExpense ?? 0
        let balance = totalIncome - totalExpense

        return HStack {
            VStack(spacing: 4) {
                Text("収入")
                    .font(.caption)
                    .foregroundStyle(Color.gray)
                Text(formatYen(totalIncome))
                    .font(.subheadline)
                    .fontWeight(.bold)
                    .foregroundStyle(Color.blue)
            }
            .frame(maxWidth: .infinity)
            VStack(spacing: 4) {
                Text("支出")
                    .font(.caption)
                    .foregroundStyle(Color.gray)
                Text(formatYen(totalExpense))
                    .font(.subheadline)
                    .fontWeight(.bold)
                    .foregroundStyle(Color.red)
            }
            .frame(maxWidth: .infinity)
            VStack(spacing: 4) {
                Text("収支")
                    .font(.caption)
                    .foregroundStyle(Color.gray)
                Text(formatBalance(balance))
                    .font(.subheadline)
                    .fontWeight(.bold)
                    .foregroundStyle(balance >= 0 ? Color.blue : Color.red)
            }
            .frame(maxWidth: .infinity)
        }
        .padding()
        .background(.regularMaterial)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
        .padding(.horizontal)
    }

    private var categoryChart: some View {
        ZStack {
            Chart {
                ForEach(viewModel.categoryItems, id: \.categoryTypeName) { item in
                    SectorMark(
                        angle: .value("金額", item.totalAmount),
                        innerRadius: .ratio(0.58),
                        angularInset: 2
                    )
                    .foregroundStyle(CategoryStyle.color(for: item.categoryTypeName))
                }
            }
            VStack(spacing: 2) {
                Text(viewModel.selectedTransactionType == .expense ? "支出合計" : "収入合計")
                    .font(.caption2)
                    .foregroundStyle(.secondary)
                Text(formatYen(viewModel.categoryTotal))
                    .font(.caption)
                    .fontWeight(.bold)
            }
        }
        .frame(width: 180, height: 180)
        .padding(.top, 12)
    }

    private var categoryBreakdownCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                Text("内訳")
                    .font(.headline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundStyle(Color.gray)
                    .padding()
                ForEach(viewModel.categoryItems, id: \.categoryTypeName) { item in
                    HStack {
                        Image(systemName: CategoryStyle.iconName(for: item.categoryTypeName))
                            .foregroundStyle(CategoryStyle.color(for: item.categoryTypeName))
                        Text(item.categoryTypeName)
                        Spacer()
                        Text(formatYen(item.totalAmount))
                            .fontWeight(.semibold)
                            .foregroundStyle(viewModel.selectedTransactionType == .expense ? Color.red : Color.blue)
                    }
                    .padding()
                    Divider()
                }
            }
        }
        .padding()
    }
}

#Preview {
    MonthlyView(authService: AuthenticationService())
}
