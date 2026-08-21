import SwiftUI

struct RootView: View {
    @ObservedObject var authService: AuthenticationService
    
    var body: some View {
        if authService.isLoading {
            ProgressView()
        } else if authService.isAuthenticated {
            TabView {
                HomeView(authService: authService)
                    .tabItem {
                        Label("ホーム", systemImage: "house")
                    }
                TransactionView()
                    .tabItem {
                        Label("入出金", systemImage: "arrow.left.arrow.right")
                    }
                MonthlyView()
                    .tabItem {
                        Label("月次", systemImage: "calendar")
                    }
                AccountView()
                    .tabItem {
                        Label("口座", systemImage: "person")
                    }
                SettingsView(authService: authService)
                    .tabItem {
                        Label("設定", systemImage: "gear")
                    }
            }
        } else {
            VStack {
                Button{
                    Task {
                        await authService.login()
                    }
                } label: {
                    Text("こねこねする")
                }
            }
        }
    }
}

#Preview {
    RootView(authService: AuthenticationService())
}
