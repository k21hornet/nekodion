import type { LucideIcon } from "lucide-react";
import {
  Banknote,
  UtensilsCrossed,
  ShoppingBag,
  Users,
  Gamepad2,
  Bus,
  Scissors,
  HeartPulse,
  BookOpen,
  Package,
  Home,
  Smartphone,
  Car,
  FileText,
  MoreHorizontal,
  HelpCircle,
} from "lucide-react";

type CategoryIconConfig = {
  Icon: LucideIcon;
  bgColor: string;
};

export const CATEGORY_ICON_MAP: Record<string, CategoryIconConfig> = {
  給与: { Icon: Banknote, bgColor: "bg-emerald-400" },
  食費: { Icon: UtensilsCrossed, bgColor: "bg-orange-400" },
  日用品: { Icon: ShoppingBag, bgColor: "bg-lime-400" },
  交際費: { Icon: Users, bgColor: "bg-pink-400" },
  "趣味・娯楽": { Icon: Gamepad2, bgColor: "bg-purple-400" },
  交通費: { Icon: Bus, bgColor: "bg-sky-400" },
  "美容・衣服": { Icon: Scissors, bgColor: "bg-rose-400" },
  "医療・健康": { Icon: HeartPulse, bgColor: "bg-red-400" },
  "教育・教養": { Icon: BookOpen, bgColor: "bg-teal-400" },
  "大型出費": { Icon: Package, bgColor: "bg-amber-400" },
  "住宅・水道光熱費": { Icon: Home, bgColor: "bg-blue-400" },
  通信費: { Icon: Smartphone, bgColor: "bg-indigo-400" },
  自動車: { Icon: Car, bgColor: "bg-yellow-500" },
  "税金・社会保険": { Icon: FileText, bgColor: "bg-slate-400" },
  その他: { Icon: MoreHorizontal, bgColor: "bg-gray-400" },
  未分類: { Icon: HelpCircle, bgColor: "bg-gray-300" },
};

const DEFAULT_ICON_CONFIG: CategoryIconConfig = {
  Icon: HelpCircle,
  bgColor: "bg-gray-300",
};

export const getCategoryIconConfig = (
  categoryTypeName: string,
): CategoryIconConfig => {
  return CATEGORY_ICON_MAP[categoryTypeName] ?? DEFAULT_ICON_CONFIG;
};
