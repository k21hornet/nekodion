import SwiftUI

struct TransactionGroupList: View {
    let groups: [DailyTransactionResponse]

    var body: some View {
        ForEach(groups, id: \.transactionDateTime) { group in
            Text(formatDate(group.transactionDateTime))
                .font(.callout)
                .frame(maxWidth: .infinity, alignment: .leading)
                .foregroundStyle(Color.gray)
                .padding(.horizontal)
            Divider()
            ForEach(group.dailyTransactions) { transaction in
                NavigationLink(value: TransactionRoute.edit(id: transaction.id)) {
                    HStack {
                        Image(systemName: CategoryStyle.iconName(for: transaction.categoryTypeName))
                            .foregroundStyle(CategoryStyle.color(for: transaction.categoryTypeName))
                        Text(transaction.transactionName ?? "（名称なし）")
                        Spacer()
                        Text(formatSignedYen(transaction.amount, direction: transaction.direction))
                            .fontWeight(.semibold)
                            .foregroundStyle(transaction.direction == "OUT" ? Color.red : Color.blue)
                    }
                    .padding()
                    .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
            }
        }
    }
}
