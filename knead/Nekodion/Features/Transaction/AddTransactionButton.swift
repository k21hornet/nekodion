import SwiftUI

struct AddTransactionButton: View {
    var body: some View {
        NavigationLink(value: TransactionRoute.create) {
            Image(systemName: "plus")
                .font(.title2.weight(.semibold))
                .foregroundStyle(.white)
                .frame(width: 56, height: 56)
                .background(Color.blue)
                .clipShape(Circle())
                .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
        }
        .padding(.trailing, 20)
        .padding(.bottom, 24)
        .accessibilityLabel("入出金を追加")
    }
}
