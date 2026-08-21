//
//  Nekodion
//  NekodionApp.swift
//

import SwiftUI

@main
struct NekodionApp: App {
    @StateObject private var authService = AuthenticationService()
    var body: some Scene {
        WindowGroup {
            RootView(authService: authService)
        }
    }
}
