

import Link from "next/link";
import { useRouter } from "next/router";
import { type FormEvent, useState } from "react";

export default function Signin() {

  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

async function handleLogin(event: FormEvent<HTMLFormElement>) {
  event.preventDefault();

  try {
    const res = await fetch(`${API_BASE_URL}/api/users/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });

    if(res.status === 401)
      setMessage("Username or password is incorrect!")
    if(res.status === 404)
      setMessage(`Username '${username}' does not exist!`)
    if (!res.ok) {
      const errorText = await res.text();
      console.error("LOGIN FAILED:", res.status, errorText);

      return;
    }

    const data = await res.json();

    localStorage.setItem("token", data.token);
    router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);

  }
}

  return (
    <div>
      <main className="max-w-5xl mx-auto p-6 text-black">


      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, Guest</h3>
      <br></br>

      <form onSubmit={handleLogin}> 
        <div className="space-y-12">
        <h3 className="text-xl font-bold text-gray-800 mb-6">Pleases sign in</h3>
        <div>
           <label  className="block text-sm/6 font-medium text-black">Username</label>
        <input className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"

        value={username}

        onChange={(event) => setUsername(event.target.value)}
        placeholder="Please enter your username"
        required
        
        title="User name must be at least 6 characters!" 
      />
      <label  className="block text-sm/6 font-medium text-black">Password</label>
      <input  className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out" 

        type="password"
        value={password}

        onChange={(event) => setPassword(event.target.value)}
        placeholder="Please enter your password" 
        required
        pattern=".{8,}" 
        title="Password must be at least 8 characters!" 
      />
      {message && <p className="text-lg text-red-700">{message}</p>}
      </div>
        <button className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="submit">Sign in</button>
        </div>
      </form>
      <br></br>
      <Link href="/Signup" className="text-gray-700 hover:text-blue-600">New User?</Link><br></br>
      

    
      
      </main>
    </div>
  );

 
}
