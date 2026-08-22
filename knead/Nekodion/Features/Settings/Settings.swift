import SwiftUI

struct SettingsView: View {
    @ObservedObject var authService: AuthenticationService
    
    var body: some View {
        ZStack {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            Button{
                Task {
                    await authService.logout()
                }
            } label: {
                Text("ログアウト")
            }
        }
    }
}

#Preview {
    SettingsView(authService: AuthenticationService())
}
