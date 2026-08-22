import Foundation

struct CategoryTypeResponse: Decodable, Identifiable {
    let categoryTypeId: Int
    let categoryTypeName: String
    let isIncome: Bool
    let categories: [CategoryItem]

    var id: Int { categoryTypeId }
}

struct CategoryItem: Decodable, Identifiable {
    let categoryId: Int
    let categoryName: String

    var id: Int { categoryId }
}
