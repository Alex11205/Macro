import { useState } from "react";

import Foods from "@/pages/foods";
import Link from "next/link";

export default function Signup() {

   const [usernameInput, setUsernameInput] = useState("");
   const [passwdInput, setPasswdInput] = useState("");
   const [emailInput, setEmailInput] = useState("");


  const [username, setUsername] = useState("Guest");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");


   const [message, setMessage] = useState('');


     const [isSubmit, setIsSubmit] = useState(false);

  const handleSubmit = (event: React.SubmitEvent) => {
    event.preventDefault();

    setUsername(usernameInput);
    setPassword(passwdInput);
    setEmail(emailInput)
    setIsSubmit(true);
    setMessage('Enjoy your exploration!');
  };

  return (
    <div>
      <main className="max-w-5xl mx-auto p-6 text-black">


      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, {username}</h3>
      <br></br>

      {!isSubmit && <form onSubmit={handleSubmit}> 
        <div className="space-y-12">
        <h3 className="text-xl font-bold text-gray-800 mb-6">Pleases sign up</h3>
        <div>
           <label className="block text-sm/6 font-medium text-black">Email</label>
        <input className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"

        value={emailInput}

        onSubmit={(event) => setEmail(event.target.value)}
        onChange={(event) => setEmailInput(event.target.value)}
        placeholder="Please enter your email"
        required
        type="email"
        pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
        title="Please enter valid email!" 
      />
      <label className="block text-sm/6 font-medium text-black">Username</label>
        <input className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"

        value={usernameInput}

        onSubmit={(event) => setUsername(event.target.value)}
        onChange={(event) => setUsernameInput(event.target.value)}
        placeholder="Please enter your username"
        required
        pattern=".{6,}" 
        title="User name must be at least 6 characters!" 
      />
      <label  className="block text-sm/6 font-medium text-black">Password</label>
      <input  className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out" 

        type="password"
        value={passwdInput}

        onSubmit={(event) => setPassword(event.target.value)}
        onChange={(event) => setPasswdInput(event.target.value)}
        placeholder="Please enter your password"
        required
        pattern=".{8,}" 
        title="Password must be at least 8 characters!" 
        
      />
      </div>
        <button className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="submit">Sign up</button>
        </div>
      </form>}
      <br></br>
      <Link href="/Signin" className="text-gray-700 hover:text-blue-600">Already a member?</Link><br></br>
      {message && <p>{message}</p>}
      


      {isSubmit && < Foods />}
      
      </main>
    </div>
  );

 
}
