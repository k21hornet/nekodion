import SwiftUI

struct SettingsView: View {
    @ObservedObject var authService: AuthenticationService
    
    var body: some View {
        VStack {
            Text("設定")
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
