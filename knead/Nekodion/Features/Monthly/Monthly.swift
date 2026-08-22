import SwiftUI
import Charts

struct MonthlyView: View {
    private enum TransactionType {
        case income
        case expense
    }
    
    @State private var selectedTransactionType: TransactionType = .income
    
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            ScrollView {
                HStack {
                    Button {
                    }
                    label: {
                        Image(systemName: "chevron.left")
                    }
                    .frame(maxWidth: .infinity)
                    .accessibilityLabel("前の月")
                    
                    Text("2026年8月")
                        .frame(maxWidth: .infinity)
                    
                    Button {
                    }
                    label: {
                        Image(systemName: "chevron.right")
                    }
                    .frame(maxWidth: .infinity)
                    .accessibilityLabel("次の月")
                }
                .font(.title3.weight(.semibold))
                .padding(.horizontal)
                .padding(.top, 12)
                
                Picker("入出金の種類", selection: $selectedTransactionType) {
                    Text("支出")
                        .tag(TransactionType.expense)
                    Text("収入")
                        .tag(TransactionType.income)
                }
                .pickerStyle(.segmented)
                .padding(.horizontal)
                
                ZStack {
                    Chart {
                        ForEach(expenseItems) { item in
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
                        Text("¥100,000")
                            .font(.caption)
                            .fontWeight(.bold)
                    }
                }
                .frame(width: 180, height: 180)
                .padding(.top, 12)
                
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
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("食費")
                            Spacer()
                            Text("¥40,000")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        Divider()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("食費")
                            Spacer()
                            Text("¥40,000")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        Divider()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("食費")
                            Spacer()
                            Text("¥40,000")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        Divider()
                    }
                }
                .padding()
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
    }
    
    private var expenseItems: [ExpenseItem] {
        [
            ExpenseItem(name: "食費", amount: 35_000, color: .orange),
            ExpenseItem(name: "住居費", amount: 40_000, color: .blue),
            ExpenseItem(name: "その他", amount: 25_000, color: .green)
        ]
    }
}

private struct ExpenseItem: Identifiable {
    let id = UUID()
    let name: String
    let amount: Double
    let color: Color
}

#Preview {
    MonthlyView()
}
