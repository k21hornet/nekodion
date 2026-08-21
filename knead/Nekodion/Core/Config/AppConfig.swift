import Foundation

enum AppConfig {
    static var apiBaseURL: String {
        Bundle.main.object(forInfoDictionaryKey: "APIBaseURL") as? String ?? "http://localhost:8080"
    }
}
