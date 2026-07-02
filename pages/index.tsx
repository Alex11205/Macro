
import Signin from "@/pages/Signin";
import Link from "next/link";
import Header from "@/components/header";
import Hero from "@/components/Hero";

export default function Home() {
  return (
    <div  className="min-h-screen  bg-gray-100">
     <Hero
        title="WELCOME TO MACROTRACKER"
        subtitle=""
        backgroundImage="/images/background1.webp"
      />

      {/* < Header/> */}
      {/* <main className="max-w-5xl mx-auto p-6 text-black"> */}
      <main className="min-h-screen  mx-auto p-6 text-black">


      </main>
      

    </div>
  );
}