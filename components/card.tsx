import Link from "next/link";
import { useState } from "react";

// type CardData = {
//   id: string
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

type CardProps = CardData & {
  isSaved: boolean;
  isTracked?: boolean;
  // isWeightReadOnly: boolean;
  hasButton: boolean;
  onToggleSave: (card: CardData) => void;
  onToggleTrack?: (card: CardData, checked: boolean) => void;
};




export default function Card({ 
  id, 
  name, 
  carb,
  protein,
  fat,
  calorie,
  // weight,
  hasButton,
  imageUrl, 
  isSaved,
  isTracked,
  // isWeightReadOnly,
  onToggleSave, 
  onToggleTrack,
}: CardProps) {
    // const [isChecked, setIsChecked] = useState(false);
    const card = { id, name, carb, protein, fat, calorie, imageUrl };
    // const handleChange = (e) => {
      // setIsChecked(e.target.checked);
      // localStorage.setItem true;
    // }
  // const [weight, setWeight] = useState(0);
  // async function handleClick() {
  //   try {
  //     const res = await fetch(`http://localhost:8080/api/favorites/${userId}/favorites/${id}`, {
  //       method: isSaved ? "DELETE" : "POST",
  //       headers: {
  //         "Content-Type": "application/json",
  //       },

        

  //     });
  //     alert(`User id is: ${userId}. FoodId is ${id}. issaved is ${isSaved}`);
  //     if (!res.ok) {
  //       throw new Error("Request failed");
  //     }

  //   } catch (error) {
  //     console.error(error);
  //     alert("Something went wrong. Please try again.");
  //   }
  // }

  return (
    <div className="relative bg-white text-gray-800 rounded-xl shadow-md overflow-hidden p-4 m-2 w-64">
      
      

      {/* <Link href={`/destinationpages/${id}`}> */}
        <img  
          src={ imageUrl ? imageUrl : "/images/food.jpg"  }
          alt={name}
          className="w-full h-40 object-cover"
        >
        </img>
      {/* </Link> */}

      <div className="p-4">
      <p className="text-gray-600">Per 100g</p>
      <h2 className="text-lg font-semibold mb-2">{name}</h2>
      <p className="text-gray-600">Carbs: {carb}g</p>
      <p className="text-gray-600">Protein: {protein}g</p>
      <p className="text-gray-600">Fat: {fat}g</p>
      <p className="text-gray-600">Calories: {calorie} kcal</p>
    
      {hasButton && 
      // <button
      //   type="button"
      //   onClick={() => onToggleSave({ id, name, carb, protein, fat, calorie, image_url })}
      //   // onClick={handleClick}
      //   className={`px-3 py-1 rounded text-sm font-medium transition ${
      //     isSaved
      //       ? "bg-green-600 text-white hover:bg-green-700"
      //       : "bg-blue-500 text-white hover:bg-blue-600"
      //   }`}
      // >
      //   Track
      // </button>
      <label className="flex items-center gap-2 mt-4 text-green-500">
      <input 
      type="checkbox"
      // className="w-full px-4 py-2 border border-black-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150 ease-in-out"
        // Current value is always the state
        checked={isTracked}
        onChange={(e) => onToggleTrack?.(card, e.target.checked)}
        // onChange={handleChange}
        // Detect changes in input and update the state
        // onSubmit={(event) => setUsername(event.target.value)}
        // onChange={(event) => setWeight(event.target.value)}
        // placeholder="Please enter the weight"
        
        // pattern=".{6,}" 
        // title="User name must be at least 6 characters!" 
      />
      Track this food
      </label>
      }
      <br></br>
     
      <button
        type="button"
        onClick={() => onToggleSave({ id, name, carb, protein, fat, calorie, imageUrl })}
        // onClick={handleClick}
        className={`px-3 py-1 rounded text-sm font-medium transition mt-4 ${
          isSaved
            ? "bg-green-600 text-white hover:bg-green-700"
            : "bg-blue-500 text-white hover:bg-blue-600"
        }`}
      >
        {isSaved ? "Remove" : "Save"}
      </button>
      {/* <Link href={`/destinationpages/${id}`}>
      <p className="rounded  text-sm px-2 py-1 font-medium border inline-block text-grey hover:bg-green-700 hover:text-white">Discover</p>
      </Link> */}
      </div>
      
    </div>
  );
}