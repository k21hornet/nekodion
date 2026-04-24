import { fetcher } from "@/util/fetcher";

export async function getCategories() {
  return await fetcher.get("/categories");
}
