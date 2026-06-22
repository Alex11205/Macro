
import Foods from "@/pages/foods";
import Link from "next/link";
import { use, useEffect, useState } from "react";
import ProtectedPage from "@/components/ProtectedPage";

export default function Profile() {


type profile = {
  id: bigint;
  username: string;
  email: string;
  password: string
 
};

   const [userProfile, setUserProfile] = useState<profile>({
    id: null,
  username: '',
  email: '',
  password: '',
   });

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [oldEmail, setOldEmail] = useState("");
  const [newEmail, setNewEmail] = useState("");

    const [isEmailWrong, setIsEmailWrong] = useState(false);
    const [isPasswordWrong, setIsPasswordWrong] = useState(false);

//      const [wrongEmailMessage, setWrongEmailMessage] = useState('');
//    const [wrongPasswordMessage, setWrongPasswordMessage] = useState('');

     const [isSubmit, setIsSubmit] = useState(false);

      useEffect(() => {
         // const stored = JSON.parse(localStorage.getItem("savedCards") || "[]");
         // setSavedItems(stored);
         async function fetchUserData() {
           try {
            
     
            const token = localStorage.getItem("token");
            
             const userDataResponse = await fetch("http://localhost:8080/api/users/profile", {
               method: "GET",
               headers: {
             "Content-Type": "application/json",
             "Authorization": token,
           },
             });
     
         if (!userDataResponse.ok) {
           const userDataErrorText = await userDataResponse.text();
           console.error("User food FETCH FAILED:", userDataResponse.status, userDataErrorText);
           alert("Fetching user data failed");
           return;
         }
     
     
         const userData = await userDataResponse.json();
         setUserProfile(userData);
         console.log("User data Fetch response: ", userData);
     
         // alert("Food fetch successfully!");
           } catch (err) {
             console.error("ERROR:", err);
         alert("fetch failed");
           }
         }
         fetchUserData();
       }, []);


  async function handleSubmitEmail(event: React.SubmitEvent) {
    event.preventDefault();

    if(oldEmail !== userProfile.email)
        setIsEmailWrong(true);
        else {
            setIsEmailWrong(false);
            const token = localStorage.getItem("token");
            try {
    const res = await fetch("http://localhost:8080/api/users", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": token,
      },
      body: JSON.stringify({
        id: userProfile.id, 
        username: userProfile.username, 
        email: newEmail, 
        password: userProfile.password}),
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error("Changing email FAILED:", res.status, errorText);
      alert("Changing email failed");
      return;
    }

    const data = await res.json();

    console.log("Change email RESPONSE:", data);
    alert("Email Changed successfully!");
      window.location.reload();
    
    // router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);
    alert("fetch failed");
  }

  };

        }
    

 async function handleSubmitPassword(event: React.SubmitEvent) {
    event.preventDefault();

    if(oldPassword !== userProfile.password)
        setIsPasswordWrong(true);
        else {
            setIsPasswordWrong(false);
            const token = localStorage.getItem("token");
            try {
    const res = await fetch("http://localhost:8080/api/users", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": token,
      },
      body: JSON.stringify({
        id: userProfile.id, 
        username: userProfile.username, 
        email: userProfile.email, 
        password: newPassword}),
    });

    if (!res.ok) {
      const errorText = await res.text();
      console.error("Changing password FAILED:", res.status, errorText);
      alert("Changing password failed");
      return;
    }

    const data = await res.json();

    console.log("Change password RESPONSE:", data);
    alert("Password Changed successfully!");
     window.location.reload();
    
    // router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);
    alert("fetch failed");
  }

  };

        }

  return (
    <ProtectedPage>
    <div>
      <main className="max-w-5xl mx-auto p-6 text-black">


      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, {userProfile.username}</h3>
      

      <br></br>
     
      


      
      <form onSubmit={handleSubmitEmail}> 
        <div className="space-y-3">
       
        <div>
            <p className="text-lg font-bold text-gray-800 mb-6">Change your email</p>
           <label className="block text-sm/6 font-medium text-black">Your old email:</label>
        <input className="w-full px-4 py-2  border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"

        value={oldEmail}

        // onSubmit={(event) => setOldEmail(event.target.value)}
        onChange={(event) => setOldEmail(event.target.value)}
        placeholder="Old email"
        required
        type="email"
        pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
        title="Please enter valid email!" 
      />
      <label className="block text-sm/6 font-medium text-black">Your new email:</label>
        <input className="w-full px-4 py-2  border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"

        value={newEmail}

        // onSubmit={(event) => setNewEmail(event.target.value)}
        onChange={(event) => setNewEmail(event.target.value)}
        placeholder="New email"
        required
        type="email"
        pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
        title="Please enter valid email!" 
      />
      </div>
      {isEmailWrong && <p className="text-red-600">Old email is incorrect!</p>}
        <button className=" py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="submit">Change</button>
        </div>
      </form>
    <br></br>
    
      <form onSubmit={handleSubmitPassword}> 
        <div className="space-y-3">
       
        <div>
    <p className="text-lg font-bold text-gray-800 mb-6">Change your password</p>
      
      <label  className="block text-sm/6 font-medium text-black">Your old password:</label>
      <input  className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out" 

        type="password"
        value={oldPassword}

        // onSubmit={(event) => setOldPassword(event.target.value)}
        onChange={(event) => setOldPassword(event.target.value)}
        placeholder="Old password"
        required
        pattern=".{8,}" 
        title="Password must be at least 8 characters!" 
        
      />
      <label  className="block text-sm/6 font-medium text-black">Your new password:</label>
      <input  className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out" 

        type="password"
        value={newPassword}

        // onSubmit={(event) => setNewPassword(event.target.value)}
        onChange={(event) => setNewPassword(event.target.value)}
        placeholder="New password"
        required
        pattern=".{8,}" 
        title="Password must be at least 8 characters!" 
        
      />
      </div>
      {isPasswordWrong && <p className="text-red-600">Old password is incorrect!</p>}
        <button className="py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="submit">Change</button>
        </div>
      </form>
      <br></br>
    
      
      </main>
    </div>
    </ProtectedPage>
  );

 
}
