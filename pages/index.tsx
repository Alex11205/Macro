
// import Signin from "@/components/A_Signin";


// export default function Home() {
//   return (
//     <div>
//        {/* As an entrance to the page, only calling other components */}
//       < Signin />

//     </div>


//   );
// }
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
        {/* < Signin /> */}
        
      {/* <div className="max-w-6xl mx-auto rounded-2xl overflow-hidden shadow-lg flex flex-col md:flex-row"> */}
  {/* Left content area */}
  
  {/* <div className="md:w-1/2">
    <img className="w-full h-full object-cover min-w-[1000px] " src={"/images/background2.webp"} alt="background2"></img>
  </div> */}

  {/* Right image */}
  {/* <div className="md:w-1/2">
    <img className="w-full h-full object-cover min-w-[1000px] " src={"/images/background3.webp"} alt="background3"></img>
  </div>
</div> */}
    


      </main>
      

    </div>
  );
}