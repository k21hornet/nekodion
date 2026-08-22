//
//  Nekodion
//  AuthenticationService.swift
//

import Foundation
import Auth0
import Combine

@MainActor
class AuthenticationService: ObservableObject {
    @Published var isAuthenticated = false
    @Published var user: UserProfile?
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    private let credentialsManager = CredentialsManager(authentication: Auth0.authentication())
    
    init() {
        Task {
            await checkAuthenticationStatus()
        }
    }
    
    private func checkAuthenticationStatus() async {
        isLoading = true
        defer { isLoading = false }
        
        guard let credentials = try? await credentialsManager.credentials() else {
            isAuthenticated = false
            return
        }
        
        isAuthenticated = true
        // Get the user profile from the stored ID token
        user = try? credentialsManager.userProfile()
    }

    func getAccessToken() async throws -> String {
        let credentials = try await credentialsManager.credentials()
        return credentials.accessToken
    }
    
    func login() async {
        isLoading = true
        errorMessage = nil
        defer { isLoading = false }
        
        do {
            // offline_access is included in the default scope as of v3, shown here for clarity
            _ = try await Auth0
                .webAuth()
                .scope("openid profile email offline_access")
                .audience("https://api.nekodion.com")
                .useCredentialsManager(credentialsManager)
                .start()
            
            isAuthenticated = true
            // Get the user profile from the stored ID token
            user = try? credentialsManager.userProfile()
        } catch {
            errorMessage = "Login failed: \(error.localizedDescription)"
        }
    }
    
    func logout() async {
        isLoading = true
        defer { isLoading = false }

        do {
            try await Auth0
              .webAuth()
              .useCredentialsManager(credentialsManager)
              .logout()
            isAuthenticated = false
            user = nil
        } catch {
            errorMessage = "Logout failed: \(error.localizedDescription)"
        }
    }

    func reportError(_ message: String, _ error: Error) {
        errorMessage = "\(message): \(error.localizedDescription)"
    }
}
