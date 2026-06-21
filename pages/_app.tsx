import "@/styles/globals.css";
import Header from "@/components/header";
import type { AppProps } from "next/app";
import Footer from "@/components/footer";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Foods from "./foods";
import SavedPage from "./SavedList";
import Signin from "./Signin";
import ProtectedRoute from "@/components/ProtectedPage";



export default function App({ Component, pageProps }: AppProps) {
  return (


    <div className="min-h-screen bg-gray-100 text-gray-800"> 
      <Header />

      <main className="max-w-5xl mx-auto p-6">
        <Component {...pageProps} />
      </main>

      <Footer />
    </div>

    
  );
}
