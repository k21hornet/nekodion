import SwiftUI
import Charts

struct HomeView: View {
    @ObservedObject var authService: AuthenticationService
    @State private var res: String = ""
    @State private var isTotalAssetsHidden = false
    
    private var apiClient: APIClient { APIClient(authService: authService) }
    
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            ScrollView {
                // 総資産
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .frame(height: 100)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    HStack {
                        VStack(alignment: .leading) {
                            Text("総資産")
                                .font(.headline)
                                .fontWeight(.semibold)
                            HStack(spacing: 4) {
                                Text(isTotalAssetsHidden ? "¥ ---,---" : "¥1,234,567")
                                    .font(.largeTitle)
                                    .fontWeight(.semibold)
                                Button {
                                    isTotalAssetsHidden.toggle()
                                } label: {
                                    Image(systemName: isTotalAssetsHidden ? "eye.slash" : "eye")
                                }
                                .foregroundStyle(.secondary)
                                .accessibilityLabel("総資産の表示切替")
                            }
                            
                        }
                        Spacer()
                        Image(.nekoIcon).resizable().frame(width: 100, height: 100)
                    }
                    .padding()
                }
                .padding(.horizontal)
                
                // 月次
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .frame(height: 220)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    VStack {
                        Text("8月収支")
                            .font(.headline)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.gray)
                            .padding(.horizontal)
                        HStack {
                            ZStack {
                                Chart {
                                    ForEach(expenseItems) { item in
                                        SectorMark(
                                            angle: .value("金額", item.amount),
                                            innerRadius: .ratio(0.58),
                                            angularInset: 2
                                        )
                                        .foregroundStyle(item.color)
                                    }
                                }
                                VStack(spacing: 2) {
                                    Text("支出合計")
                                        .font(.caption2)
                                        .foregroundStyle(.secondary)
                                    Text("¥100,000")
                                        .font(.caption)
                                        .fontWeight(.bold)
                                }
                            }
                            .frame(width: 140, height: 140)
                            
                            VStack {
                                HStack {
                                    Text("収入")
                                        .foregroundStyle(Color.gray)
                                    Spacer()
                                    Text("¥80,000")
                                        .font(.title2)
                                        .fontWeight(.semibold)
                                        .foregroundStyle(Color.blue)
                                }
                                HStack {
                                    Text("支出")
                                        .foregroundStyle(Color.gray)
                                    Spacer()
                                    Text("¥100,000")
                                        .font(.title2)
                                        .fontWeight(.semibold)
                                        .foregroundStyle(Color.red)
                                }
                                HStack {
                                    Text("収支")
                                        .foregroundStyle(Color.gray)
                                    Spacer()
                                    Text("¥80,000")
                                        .font(.title2)
                                        .fontWeight(.semibold)
                                        .foregroundStyle(Color.blue)
                                }
                            }
                        }
                        .padding(.horizontal)
                    }
                }
                .padding(.horizontal)
                
                // 最近の入出金
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    VStack {
                        Text("最近の入出金")
                            .font(.headline)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.gray)
                            .padding()
                        Text("2026年8月21日（金）")
                            .font(.callout)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.gray)
                            .padding(.horizontal)
                        Divider()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("イオン")
                            Spacer()
                            Text("¥-1,200")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        HStack {
                            Image(systemName: "graduationcap.fill")
                            Text("お小遣い")
                            Spacer()
                            Text("¥10,000")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.blue)
                        }
                        .padding()
                        
                        Text("2026年8月20日（木）")
                            .font(.callout)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.gray)
                            .padding(.horizontal)
                        Divider()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("セブンイレブン")
                            Spacer()
                            Text("¥-900")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        HStack {
                            Image(systemName: "graduationcap.fill")
                            Text("読書")
                            Spacer()
                            Text("¥-1,500")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("カフェ")
                            Spacer()
                            Text("¥-1,000")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                        HStack {
                            Image(systemName: "fork.knife")
                            Text("朝食")
                            Spacer()
                            Text("¥-600")
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.red)
                        }
                        .padding()
                    }
                }
                .padding(.horizontal)
                
                Spacer()
            }
            .task {
                await fetchTotalAssets()
            }

            Button {
            } label: {
                Image(systemName: "plus")
                    .font(.title2.weight(.bold))
                    .foregroundStyle(.white)
                    .frame(width: 56, height: 56)
                    .background(Color.blue)
                    .clipShape(Circle())
                    .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
            }
            .padding(.trailing, 20)
            .padding(.bottom, 24)
            .accessibilityLabel("追加")
        }
    }

    private var expenseItems: [ExpenseItem] {
        [
            ExpenseItem(name: "食費", amount: 35_000, color: .orange),
            ExpenseItem(name: "住居費", amount: 40_000, color: .blue),
            ExpenseItem(name: "その他", amount: 25_000, color: .green)
        ]
    }
    
    private func fetchTotalAssets() async {
        do {
            let data = try await apiClient.get("/api/transactions/total-assets")
            res = String(data: data, encoding: .utf8) ?? "Invalid response"
        } catch {
            authService.errorMessage = "資産の取得に失敗しました: \(error.localizedDescription)"
        }
    }
}

private struct ExpenseItem: Identifiable {
    let id = UUID()
    let name: String
    let amount: Double
    let color: Color
}

#Preview {
    HomeView(authService: AuthenticationService())
}
