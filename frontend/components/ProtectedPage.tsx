
import { useEffect, useState } from "react";
import { useRouter } from "next/router";

export default function ProtectedPage({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();

  const [hasToken] = useState(() => {
    if (typeof window === "undefined") {
      return false;
    }

    return Boolean(localStorage.getItem("token"));
  });

 useEffect(() => {
    if (!hasToken) {
      router.replace("/Signin");
    }
  }, [hasToken, router]);

    if (!hasToken) {
    return <div>Loading...</div>;
  }

  return <>{children}</>;
}