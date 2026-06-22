
import { useEffect, useState } from "react";
import { useRouter } from "next/router";

export default function ProtectedPage({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      router.replace("/Signin");
    } else {
      setChecking(false);
    }
  }, []);

  if (checking) return <div>Loading...</div>;

  return <>{children}</>;
}