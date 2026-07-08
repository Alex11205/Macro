
type CardData = {
  id: bigint;
  name: string;
  carb: number;
  protein: number;
  fat: number;
  calorie: number;
  imageUrl?: string | null;
};

type CardProps = CardData & {
  isSaved: boolean;
  isTracked?: boolean;
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
  hasButton,
  imageUrl, 
  isSaved,
  isTracked,
  onToggleSave, 
  onToggleTrack,
}: CardProps) {

    const card = { id, name, carb, protein, fat, calorie, imageUrl };


  return (
    <div className="relative bg-white text-gray-800 rounded-xl shadow-md overflow-hidden p-4 m-2 w-64">

        <img  
          src={ imageUrl ? imageUrl : "/images/food.jpg"  }
          alt={name}
          className="w-full h-40 object-cover"
        >
        </img>

      <div className="p-4">
      <p className="text-gray-600">Per 100g</p>
      <h2 className="text-lg font-semibold mb-2">{name}</h2>
      <p className="text-gray-600">Carbs: {carb}g</p>
      <p className="text-gray-600">Protein: {protein}g</p>
      <p className="text-gray-600">Fat: {fat}g</p>
      <p className="text-gray-600">Calories: {calorie} kcal</p>
    
      {hasButton && 
      <label className="flex items-center gap-2 mt-4 text-green-500">
      <input 
      type="checkbox"
  
        checked={isTracked}
        onChange={(e) => onToggleTrack?.(card, e.target.checked)}

      />
      Track this food
      </label>
      }
      <br></br>
     
      <button
        type="button"
        onClick={() => onToggleSave({ id, name, carb, protein, fat, calorie, imageUrl })}

        className={`px-3 py-1 rounded text-sm font-medium transition mt-4 ${
          isSaved
            ? "bg-green-600 text-white hover:bg-green-700"
            : "bg-blue-500 text-white hover:bg-blue-600"
        }`}
      >
        {isSaved ? "Remove" : "Save"}
      </button>

      </div>
      
    </div>
  );
}