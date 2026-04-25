"use client";

import type { LucideIcon } from "lucide-react";

type Props = {
  Icon: LucideIcon;
  bgColor?: string;
};

export const CategoryIcon = ({ Icon, bgColor }: Props) => {
  return (
    <div
      className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full ${bgColor ?? "bg-gray-300"}`}
    >
      <Icon size={20} color="white" />
    </div>
  );
};
