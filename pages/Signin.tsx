// import { useState } from "react";

import Foods from "@/pages/foods";
import Link from "next/link";
import { useRouter } from "next/router";
import { type FormEvent, useState } from "react";

export default function Signin() {
  // Use a state to temperarily save the user input
  const router = useRouter();
  //  const [usernameInput, setUsernameInput] = useState("");
  //  const [passwdInput, setPasswdInput] = useState("");

   // Use a state to save username that will be submitted, the initiating value is 'Guest'
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  

async function handleLogin(event: FormEvent<HTMLFormElement>) {
  event.preventDefault();

  try {
    const res = await fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error("LOGIN FAILED:", res.status, errorText);
      alert("Login failed");
      return;
    }

    const data = await res.json();

    console.log("LOGIN RESPONSE:", data);
    alert("token is: " + data.token);

    localStorage.setItem("token", data.token);
    router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);
    alert("fetch failed");
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
        
        // pattern=".{6,}" 
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
      </div>
        <button className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="submit">Sign in</button>
        </div>
      </form>
      <br></br>
      <Link href="/Signup" className="text-gray-700 hover:text-blue-600">Sign up here</Link><br></br>
      

    
      
      </main>
    </div>
  );

 
}
