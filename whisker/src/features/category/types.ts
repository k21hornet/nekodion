export type CategoryItem = {
  categoryId: number;
  categoryName: string;
};

export type CategoryTypeResponse = {
  categoryTypeId: number;
  categoryTypeName: string;
  isIncome: boolean;
  categories: CategoryItem[];
};
