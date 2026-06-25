
import Foods from "@/pages/foods";
import Link from "next/link";
import { use, useEffect, useState } from "react";
import ProtectedPage from "@/components/ProtectedPage";
import { useRouter } from "next/router";


export default function Profile() {

  type UserProfile = {
  id: number;
  username: string;
  email: string;
  role: "USER" | "ADMIN";
};

// type profile = {
//   id: bigint;
//   username: string;
//   email: string;
//   password: string
 
// };

  //  const [userProfile, setUserProfile] = useState<profile>({
  //   id: BigInt(0),
  // username: '',
  // email: '',
  // password: '',
  //  });

  const [profile, setProfile] = useState<UserProfile | null>(null);
const [users, setUsers] = useState<UserProfile[]>([]);

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [oldEmail, setOldEmail] = useState("");
  const [newEmail, setNewEmail] = useState("");

    const [isEmailWrong, setIsEmailWrong] = useState(false);
    const [isPasswordWrong, setIsPasswordWrong] = useState(false);
 const router = useRouter();
//      const [wrongEmailMessage, setWrongEmailMessage] = useState('');
//    const [wrongPasswordMessage, setWrongPasswordMessage] = useState('');

    //  const [isSubmit, setIsSubmit] = useState(false);

     function handleLogOut(){
        localStorage.removeItem("token");
        router.replace("/Signin");

     }

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
             "Authorization": `Bearer ${token}`,
           },
             });
     
             if (userDataResponse.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");}

         if (!userDataResponse.ok) {
           const userDataErrorText = await userDataResponse.text();
           console.error("User FETCH FAILED:", userDataResponse.status, userDataErrorText);
        //    alert("Fetching user data failed");
           return;
         }

         
     
     
     
         const userData = await userDataResponse.json();
         
         setProfile(userData);
         console.log("User data Fetch response: ", userData);
     if (userData.role === "ADMIN") {
  fetchUser();
}
         // alert("Food fetch successfully!");
           } catch (err) {
             console.error("ERROR:", err);
        //  alert("fetch failed");
           }
         }
         fetchUserData();
       }, []);



       async function fetchUser() {
           try {

            const token = localStorage.getItem("token");
            
             const userDataResponse = await fetch("http://localhost:8080/api/users", {
               method: "GET",
               headers: {
             "Content-Type": "application/json",
             "Authorization": `Bearer ${token}`,
           },
             });

              if (userDataResponse.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");}
     
         if (!userDataResponse.ok) {
           const userDataErrorText = await userDataResponse.text();
           console.error("User Table FETCH FAILED:", userDataResponse.status, userDataErrorText);
        //    alert("Fetching user data failed");
           return;
         }

        
     
     
     
         const userTableData = await userDataResponse.json();
         
         setUsers(userTableData);
         console.log("User data Fetch response: ", userTableData);
     

         // alert("Food fetch successfully!");
           } catch (err) {
             console.error("ERROR:", err);
        //  alert("fetch failed");
           }
         }
         
       


  async function handleSubmitEmail(event: React.SubmitEvent) {
    event.preventDefault();

    // if(oldEmail !== userProfile.email)
    //     
    //     else {
    //         setIsEmailWrong(false);
            const token = localStorage.getItem("token");
            try {
    const res = await fetch("http://localhost:8080/api/users/changeEmail", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify({
        
        oldEmail: oldEmail,
        newEmail: newEmail, 
      }),
    });

    if (res.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");}

    if (!res.ok) {
      setIsEmailWrong(true);
      const errorText = await res.text();
      console.error("Changing email FAILED:", res.status, errorText);
    //   alert("Changing email failed");
      return;
    }
    
     

    const data = await res.json();

    console.log("Change email RESPONSE:", data);

    alert("Email Changed successfully!");
      window.location.reload();
    
    // router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);
    // alert("fetch failed");
  }

  }

        
    

 async function handleSubmitPassword(event: React.SubmitEvent) {
    event.preventDefault();

    // if(oldPassword !== userProfile.password)
    //     
    //     else {
    //         setIsPasswordWrong(false);
            const token = localStorage.getItem("token");
            try {
    const res = await fetch("http://localhost:8080/api/users/changePassword", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify({
        oldPassword: oldPassword,
        newPassword: newPassword, }),
    });

    if (res.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");}

    if (!res.ok) {
  
      setIsPasswordWrong(true);
      const errorText = await res.text();
      // console.error("Changing password FAILED:", res.status, errorText);
      console.log(errorText || "Changing password failed");
    //   alert("Changing password failed");
      return;
    }



    const data = await res.json();

    console.log("Change password RESPONSE:", data);
    alert("Password Changed successfully!");
     window.location.reload();
    
    // router.push("/foods");
  } catch (err) {
    console.error("ERROR:", err);
    // alert("fetch failed");
  }

  }

  async function handleDeleteUser(userId: number) {
  if (!confirm("Delete this user?")) return;

  const token = localStorage.getItem("token");

  const res = await fetch(`http://localhost:8080/api/users/${userId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    alert("Delete failed");
    return;
  }

  setUsers((current) => current.filter((user) => user.id !== userId));
}

        

  return (
    
    <ProtectedPage>
      {profile?.role === "ADMIN" ? 
      (
  <div>
    <div className="flex justify-between items-center mb-6">

      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, {profile?.username}</h3>
      <button onClick={handleLogOut} className=" py-1 px-2 bg-red-500 hover:bg-red-600 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="button">Log out
        
        </button>
      </div>
        <table className="w-full border">
  <thead>
    <tr>
      <th className="border p-2">Username</th>
      <th className="border p-2">Email</th>
      <th className="border p-2">Role</th>
      <th className="border p-2">Action</th>
    </tr>
  </thead>
  <tbody>
    {users.map((user) => (
      <tr key={user.id}>
        <td className="border p-2">{user.username}</td>
        <td className="border p-2">{user.email}</td>
        <td className="border p-2">{user.role}</td>
        <td className="border p-2">
          {user.role === "USER" && 
          <button
            type="button"
            onClick={() => handleDeleteUser(user.id)}
            className="rounded bg-red-600 px-3 py-1 text-white"
          >
            Delete
          </button>}
        </td>
      </tr>
    ))}
  </tbody>
</table>
      

    </div>
) 
: (
    <div>

      <main className="max-w-5xl mx-auto p-6 text-black">
    <div className="flex justify-between items-center mb-6">

      <h3 className="text-4xl font-bold text-gray-800 mb-6">Hello, {profile?.username}</h3>
      <button onClick={handleLogOut} className=" py-1 px-2 bg-red-500 hover:bg-red-600 text-white font-medium rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition duration-150 ease-in-out" 
        type="button">Log out
        
        </button>
      </div>

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
        // pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
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
        // pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
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
    )}
    </ProtectedPage>
  );

 
}
