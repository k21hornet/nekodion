import { getMonthlyDataAction } from "@/features/transaction/actions";
import { MonthlyPage } from "./_components";

export default async function Monthly() {
  const now = new Date();
  const initialData = await getMonthlyDataAction(
    now.getFullYear(),
    now.getMonth() + 1,
  );
  return <MonthlyPage initialData={initialData} />;
}
