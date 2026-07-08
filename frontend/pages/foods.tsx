import Link from "next/link";
import Card from "@/components/card";
import { useEffect, useState } from "react";
import ProtectedPage from "@/components/ProtectedPage";
import { useRouter } from "next/router";


type CardData = {
  id: bigint;
  name: string;
  carb: number;
  protein: number;
  fat: number;
  calorie: number;

  imageUrl?: string | null;
};





export default function Foods() {

  const [savedItems, setSavedItems] = useState<CardData[]>([]);
  const [foodList, setFoodList] = useState<CardData[]>([]);
   const router = useRouter();
   const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";
  useEffect(() => {

    async function fetchFoodData() {
      const token = localStorage.getItem("token");
      try {
        const allFoodResponse = await fetch(`${API_BASE_URL}/api/foods`, {
          method: "GET",
          headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
        });

        
        const userFoodResponse = await fetch(`${API_BASE_URL}/api/favorites/favoriteList`, {
          method: "GET",
          headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
        });

        if (userFoodResponse.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");
}

        if (!allFoodResponse.ok) {
      const allFoodErrorText = await allFoodResponse.text();
      console.error("All food FETCH FAILED:", allFoodResponse.status, allFoodErrorText);

      return;
    }

    if (!userFoodResponse.ok) {
      const userFoodErrorText = await userFoodResponse.text();
      console.error("User food FETCH FAILED:", userFoodResponse.status, userFoodErrorText);

      return;
    }

    const allFoodData = await allFoodResponse.json();
    setFoodList(allFoodData);
    console.log("All food Fetch response: ", allFoodData);

    const userFoodData = await userFoodResponse.json();
    setSavedItems(userFoodData);
    console.log("User food Fetch response: ", userFoodData);

      } catch (err) {
        console.error("ERROR:", err);

      }
    }
    fetchFoodData();
  }, []);

   async function toggleSave(card: CardData) {
    const isSaved = savedItems.some((item) => item.id === card.id);
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`${API_BASE_URL}/api/favorites/favorites/${card.id}`, {
        method: isSaved ? "DELETE" : "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },

        
        

      });
      if (res.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");
      console.log(card);
      }

      if (!res.ok) {
        throw new Error("Request failed");
      }

    } catch (error) {
      console.error(error);
      alert("Something went wrong. Please try again.");
    }

    

    const alreadySaved = savedItems.some((item) => item.id === card.id);

    let updated: CardData[];

    if (alreadySaved) {
      updated = savedItems.filter((item) => item.id !== card.id);
    } else {
      updated = [...savedItems, card];
    }

    setSavedItems(updated);
    localStorage.setItem("savedCards", JSON.stringify(updated));
  };

  return (
    <ProtectedPage>
    <div className="min-h-screen bg-gray-100">
      
        <main className="max-w-5xl mx-auto p-6 text-black">
      <h2 className="text-xl font-semibold mt-4">Full Food List</h2><br></br>
      <Link href="./postNewFood"><p className="bg-orange-300 rounded text-sm px-2 py-1 font-medium border inline-block text-grey hover:bg-green-700 hover:text-white">
        Create your own food</p></Link>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        
        {foodList.map((card) => (
            
          <Card
            key={card.id}
            {...card}
            isSaved={savedItems.some((item) => item.id === card.id)}
            onToggleSave={toggleSave}
            hasButton={false}

            
            
          />
          
        ))}
      </div>

      </main>
    </div>
    </ProtectedPage>
  );
}