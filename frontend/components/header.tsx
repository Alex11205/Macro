import Link from "next/link";

export default function Header() {

  return (
    <div>

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
        <Link href="/SavedList" className="text-gray-700 hover:text-blue-600">MACRO CACULATOR</Link>
        <Link href="/profile" className="text-gray-700 hover:text-blue-600">PROFILE</Link>

        
        
        
        </div>
        </div>
      </nav>

    </div>
  );
}



