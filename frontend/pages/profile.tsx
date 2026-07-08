
import { useEffect, useState } from "react";
import ProtectedPage from "@/components/ProtectedPage";
import { useRouter } from "next/router";


export default function Profile() {

  type UserProfile = {
  id: number;
  username: string;
  email: string;
  role: "USER" | "ADMIN";
};


  const [profile, setProfile] = useState<UserProfile | null>(null);
const [users, setUsers] = useState<UserProfile[]>([]);

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [oldEmail, setOldEmail] = useState("");
  const [newEmail, setNewEmail] = useState("");

 const router = useRouter();
 const [emailMessage, setEmailMessage] = useState("");
 const [passwordMessage, setPasswordMessage] = useState("");
 const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

     function handleLogOut(){
        localStorage.removeItem("token");
        router.replace("/Signin");

     }

        async function fetchUser() {
           try {

            const token = localStorage.getItem("token");
            
             const userDataResponse = await fetch(`${API_BASE_URL}/api/users`, {
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

           return;
         }
     
         const userTableData = await userDataResponse.json();
         
         setUsers(userTableData);
         console.log("User data Fetch response: ", userTableData);
     

           } catch (err) {
             console.error("ERROR:", err);

           }
         }

      useEffect(() => {

         async function fetchUserData() {
           try {

            const token = localStorage.getItem("token");
            
             const userDataResponse = await fetch(`${API_BASE_URL}/api/users/profile`, {
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

           return;
         }

         
     
     
     
         const userData = await userDataResponse.json();
         
         setProfile(userData);
         console.log("User data Fetch response: ", userData);
     if (userData.role === "ADMIN") {
  fetchUser();
}
           } catch (err) {
             console.error("ERROR:", err);

           }
         }
         fetchUserData();
       }, []);



    
         
       


  async function handleSubmitEmail(event: React.SubmitEvent) {
    event.preventDefault();

            const token = localStorage.getItem("token");
            try {
    const res = await fetch(`${API_BASE_URL}/api/users/changeEmail`, {
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

  if(res.status === 400) {
      setEmailMessage("The current email you entered is incorrect.")
    }

    if(res.status === 409) {
      setEmailMessage("New email cannot be the same as your current email.")
    }

 

    if (!res.ok) {
      const errorText = await res.text();
      console.error("Changing email FAILED:", res.status, errorText);

      return;
    }

    alert("Email Changed successfully!");
      window.location.reload();
    
  } catch (err) {
    console.error("ERROR:", err);

  }

  }

        
    

 async function handleSubmitPassword(event: React.SubmitEvent) {
    event.preventDefault();

            const token = localStorage.getItem("token");
            try {
    const res = await fetch(`${API_BASE_URL}/api/users/changePassword`, {
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

     if(res.status === 400) {
      setPasswordMessage("The current password you entered is incorrect.")
    }

    if(res.status === 409) {
      setPasswordMessage("New password cannot be the same as your current password.")
    }

   

    if (!res.ok) {
  
      const errorText = await res.text();

      console.log(errorText || "Changing password failed");

      return;
    }

    alert("Password Changed successfully!");
     window.location.reload();
    
  } catch (err) {
    console.error("ERROR:", err);

  }

  }

  async function handleDeleteUser(userId: number) {
  if (!confirm("Delete this user?")) return;

  const token = localStorage.getItem("token");

  const res = await fetch(`${API_BASE_URL}/api/users/${userId}`, {
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
        onChange={(event) => setOldEmail(event.target.value)}
        placeholder="Old email"
        required
        type="email"
        title="Please enter valid email!" 
      />
      <label className="block text-sm/6 font-medium text-black">Your new email:</label>
        <input className="w-full px-4 py-2  border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"
        value={newEmail}
        onChange={(event) => setNewEmail(event.target.value)}
        placeholder="New email"
        required
        type="email"
        title="Please enter valid email!" 
      />
      
      </div>

      {emailMessage && <p className="text-lg text-red-700">{emailMessage}</p>}
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
        onChange={(event) => setNewPassword(event.target.value)}
        placeholder="New password"
        required
        pattern=".{8,}" 
        title="Password must be at least 8 characters!" 
      />
      {passwordMessage && <p className="text-lg text-red-700">{passwordMessage}</p>}
      </div>

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
