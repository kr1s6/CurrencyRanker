import axios from 'axios';
import { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [currencies, setCurrencies] = useState<CurrencyData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const tableHeaders: string[] = [
    "Rank",
    "Countries",
    "Currency",
    "Code",
    "Turnover (%)",
    "Inflation",
    "Kurs (1 PLN)"
  ];

  interface CurrencyData {
    currencyName: string;
    currencyCode: string;
    rate: number;
    turnover: number;
    rank: number;
    countries: string[];
    inflation: string;
    inflationUpdate: string;
  }

  useEffect(() => {
    const fetchCurrencyData = async () => {
      try {
        setLoading(true);
        const response = await axios.get<CurrencyData[]>('http://localhost:8080/api/v1/getAllCurrencyData');
        console.log(response.data);

        const currenciesArray = response.data
          .map(key => ({
            currencyName: key.currencyName,
            currencyCode: key.currencyCode,
            rate: key.rate,
            turnover: key.turnover,
            rank: key.rank,
            countries: key.countries,
            inflation: key.inflation,
            inflationUpdate: key.inflationUpdate
          }));

        setCurrencies(currenciesArray);
        setError(null);
      } catch (err) {
        setError('Nie udało się pobrać kursów walut');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchCurrencyData();
  }, []);

  if (loading) return <div className="p-8 text-center">Loading...</div>;
  if (error) return <div className="p-8 text-center text-red-500">{error}</div>;


  return (
    <>
      <h1 className='mb-8'>
        Top Currency Ranking
      </h1>

      <table className="min-w-full border-collapse border border-slate-300 shadow-lg rounded-lg overflow-hidden">
        <thead className="bg-linear-to-r from-indigo-500 to-purple-600 text-white">
          <tr>
            {tableHeaders.map(header => (
              <th key={header} className="px-6 py-4 text-center text-sm font-semibold uppercase tracking-wider">
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-slate-200">
          {currencies.map((key, index) => (
            <tr key={key.currencyName} className={`hover:bg-indigo-50 transition-colors duration-200 ${index % 2 === 0 ? 'bg-slate-50' : 'bg-white'}`}>

              <td className="py-4 text-sm font-bold text-indigo-600">
                #{key.rank}
              </td>

              {key.countries.length <= 3 ? (
                <td className="py-4 text-xs font-bold text-slate-700">
                  {key.countries.map((key, index) =>
                    <span key={index} className="block text-gray-600 italic">{key}</span>
                  )}
                </td>
              ) : (
                <td className="py-4 text-xs font-bold text-slate-700 relative group cursor-pointer">
                  <span className="underline">({key.countries.length}) Countries</span>

                  <div className="absolute z-13 hidden group-hover:block bg-white border border-gray-200 rounded shadow-lg p-2 min-w-max top-full left-0">
                    {key.countries.map((country, index) => (
                      <span key={index} className="block text-gray-600 italic py-0.5">{country}</span>
                    ))}
                  </div>
                </td>
              )}

              <td className="py-4 text-xs font-bold text-slate-700">
                {key.currencyName}
              </td>

              <td className="px-4 py-4 text-sm font-bold text-slate-900">
                {key.currencyCode}
              </td>

              <td className="px-6 py-4 text-sm text-slate-600">
                {key.turnover >= 0 ? (
                  <div className="flex items-center gap-2">
                    <div className="w-15 bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-indigo-600 h-2 rounded-full"
                        style={{ width: `${Math.min(key.turnover, 100)}%` }}
                      />
                    </div>
                    <span>{key.turnover.toFixed(1)}%</span>
                  </div>
                ) : (
                  <span className="text-gray-400 italic">UNKNOWN</span>
                )}
              </td>

              <td className="px-2 py-4 text-sm text-slate-600">
                {key.inflation}
              </td>

              <td className="px-2 py-4 text-sm text-slate-600">
                {key.rate.toFixed(4)}
              </td>
              
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}

export default App
