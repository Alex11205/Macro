import { useState } from "react";
import Header from "./A_Header";
import SavingList from "./A_SavingList";

export default function Signin() {
   const [input, setInput] = useState(" ");
  const [username, setUsername] = useState("Guest");

   const [message, setMessage] = useState('');
     const [isSubmit, setIsSubmit] = useState(false);

  const handleSubmit = (event: React.SubmitEvent) => {
    event.preventDefault();
    setUsername(input);
    setIsSubmit(true);
    setMessage('Enjoy your journey!');
  };

  return (
    <div>
      < Header username={username} />
      {!isSubmit && <form onSubmit={handleSubmit}>
        <h3>Enter your username</h3>
        <input
        // Current value is always the state
        value={input}
        // Detect changes in input and update the state
        onSubmit={(event) => setUsername(event.target.value)}
        onChange={(event) => setInput(event.target.value)}
        placeholder="Please enter your username"
      />
        <button type="submit">Sign in</button>
      </form>}
      {message && <p>{message}</p>}
      
      {isSubmit && < SavingList />}
      
      
    </div>
  );

 
}
