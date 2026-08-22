import SwiftUI
import UIKit

struct SettingsView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: SettingsViewModel
    @State private var didCopyAddress = false

    init(authService: AuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: SettingsViewModel(authService: authService))
    }

    var body: some View {
        ZStack {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            if viewModel.isLoading {
                ProgressView()
            } else {
                ScrollView {
                    Text("設定")
                        .font(.title3.weight(.semibold))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal)
                        .padding(.top, 12)

                    if let forwardingAddress = viewModel.forwardingAddress {
                        forwardingAddressCard(forwardingAddress)
                    }

                    if let confirmation = viewModel.confirmation {
                        confirmationCard(confirmation)
                    }

                    Button {
                        Task {
                            await authService.logout()
                        }
                    } label: {
                        Text("ログアウト")
                    }
                    .padding()
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }

    private func forwardingAddressCard(_ address: String) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack(alignment: .leading, spacing: 8) {
                Text("カード明細の転送先アドレス")
                    .font(.headline)
                    .fontWeight(.semibold)
                Text("カード会社からの利用明細メールをこのアドレスに転送するよう、お使いのメールクライアントで設定してください")
                    .font(.caption)
                    .foregroundStyle(Color.gray)
                HStack {
                    Text(address)
                        .font(.system(.footnote, design: .monospaced))
                        .lineLimit(1)
                        .truncationMode(.middle)
                    Spacer()
                    Button {
                        copyToClipboard(address)
                    } label: {
                        Image(systemName: didCopyAddress ? "checkmark" : "doc.on.doc")
                    }
                    .accessibilityLabel("コピー")
                }
            }
            .padding()
        }
        .padding(.horizontal)
    }

    private func confirmationCard(_ confirmation: EmailForwardingConfirmation) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack(alignment: .leading, spacing: 8) {
                Text("転送設定の確認")
                    .font(.headline)
                    .fontWeight(.semibold)
                Text("\(confirmation.fromAddress) から届いた転送設定の確認メールです。以下のボタンをタップして確認を完了してください")
                    .font(.caption)
                    .foregroundStyle(Color.gray)
                if let link = viewModel.confirmationLink {
                    Link("確認する", destination: link)
                        .font(.subheadline.weight(.semibold))
                } else {
                    Text(confirmation.bodyText)
                        .font(.caption)
                        .lineLimit(3)
                }
            }
            .padding()
        }
        .padding(.horizontal)
        .padding(.top, 8)
    }

    private func copyToClipboard(_ value: String) {
        UIPasteboard.general.string = value
        didCopyAddress = true
        Task {
            try? await Task.sleep(nanoseconds: 2_000_000_000)
            didCopyAddress = false
        }
    }
}

#Preview {
    SettingsView(authService: AuthenticationService())
}
