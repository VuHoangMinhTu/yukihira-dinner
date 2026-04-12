import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Menu from './component/Menu'
import Navbar from './component/Navbar'
import Cart from './page/Cart'
import { Route, Routes } from 'react-router-dom'
function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Routes>
        <Route element={<Navbar />}>
          <Route path="/" element={<Menu />} />
          <Route path="/cart" element={<Cart />} />
        </Route>
      </Routes>

    </>
  )
}

export default App
