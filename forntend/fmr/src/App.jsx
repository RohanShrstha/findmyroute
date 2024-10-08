import './App.css'
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import BusStopFinder from "./components/BusStopFinder.jsx";

function App() {

  return (
      <Router>
          <Routes>
              <Route path="/" element={<BusStopFinder />} />
          </Routes>
      </Router>

  )
}

export default App
