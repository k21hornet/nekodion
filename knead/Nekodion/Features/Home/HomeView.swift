import SwiftUI

struct HomeView: View {
    @ObservedObject var authService: AuthenticationService
    @State private var res: String = ""

    private var apiClient: APIClient { APIClient(authService: authService) }

    var body: some View {
        VStack {
            Text("ホーム")
            Text("レスポンス: \(res)")
        }
        .task {
            await fetchTotalAssets()
        }
    }

    private func fetchTotalAssets() async {
        do {
            let data = try await apiClient.get("/api/transactions/total-assets")
            res = String(data: data, encoding: .utf8) ?? "Invalid response"
        } catch {
            authService.errorMessage = "資産の取得に失敗しました: \(error.localizedDescription)"
        }
    }
}

#Preview {
    HomeView(authService: AuthenticationService())
}
