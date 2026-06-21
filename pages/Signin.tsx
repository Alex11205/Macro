// import { useState } from "react";
import Header from "../components/A_Header";
import SavingList from "../components/A_SavingList";
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
  
  // A welcome message which has no value initially
  //  const [message, setMessage] = useState('');

   // A boolean state to tell if the user has clicked the button
    //  const [isSubmit, setIsSubmit] = useState(false);

     // A function to handle submission
//   async function handleLogin(){
//     // event.preventDefault();
//     // setUsername(usernameInput);
//     // setPassword(passwdInput);
//     // alert("usernameInput: " + usernameInput + "; passwdInput: " + passwdInput)
//     // alert("username: " + username + "; password: " + password)


//     alert("before fetching");

//      try {
//     const res = await fetch("http://localhost:8080/api/users/login", {
//       method: "POST",
//   //     headers: {
//   //   "Authorization": token
//   // },
  
//   headers: {
//     "Content-Type": "application/json",
//   },
//       body: JSON.stringify({ username: username, password: password }),
//     });
//     alert("after fetching");
//     alert("token is: " + localStorage.getItem("token"));
//     const text = res.text();
// console.log("RAW RESPONSE:", text);
// alert("raw: " + text);
//     const data = await res.json();
//     const token = await data.token;
//     alert("data is: " + data);
//     console.log(token);
//     alert(token);
//     localStorage.setItem("token", token);

// //     if (res.status === 401) {
// //   localStorage.removeItem("token");
// //   router.push("/login");
// // }

//     router.push("/foods"); // go to protected page

//   } catch (err) {
//       console.error("ERROR:", err);
//   alert("fetch failed");
// }


//     // Update the following states
    
//     // setIsSubmit(true);
//     // setMessage('Enjoy your exploration!');
//   };

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
      {/* // Calling another component with a prop called username, passing the username data to another component */}
      {/* < Header username={username} /> */}

      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, Guest</h3>
      <br></br>
      {/* // An event handler and conditional rendering: If user has submitted, this form part will be invisible */}
      <form onSubmit={handleLogin}> 
        <div className="space-y-12">
        <h3 className="text-xl font-bold text-gray-800 mb-6">Pleases sign in</h3>
        <div>
           <label  className="block text-sm/6 font-medium text-black">Username</label>
        <input className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"
        // Current value is always the state
        value={username}
        // Detect changes in input and update the state
        // onSubmit={(event) => setUsername(event.target.value)}
        onChange={(event) => setUsername(event.target.value)}
        placeholder="Please enter your username"
        required
        
        // pattern=".{6,}" 
        title="User name must be at least 6 characters!" 
      />
      <label  className="block text-sm/6 font-medium text-black">Password</label>
      <input  className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out" 
        // Current value is always the state
        type="password"
        value={password}
        // Detect changes in input and update the state
        // onSubmit={(event) => setPassword(event.target.value)}
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
      
      {/* {message && <p>{message}</p>} */}
      

      {/* // An event handler and conditional rendering: If user has submitted, the saving list component will be called and shown up */}
      {/* {isSubmit && < Foods />} */}
      
      </main>
    </div>
  );

 
}
