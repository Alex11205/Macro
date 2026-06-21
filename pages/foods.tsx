import Link from "next/link";
import Header from "@/components/header";
import Card from "@/components/card";
import { useEffect, useState } from "react";
import ProtectedPage from "@/components/ProtectedPage";

// type CardData = {
//   id: string;
//   title: string;
//   continent: string;
//   image: string;
// };

type CardData = {
  id: bigint;
  name: string;
  carb: number;
  protein: number;
  fat: number;
  calorie: number;
  // weight: number;
  imageUrl?: string | null;
};





export default function Foods() {
  // localStorage.setItem("token", "");
  // const token = localStorage.getItem("token");
  // alert("The token is: " + token);
  // alert("token is: " + localStorage.getItem("token"));
  
  // const [foodList, setFoodList] =useState<FoodCardData[]>([]);
  const [savedItems, setSavedItems] = useState<CardData[]>([]);
  const [foodList, setFoodList] = useState<CardData[]>([]);
  useEffect(() => {
    // const stored = JSON.parse(localStorage.getItem("savedCards") || "[]");
    // setSavedItems(stored);
    async function fetchFoodData() {
      try {
        const allFoodResponse = await fetch("http://localhost:8080/api/foods", {
          method: "GET",
          headers: {
        "Content-Type": "application/json",
      },
        });

        const token = localStorage.getItem("token");
        const userFoodResponse = await fetch("http://localhost:8080/api/favorites/favoriteList", {
          method: "GET",
          headers: {
        "Content-Type": "application/json",
        "Authorization": token,
      },
        });

        if (!allFoodResponse.ok) {
      const allFoodErrorText = await allFoodResponse.text();
      console.error("All food FETCH FAILED:", allFoodResponse.status, allFoodErrorText);
      alert("Fetching food list failed");
      return;
    }

    if (!userFoodResponse.ok) {
      const userFoodErrorText = await userFoodResponse.text();
      console.error("User food FETCH FAILED:", userFoodResponse.status, userFoodErrorText);
      alert("Fetching user food failed");
      return;
    }

    const allFoodData = await allFoodResponse.json();
    setFoodList(allFoodData);
    console.log("All food Fetch response: ", allFoodData);

    const userFoodData = await userFoodResponse.json();
    setSavedItems(userFoodData);
    console.log("User food Fetch response: ", userFoodData);

    // alert("Food fetch successfully!");
      } catch (err) {
        console.error("ERROR:", err);
    alert("fetch failed");
      }
    }
    fetchFoodData();
  }, []);

   async function toggleSave(card: CardData) {
    const isSaved = savedItems.some((item) => item.id === card.id);
    const userId = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/api/favorites/${userId}/favorites/${card.id}`, {
        method: isSaved ? "DELETE" : "POST",
        headers: {
          "Content-Type": "application/json",
        },

        

      });
      console.log(card);
      alert(`User id is: ${userId}. FoodId is ${card.id}. food name is ${card.name}. imageUrl is: ${card.imageUrl}`);
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
      

      {/* Navigation */}
      {/* < Header/> */}
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
            userId={localStorage.getItem("token")}
            onToggleSave={toggleSave}
            hasButton={false}
            // isWeightReadOnly={true}
            // name={card.title}
            // continent={card.continent}
            // image={card.image}
            // onSave={() => handleSave(card)}
            
            
          />
          
        ))}
      </div>

      </main>
    </div>
    </ProtectedPage>
  );
}