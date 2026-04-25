"use client";

import Link from "next/link";
import { MdOutlineLogout } from "react-icons/md";

export default function Error() {
  return (
    <div className="flex flex-col items-center justify-center">
      <p>ただいまアクセスできません。しばらくお待ちください。</p>
      <Link
        href="/auth/logout"
        className="flex items-center gap-1"
        prefetch={false}
      >
        ログアウト
        <MdOutlineLogout className="text-lg" />
      </Link>
    </div>
  );
}
