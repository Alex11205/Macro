// import { Navigate, Outlet, useLocation } from "react-router-dom";
// import { useAuth } from "@/components/AuthContext";

// export const ProtectedRoute = ({ children }) => {
//   const { token } = useAuth();
  
//   if (!token) {
//     // Force redirect unauthenticated users back to login
//     return <Navigate to="/login" replace />;
//   }
  
//   return children;
// };


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

  // prevent flicker
  if (checking) return <div>Loading...</div>;

  return <>{children}</>;
}