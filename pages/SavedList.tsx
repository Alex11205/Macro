import { useEffect, useState } from "react";
import Link from "next/link";
import Card from "@/components/card";
import ProtectedPage from "@/components/ProtectedPage";
import { useRouter } from "next/router";

type CardData = {
  id: bigint;
  name: string;
  carb: number;
  protein: number;
  fat: number;
  calorie: number;
  // weight: number;
  mageUrl?: string | null;
};

export default function SavedPage() {
  const [savedItems, setSavedItems] = useState<CardData[]>([]);
  const [trackedItems, setTrackedItems] = useState<CardData[]>([]);
  const [weights, setWeights] = useState<Record<string, string>>({});
  const router = useRouter();
  useEffect(() => {
    async function fetchFoodData() {
      try {
        const token = localStorage.getItem("token");
        const userFoodResponse = await fetch("http://localhost:8080/api/favorites/favoriteList", {
          method: "GET",
          headers: {
        "Content-Type": "application/json",
        "Authorization": token,
      },
        });


    if (!userFoodResponse.ok) {
      const userFoodErrorText = await userFoodResponse.text();
      console.error("User food FETCH FAILED:", userFoodResponse.status, userFoodErrorText);
      // alert("Fetching user food failed");
      return;
    }

    if (userFoodResponse.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");}
      // console.log(card);

    const userFoodData = await userFoodResponse.json();
    setSavedItems(userFoodData);
    console.log("User food Fetch response: ", userFoodData);
    // console.log("Saved cards are: " + localStorage.getItem("savedCards"));
    // alert("Food fetch successfully!");
      } catch (err) {
        console.error("ERROR:", err);
    // alert("fetch failed");
      }
    }
    fetchFoodData();
    // const stored = JSON.parse(localStorage.getItem("savedCards") || "[]");
    // setSavedItems(stored);
  }, []);

  function idKey(id: bigint) {
  return id.toString();
}

function toggleTrack(card: CardData, checked: boolean) {
  const key = idKey(card.id);

  if (checked) {
    setTrackedItems((current) =>
      current.some((item) => idKey(item.id) === key)
        ? current
        : [...current, card]
    );

    setWeights((current) => ({
      ...current,
      [key]: current[key] ?? "100",
    }));
  } else {
    setTrackedItems((current) =>
      current.filter((item) => idKey(item.id) !== key)
    );

    setWeights((current) => {
      const next = { ...current };
      delete next[key];
      return next;
    });
  }
}

function updateWeight(id: bigint, value: string) {
  setWeights((current) => ({
    ...current,
    [idKey(id)]: value,
  }));
}

function getWeight(id: bigint) {
  const value = Number(weights[idKey(id)] ?? "100");
  return Number.isFinite(value) ? value : 0;
}

function calculated(value: number, id: bigint) {
  return (value * getWeight(id)) / 100;
}

function format(value: number) {
  return value.toFixed(1);
}

const totals = trackedItems.reduce(
  (sum, item) => {
    sum.carb += calculated(item.carb, item.id);
    sum.protein += calculated(item.protein, item.id);
    sum.fat += calculated(item.fat, item.id);
    sum.calorie += calculated(item.calorie, item.id);
    return sum;
  },
  { carb: 0, protein: 0, fat: 0, calorie: 0 }
);


  async function toggleSave(card: CardData) {

    const isSaved = savedItems.some((item) => item.id === card.id);
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/api/favorites/favorites/${card.id}`, {
        method: isSaved ? "DELETE" : "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": token,
        },

        

      });


      // alert(`User id is: ${userId}. FoodId is ${card.id}. issaved is ${isSaved}`);
      if (!res.ok) {
        throw new Error("Request failed");
      }

      if (res.status === 401) {
  localStorage.removeItem("token");
  router.replace("/Signin");
      console.log(card);}

    } catch (error) {
      console.error(error);
      // alert("Something went wrong. Please try again.");
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
    

    // const updated = savedItems.filter((item) => item.id !== card.id);
    // setSavedItems(updated);
    // localStorage.setItem("savedCards", JSON.stringify(updated));
  };

  return (
    <ProtectedPage>
    <main className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Saved Foods</h1>
        <Link
          href="/foods"
          className="text-blue-600 hover:underline"
        >
          Back to Full List
        </Link>
      </div>

      {savedItems.length === 0 ? (
        <p>No saved food yet.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {savedItems.map((card) => (
            <Card
              key={idKey(card.id)}
              {...card}
              isSaved={true}
              isTracked={trackedItems.some((item) => idKey(item.id) === idKey(card.id))}
              onToggleSave={toggleSave}
              onToggleTrack={toggleTrack}
              hasButton={true}
              // isWeightReadOnly={false}
              // isTracked

            />
          ))}
        </div>
        

        
      )}

      {trackedItems.length > 0 && (
  <section className="mt-8">
    <h2 className="text-xl font-bold mb-4">Tracked Foods</h2>

    <div className="overflow-x-auto">
      <table className="w-full border border-gray-300 text-left">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 border">Name</th>
            <th className="p-2 border">Weight</th>
            <th className="p-2 border">Carbs</th>
            <th className="p-2 border">Protein</th>
            <th className="p-2 border">Fat</th>
            <th className="p-2 border">Calories</th>
          </tr>
        </thead>

        <tbody>
          {trackedItems.map((food) => {
            const key = idKey(food.id);

            return (
              <tr key={key}>
                <td className="p-2 border">{food.name}</td>
                <td className="p-2 border">
                  <input
                    type="number"
                    min="0"
                    value={weights[key] ?? "100"}
                    onChange={(e) => updateWeight(food.id, e.target.value)}
                    className="w-24 border rounded px-2 py-1"
                  />
                  &nbsp;g
                </td>
                <td className="p-2 border">{format(calculated(food.carb, food.id))}g</td>
                <td className="p-2 border">{format(calculated(food.protein, food.id))}g</td>
                <td className="p-2 border">{format(calculated(food.fat, food.id))}g</td>
                <td className="p-2 border">{format(calculated(food.calorie, food.id))} kcal</td>
              </tr>
            );
          })}

          <tr className="font-bold bg-gray-100">
            <td className="p-2 border">Total</td>
            <td className="p-2 border" />
            <td className="p-2 border">{format(totals.carb)}g</td>
            <td className="p-2 border">{format(totals.protein)}g</td>
            <td className="p-2 border">{format(totals.fat)}g</td>
            <td className="p-2 border">{format(totals.calorie)} kcal</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
)}
    </main>
    </ProtectedPage>
  );
}