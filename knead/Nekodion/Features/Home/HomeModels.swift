import SwiftUI

struct ExpenseItem: Identifiable {
    let id = UUID()
    let name: String
    let amount: Decimal
    let color: Color
}
