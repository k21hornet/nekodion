import SwiftUI

struct TransactionFormView: View {
    @ObservedObject var authService: AuthenticationService
    @StateObject private var viewModel: TransactionFormViewModel
    @Environment(\.dismiss) private var dismiss
    let onSaved: () -> Void

    init(mode: TransactionRoute, authService: AuthenticationService, onSaved: @escaping () -> Void) {
        self.authService = authService
        self.onSaved = onSaved
        _viewModel = StateObject(wrappedValue: TransactionFormViewModel(mode: mode, authService: authService))
    }

    var body: some View {
        ZStack {
            Color.blue.opacity(0.06)
                .ignoresSafeArea()
            if viewModel.isLoading {
                ProgressView()
            } else {
                ScrollView {
                    formCard
                    if let message = viewModel.validationMessage {
                        Text(message)
                            .foregroundStyle(.red)
                            .font(.callout)
                            .padding(.horizontal)
                    }
                    saveButton
                    if viewModel.isEditMode && viewModel.isDeletable {
                        deleteButton
                    }
                }
            }
        }
        .navigationTitle(viewModel.isEditMode ? "入出金を編集" : "入出金を追加")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            await viewModel.loadInitialData()
        }
    }

    private var formCard: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(.regularMaterial)
                .shadow(color: .black.opacity(0.1), radius: 10, x: 0, y: 4)
            VStack(alignment: .leading, spacing: 16) {
                Picker("種別", selection: $viewModel.direction) {
                    Text("支出").tag("OUT")
                    Text("収入").tag("IN")
                }
                .pickerStyle(.segmented)
                .onChange(of: viewModel.direction) { _, _ in
                    viewModel.directionChanged()
                }

                Picker("カテゴリ", selection: $viewModel.selectedCategoryId) {
                    Text("選択してください").tag(Int?.none)
                    ForEach(viewModel.filteredCategories) { group in
                        Section(group.categoryTypeName) {
                            ForEach(group.categories) { category in
                                Text(category.categoryName).tag(Int?.some(category.categoryId))
                            }
                        }
                    }
                }

                Picker("口座", selection: $viewModel.selectedAccountId) {
                    Text("未選択").tag(Int?.none)
                    ForEach(viewModel.flattenedAccounts) { account in
                        Text(account.accountName).tag(Int?.some(account.accountId))
                    }
                }

                TextField("金額", text: $viewModel.amountText)
                    .keyboardType(.numberPad)

                TextField("取引名", text: $viewModel.transactionName)

                DatePicker("日時", selection: $viewModel.selectedDate)

                TextField("メモ（任意）", text: $viewModel.description, axis: .vertical)
            }
            .padding()
        }
        .padding(.horizontal)
    }

    private var saveButton: some View {
        Button {
            Task {
                if await viewModel.save() {
                    onSaved()
                    dismiss()
                }
            }
        } label: {
            Text(viewModel.isSaving ? "保存中..." : "保存する")
                .foregroundStyle(.white)
                .frame(maxWidth: .infinity, minHeight: 50)
                .background(Color.blue)
                .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .disabled(viewModel.isSaving)
        .padding(.horizontal)
        .padding(.top, 8)
    }

    private var deleteButton: some View {
        Button(role: .destructive) {
            Task {
                if await viewModel.delete() {
                    onSaved()
                    dismiss()
                }
            }
        } label: {
            Text("削除する")
                .foregroundStyle(.red)
                .frame(maxWidth: .infinity, minHeight: 50)
        }
        .disabled(viewModel.isSaving)
        .padding(.horizontal)
    }
}

#Preview {
    NavigationStack {
        TransactionFormView(mode: .create, authService: AuthenticationService()) {}
    }
}
