//
//  Nekodion
//  ContentView.swift
//

import SwiftUI

struct ContentView: View {
    @ObservedObject var authService: AuthenticationService

    @State private var res: String = ""

    var body: some View {
        if authService.isLoading {
            ProgressView("ローディング中")
        } else if authService.isAuthenticated {
            VStack {
                Text("子猫こねこね")
                Text("レスポンス: \(res)")
                Button{
                    Task {
                        await authService.logout()
                    }
                } label: {
                    Text("ログアウト")
                }
            }
            .task {
                await fetchTotalAssets()
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

    private func fetchTotalAssets() async {
        do {
            guard let url = URL(string: "http://localhost:8080/api/transactions/total-assets") else { return }
            print("Fetching total assets from: \(url)")

            let token: String
            do {
                token = try await authService.getAccessToken()
            } catch {
                authService.errorMessage = "トークンが取得できませんでした: \(error.localizedDescription)"
                return
            }

            var request = URLRequest(url: url)
            request.httpMethod = "GET"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")

            let (data, _) = try await URLSession.shared.data(for: request)
            res = String(data: data, encoding: .utf8) ?? "Invalid response"
        } catch {
            authService.errorMessage = "資産の取得に失敗しました: \(error.localizedDescription)"
            print("Error fetching total assets: \(error)")
        }
    }
}

#Preview {
    ContentView(authService: AuthenticationService())
}
