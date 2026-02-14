import { useState } from "react";


function ListItem({ item, removeItem }) {
//   const [isEditing, setIsEditing] = useState(false);
//   const [itemToUpdate, setItemToUpdate] = useState({item.name, item.calorie, item.protein, item.fat, item.carb});

//   function handleEdit() {
//     setIsEditing(true);
//   }

//   function handleSave() {
//     updateItem(item.id, itemToUpdate.name, itemToUpdate.calorie, itemToUpdate.protein, itemToUpdate.fat, itemToUpdate.carb);
//     setIsEditing(false);
//   }

  return (
    <li key={item.id}>
      {/* {isEditing ? (
        <input
          onChange={(event) => {
            setItemToUpdate(event.target.value);
          }}
          value={itemToUpdate}
        />
      ) : (
        item.name
      )} */
        
      }
      { item.name }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      { item.calorie }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      { item.protein }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      { item.fat}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      { item.carb }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        
      <button onClick={() => removeItem(item.id)}>Delete</button>
      {/* <button onClick={() => isEditing ? handleSave() : handleEdit()}>Edit</button> */}
      
    </li>
    
  );
}

 function showResult(result) {
    <li>
        {result}
        </li>

  }

   function ShowResult2() {
    <li>
        result
        </li>

  }

export default function SavingList() {
  const [items, setItems] = useState([
    { id: 1, name: "Name", calorie: "Calorie", protein: "Protein", fat: "Fat", carb: "Carb" },
    
  ]);
  const [nameToAdd, setNameToAdd] = useState("");
   const [calorieToAdd, setCalorieToAdd] = useState("");
    const [proteinToAdd, setProteinToAdd] = useState("");
    const [fatToAdd, setFatToAdd] = useState("");
    const [carbToAdd, setCarbToAdd] = useState("");
    const [nameToCal, setNameToCal] = useState("");
    const [weightToCal, setWeightToCal] = useState("");

    const [isCal, setIsCal] = useState(false);
    
    // const [calResult, setCalResult] = useState({id: 1, name: "Name", calorie: "Calorie", protein: "Protein", fat: "Fat", carb: "Carb"})
    const [calResult, setCalResult] = useState([]);
    
   


  const addItem = () => {
    const newItem = { id: items.length + 1, name: nameToAdd, calorie: calorieToAdd, protein: proteinToAdd, fat: fatToAdd, carb: carbToAdd };
    setItems(
      // Create a new array
      [
        // Copy everything from items
        ...items,
        // Add newItem to this new array
        newItem,
      ],
    );
  };


  const calculateMacro = (event) => {
    event.preventDefault();
    
    const res = items.find(
        p => p.name === nameToCal
    );
    // setCalResult(prev => [...prev, res]);
    alert(`The macro facts in your food:\n Food name: ${res.name}\n Weight: ${weightToCal}g\n Calorie: ${res.calorie*weightToCal/100}kcal\n Protein: ${res.protein*weightToCal/100}g\n Fat: ${res.fat*weightToCal/100}g\n Carb: ${res.carb*weightToCal/100}g`);
    setIsCal(true);
    
  };

 

  const removeItem = (id: number) => {
    setItems(items.filter((item) => item.id !== id));
  };

//   const updateItem = (id: number, newName: string, newCalorie, newProtein, newFat, newCarb) => {
//     setItems(
//       items.map((item) => (item.id === id ? { ...item, name: newName, calorie: newCalorie, protein: newProtein, fat: newFat, carb: newCarb } : item)),
//     );
//   };
  
//   const filteredItems = checked
//   ? items.filter((item) => item.name.startsWith("A"))
//   : items;
  // const filterItem = () => {
  //    setChecked(event.target.checked);
  //   if(checked)
  //   setItems());
  //   else setItems([...items]);
  // }



  return (
    <div>
      <h1>Food List</h1>
      <p>Macros per 100g</p>
      <ul>
        {items.map((item) => (
          <ListItem
            item={item}
            removeItem={removeItem}
            // updateItem={updateItem}
            // filterItem={filterItem}
          />
        ))}
      </ul>
      <h3 className="add">Add new food</h3>
      <input
        // Current value is always the state
        value={nameToAdd}
        // Detect changes in input and update the state
        onChange={(event) => setNameToAdd(event.target.value)}
        placeholder="Food name"
      />
      <input
        // Current value is always the state
        value={calorieToAdd}
        // Detect changes in input and update the state
        onChange={(event) => setCalorieToAdd(event.target.value)}
        placeholder="Calorie"
      />
      <input
        // Current value is always the state
        value={proteinToAdd}
        // Detect changes in input and update the state
        onChange={(event) => setProteinToAdd(event.target.value)}
        placeholder="Protein"
      />
      <input
        // Current value is always the state
        value={fatToAdd}
        // Detect changes in input and update the state
        onChange={(event) => setFatToAdd(event.target.value)}
        placeholder="Fat"
      />
      <input
        // Current value is always the state
        value={carbToAdd}
        // Detect changes in input and update the state
        onChange={(event) => setCarbToAdd(event.target.value)}
        placeholder="Carb"
      />
      <button onClick={addItem}>Add Food</button><br></br>
      {/* <input
        type="checkbox"
        checked={checked}
        onChange={(event) => setChecked(event.target.checked)
         
         
          // if(checked)
            }
      />
      Show Only Items Starting with 'A' */}

      <h3 className="cal">Macro Calculator</h3>
      <input
        // Current value is always the state
        value={nameToCal}
        // Detect changes in input and update the state
        onChange={(event) => setNameToCal(event.target.value)}
        placeholder="Name"
      />
      <input
        // Current value is always the state
        value={weightToCal}
        // Detect changes in input and update the state
        onChange={(event) => setWeightToCal(event.target.value)}
        placeholder="Weight"
      />gram
      <button onClick={calculateMacro}>Calcualte</button><br></br>
      
    {/* {isCal && <showResult result={calResult} /> */}
    {/* {isCal && 
  <li>{calResult}
      </li>
        } */}

    </div>
  );
}
