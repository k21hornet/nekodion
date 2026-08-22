import Foundation

private let yenNumberFormatter: NumberFormatter = {
    let formatter = NumberFormatter()
    formatter.numberStyle = .decimal
    formatter.maximumFractionDigits = 0
    return formatter
}()

func formatYen(_ amount: Decimal) -> String {
    "¥" + (yenNumberFormatter.string(from: amount as NSDecimalNumber) ?? "0")
}

func formatSignedYen(_ amount: Decimal, direction: String) -> String {
    let sign = direction == "OUT" ? "-" : ""
    return "¥\(sign)" + (yenNumberFormatter.string(from: abs(amount) as NSDecimalNumber) ?? "0")
}

func formatBalance(_ amount: Decimal) -> String {
    if amount >= 0 {
        return "+" + formatYen(amount)
    }
    return "¥-" + (yenNumberFormatter.string(from: abs(amount) as NSDecimalNumber) ?? "0")
}
