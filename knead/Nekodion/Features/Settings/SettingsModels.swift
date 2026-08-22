import Foundation

struct EmailForwardingAddress: Decodable {
    let address: String
}

struct EmailForwardingConfirmation: Decodable {
    let fromAddress: String
    let subject: String
    let bodyText: String
    let receivedAt: String
}
