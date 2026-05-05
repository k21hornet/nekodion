"use client";

import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Label,
} from "recharts";
import { getCategoryColor } from "@/features/category/const";

type ExpenseItem = { categoryTypeName: string; totalAmount: number };

type Props = { items: ExpenseItem[] };

const CustomTooltip = ({
  active,
  payload,
}: {
  active?: boolean;
  payload?: { name: string; value: number }[];
}) => {
  if (!active || !payload?.length) return null;
  const { name, value } = payload[0];
  return (
    <div className="border-border bg-card rounded-lg border px-3 py-2 text-xs shadow-sm">
      <p className="font-medium">{name}</p>
      <p className="text-muted-foreground">¥{value.toLocaleString()}</p>
    </div>
  );
};

export const ExpensePieChart = ({ items }: Props) => {
  if (items.length === 0) return null;

  const total = items.reduce((sum, item) => sum + item.totalAmount, 0);

  const data = items.map((item) => ({
    name: item.categoryTypeName,
    value: item.totalAmount,
    fill: getCategoryColor(item.categoryTypeName),
  }));

  return (
    <ResponsiveContainer width="100%" height={220}>
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          innerRadius={65}
          outerRadius={95}
          paddingAngle={2}
          dataKey="value"
          nameKey="name"
          startAngle={90}
          endAngle={-270}
        >
          {data.map((entry, index) => (
            <Cell key={index} fill={entry.fill} stroke="transparent" />
          ))}
          <Label
            content={() => (
              <text
                x="50%"
                y="50%"
                textAnchor="middle"
                dominantBaseline="middle"
              >
                <tspan x="50%" dy="-0.6em" fontSize="11" fill="#888">
                  支出合計
                </tspan>
                <tspan
                  x="50%"
                  dy="1.5em"
                  fontSize="15"
                  fontWeight="bold"
                  fill="#111"
                >
                  ¥{total.toLocaleString()}
                </tspan>
              </text>
            )}
            position="center"
          />
        </Pie>
        <Tooltip content={<CustomTooltip />} />
      </PieChart>
    </ResponsiveContainer>
  );
};
