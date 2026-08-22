import SwiftUI

struct AccountView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: AccountViewModel

    private static let accountTypeLabels: [String: String] = [
        "BANK": "銀行口座",
        "CREDIT": "クレカ口座",
        "MANUAL": "手動管理口座"
    ]

    init(authService: AuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: AccountViewModel(authService: authService))
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            if viewModel.isLoading {
                ProgressView()
            } else {
                ScrollView {
                    Text("口座")
                        .font(.title3.weight(.semibold))
                        .padding(.top, 12)

                    if viewModel.accountGroups.isEmpty {
                        Text("口座がありません")
                            .font(.callout)
                            .foregroundStyle(Color.gray)
                            .padding()
                    } else {
                        ForEach(viewModel.accountGroups, id: \.accountType) { group in
                            accountGroupCard(group)
                        }
                    }
                }
            }

            Button {
            } label: {
                Text("＋口座を追加する")
                    .foregroundStyle(.white)
                    .frame(width: 200, height: 50)
                    .background(Color.blue)
                    .clipShape(RoundedRectangle(cornerRadius: 28))
                    .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
            }
            .padding(.bottom, 24)
            .accessibilityLabel("追加")
        }
        .task {
            await viewModel.load()
        }
    }

    private func accountGroupCard(_ group: AccountSummaryResponse) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack {
                Text(Self.accountTypeLabels[group.accountType] ?? group.accountType)
                    .font(.headline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                ForEach(Array(group.accounts.enumerated()), id: \.element.id) { index, account in
                    HStack {
                        Text(account.accountName)
                        Spacer()
                        Text(formatYen(group.accountType == "CREDIT" ? abs(account.totalAmount) : account.totalAmount))
                            .fontWeight(.semibold)
                    }
                    .padding()
                    if index < group.accounts.count - 1 {
                        Divider()
                    }
                }
            }
        }
        .padding(.horizontal)
    }
}

#Preview {
    AccountView(authService: AuthenticationService())
}
