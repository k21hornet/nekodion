import { fetcher } from "@/util/fetcher";

export default async function Transactions() {
  const res1 = await fetcher.get("/transactions");
  console.log("res1: ", res1);
  const res2 = await fetcher.get("/transactions/1");
  console.log("res2: ", res2);
  const res3 = await fetcher.get("/transactions/total-assets");
  console.log("res3: ", res3);

  return (
    <div>
      子猫こねこねこねこねこねこねこねこねこねこねこねこねこねこねこねこねこねこねこねこね
    </div>
  );
}
