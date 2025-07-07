import { useState } from 'react'
import Navbar from './components/Navbar/Navbar'
import About from './components/About/About'
import Place from './components/Place/Place'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Navbar/>
      {/* <About/> */}
      <Place/>
    </>
  )
}

export default App
