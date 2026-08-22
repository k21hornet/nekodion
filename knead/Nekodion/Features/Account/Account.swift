import SwiftUI

struct AccountView: View {
    var body: some View {
        ZStack(alignment: .bottom) {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            ScrollView {
                Text("口座")
                    .font(.title3.weight(.semibold))
                    .padding(.top, 12)
                ZStack {
                    RoundedRectangle(cornerRadius: 20)
                        .fill(.regularMaterial)
                        .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
                    VStack {
                        Text("銀行口座")
                            .font(.headline)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding()
                        HStack {
                            Text("三井住友銀行")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
                        }
                        .padding()
                        Divider()
                        HStack {
                            Text("三菱UFJ銀行")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
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
                        Text("クレカ口座")
                            .font(.headline)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding()
                        HStack {
                            Text("JCBカード")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
                        }
                        .padding()
                        Divider()
                        HStack {
                            Text("三井住友カード（VPassID）")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
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
                        Text("手動管理口座")
                            .font(.headline)
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding()
                        HStack {
                            Text("My財布")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
                        }
                        .padding()
                        Divider()
                        HStack {
                            Text("PayPay")
                            Spacer()
                            Text("¥123,456")
                                .fontWeight(.semibold)
                        }
                        .padding()
                    }
                }
                .padding(.horizontal)
            }

            Button {
            } label: {
                Text("＋口座を追加する")
                    .foregroundStyle(.white)
                    .frame(width: 200, height: 50)
                    .background(Color.blue)
                    .clipShape(RoundedRectangle(cornerRadius: 28))
                    .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
            }
            .padding(.bottom, 24)
            .accessibilityLabel("追加")
        }
    }
}

#Preview {
    AccountView()
}
