import Foundation

struct AccountSummaryResponse: Decodable {
    let accountType: String
    let accounts: [AccountItem]
}

struct AccountItem: Decodable, Identifiable {
    let accountId: Int
    let accountName: String
    let totalAmount: Decimal

    var id: Int { accountId }
}
