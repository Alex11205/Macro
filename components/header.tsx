import Link from "next/link";


export default function Header() {
  return (
    <div>
     

      {/* Navigation */}
      <nav className="bg-white shadow-md">
        <div className="max-w-5xl mx-auto flex justify-between items-center p-4">
          <Link href="/">
          <h1 className="text-xl font-bold text-green-600">
            Macro Tracker
          </h1>
          </Link>

          <div className="flex justify-center gap-8 text-gray-700 font-medium">
        <Link href="/" className="text-gray-700 hover:text-blue-600">HOME</Link><br></br>
        <Link href="/foods" className="text-gray-700 hover:text-blue-600">EXPLORE FOODS</Link><br></br>
        {/* <Link href="/trips" className="text-gray-700 hover:text-blue-600">TRIPS</Link><br></br> */}
        <Link href="/SavedList" className="text-gray-700 hover:text-blue-600">MACRO CACULATOR</Link>
        {/* <Link href="/aboutus" className="text-gray-700 hover:text-blue-600">ABOUT US</Link> */}
        
        </div>
        </div>
      </nav>

    </div>
  );
}



// import Link from "next/link";

// export default function Home() {
//   return (
//     <div className="min-h-screen bg-gray-100">

//       {/* Navbar */}
//       <nav className="bg-white shadow-md">
//         <div className="max-w-5xl mx-auto flex justify-between items-center p-4">
          
//           <h1 className="text-xl font-bold text-blue-600">
//             Travel App
//           </h1>

//           <div className="space-x-6">
//             <Link href="/" className="text-gray-700 hover:text-blue-600">
//               Home
//             </Link>
//             <Link href="/destinations" className="text-gray-700 hover:text-blue-600">
//               Destinations
//             </Link>
//             <Link href="/trips" className="text-gray-700 hover:text-blue-600">
//               Trips
//             </Link>
//             <Link href="/aboutus" className="text-gray-700 hover:text-blue-600">
//               About
//             </Link>
//           </div>
//         </div>
//       </nav>

//       {/* Page content */}
//       <main className="max-w-5xl mx-auto p-6">
//         <h2 className="text-2xl font-semibold mb-4">
//           Welcome to the Travel App
//         </h2>

//         <p className="text-gray-600">
//           Explore destinations and plan your trips easily.
//         </p>
//       </main>

//     </div>
//   );
// }