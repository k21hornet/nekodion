import Foundation

enum AppConfig {
    static var apiBaseURL: String {
        #if targetEnvironment(simulator)
        return "http://localhost:8080"
        #else
        return "https://nekodion.konekokonekone.com"
        #endif
    }
}
