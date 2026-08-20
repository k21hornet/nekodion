//
//  Nekodion
//  ContentView.swift
//

import SwiftUI

struct ContentView: View {
    @ObservedObject var authService: AuthenticationService

    var body: some View {
        if authService.isLoading {
            ProgressView("ローディング中")
        } else if authService.isAuthenticated {
            VStack {
                Text("子猫こねこね")
                Button{
                    Task {
                        await authService.logout()
                    }
                } label: {
                    Text("ログアウト")
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
    ContentView(authService: AuthenticationService())
}
