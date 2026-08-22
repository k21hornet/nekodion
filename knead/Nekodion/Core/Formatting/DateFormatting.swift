import Foundation

private let dateTimeParser: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    formatter.locale = Locale(identifier: "en_US_POSIX")
    formatter.timeZone = TimeZone(identifier: "Asia/Tokyo")
    return formatter
}()

private let japaneseWeekdaySymbols = ["日", "月", "火", "水", "木", "金", "土"]

func formatDate(_ dateTimeString: String) -> String {
    guard let date = dateTimeParser.date(from: String(dateTimeString.prefix(19))) else {
        return dateTimeString
    }
    var calendar = Calendar(identifier: .gregorian)
    calendar.timeZone = TimeZone(identifier: "Asia/Tokyo") ?? .current
    let components = calendar.dateComponents([.year, .month, .day, .weekday], from: date)
    let weekday = japaneseWeekdaySymbols[(components.weekday ?? 1) - 1]
    return "\(components.year ?? 0)年\(components.month ?? 0)月\(components.day ?? 0)日（\(weekday)）"
}
