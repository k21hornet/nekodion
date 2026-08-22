import Foundation

enum TransactionRoute: Hashable {
    case create
    case edit(id: Int)
}
