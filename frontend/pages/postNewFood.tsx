
import Link from "next/link";
import { ChangeEvent, FormEvent, useState } from "react";

type FoodFormData = {
  name: string;
  carb: string;
  protein: string;
  fat: string
  calorie: string;
  createdBy: string;
};


type AddFoodFormProps = {
  apiUrl?: string;
  onCreated?: (food: unknown) => void;
};

const initialFormData: FoodFormData = {
  name: "",
  carb: "",
  protein: "",
  fat: "",
  calorie: "",
  createdBy: "",
};

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export default function AddFoodForm({
  
  apiUrl = `${API_BASE_URL}/api/foods`,
  onCreated,
}: AddFoodFormProps) {
  const [formData, setFormData] = useState<FoodFormData>(initialFormData);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [message, setMessage] = useState("");

  function handleChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;

    setFormData((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage("");

    const payload = {
      name: formData.name.trim(),
      carb: Number(formData.carb),
      protein: Number(formData.protein),
      fat: Number(formData.fat),
      calorie: Number(formData.calorie),
      createdBy: "",
    };

    if (!payload.name) {
      setMessage("Please enter a food name.");
      return;
    }

    if (
      Number.isNaN(payload.carb) ||
      Number.isNaN(payload.protein) ||
      Number.isNaN(payload.fat) ||
      Number.isNaN(payload.calorie)
    ) {
      setMessage("Carb, protein, fat and calorie must be valid numbers.");
      return;
    }

    try {
      setIsSubmitting(true);

      const token = localStorage.getItem("token");
      const headers: HeadersInit = {
        "Content-Type": "application/json",
      };

      if (token) {
        headers.Authorization =  `Bearer ${token}`;
      }

      const response = await fetch(apiUrl, {
        method: "POST",
        headers,
        body: JSON.stringify(payload),
      });

      if(response.status === 409) {
        setMessage("Food already exists!");
      }
      
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Food creation failed.");
      }

      const createdFood = await response.json().catch(() => null);
      onCreated?.(createdFood);

      setFormData(initialFormData);
      setMessage("Food added successfully!");
    } catch (error) {
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="p-6">
        <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-semibold mt-4">Your custom food</h2>
        <Link
          href="/foods"
          className="text-blue-600 hover:underline"
        >
          Back to Full List
        </Link>
        </div>
    <form onSubmit={handleSubmit} className="max-w-md space-y-4">
        
      <div>
        <label className="block text-sm font-medium text-gray-700">
          Name
        </label>
        <input
          name="name"
          type="text"
          value={formData.name}
          onChange={handleChange}
          required
          className="mt-1 w-full rounded border border-gray-300 px-3 py-1"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700">
          Carb
        </label>
        <input
          name="carb"
          type="number"
          min="0"
          step="0.01"
          value={formData.carb}
          onChange={handleChange}
          required
          className="mt-1 w-full rounded border border-gray-300 px-3 py-1"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700">
          Protein
        </label>
        <input
          name="protein"
          type="number"
          min="0"
          step="0.01"
          value={formData.protein}
          onChange={handleChange}
          required
          className="mt-1 w-full rounded border border-gray-300 px-3 py-1"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700">
          Fat
        </label>
        <input
          name="fat"
          type="number"
          min="0"
          step="0.01"
          value={formData.fat}
          onChange={handleChange}
          required
          className="mt-1 w-full rounded border border-gray-300 px-3 py-1"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700">
          Calorie
        </label>
        <input
          name="calorie"
          type="number"
          min="0"
          step="0.01"
          value={formData.calorie}
          onChange={handleChange}
          required
          className="mt-1 w-full rounded border border-gray-300 px-3 py-1"
        />
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="rounded bg-blue-600 px-4 py-2 font-medium text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-blue-300"
      >
        {isSubmitting ? "Submitting..." : "Add Food"}
      </button>

      {message && <p className="text-lg text-red-700">{message}</p>}
    </form>
    </main>
  );
}
