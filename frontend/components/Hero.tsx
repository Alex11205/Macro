
type HeroProps = {
  title: string;
  subtitle: string;
  backgroundImage: string;
};

export default function Hero({ title, subtitle, backgroundImage }: HeroProps) {
  return (
    <div
      className="min-h-screen bg-cover bg-center flex items-center justify-center text-white"
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <div className="bg-green-600 bg-opacity-0 p-6 rounded mb-40">
        <h1 className="text-3xl md:text-5xl font-bold">{title}</h1>
        <p className="mt-4 text-lg">{subtitle}</p>
      
      </div>
      
      
    </div>
  );
}