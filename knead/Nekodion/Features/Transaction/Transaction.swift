import SwiftUI

struct TransactionView: View {
    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            ScrollView {
                Text("入出金")
                    .font(.title3.weight(.semibold))
                    .padding(.top, 12)
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    VStack {
                        HStack {
                            Text("未読の入出金")
                                .font(.headline)
                                .fontWeight(.semibold)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .foregroundStyle(Color.gray)
                                .padding()
                            Button("既読をつける"){
                            }
                            .padding()
                        }
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
                    }
                }
                .padding(.horizontal)
                
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    VStack {
                        Text("入出金一覧")
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
                        
                        Text("2026年8月19日（水）")
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
}

#Preview {
    TransactionView()
}
