import SwiftUI

enum CategoryStyle {
    private static let styles: [String: (icon: String, color: Color)] = [
        "給与": ("banknote.fill", .green),
        "食費": ("fork.knife", .orange),
        "日用品": ("bag.fill", .mint),
        "交際費": ("person.2.fill", .pink),
        "趣味・娯楽": ("gamecontroller.fill", .purple),
        "交通費": ("bus.fill", .cyan),
        "美容・衣服": ("scissors", .pink),
        "医療・健康": ("heart.text.square.fill", .red),
        "教育・教養": ("book.fill", .teal),
        "大型出費": ("shippingbox.fill", .yellow),
        "住宅・水道光熱費": ("house.fill", .blue),
        "通信費": ("iphone", .indigo),
        "自動車": ("car.fill", .yellow),
        "税金・社会保険": ("doc.text.fill", .gray),
        "その他": ("ellipsis.circle.fill", .gray),
        "未分類": ("questionmark.circle.fill", .gray)
    ]

    static func iconName(for categoryTypeName: String) -> String {
        styles[categoryTypeName]?.icon ?? "questionmark.circle.fill"
    }

    static func color(for categoryTypeName: String) -> Color {
        styles[categoryTypeName]?.color ?? .gray
    }
}
