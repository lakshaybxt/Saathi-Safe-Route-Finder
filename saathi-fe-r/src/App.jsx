import { useState } from 'react'
import Navbar from './components/Navbar/Navbar'
import Trust from './components/Trust/Trust'
import Place from './components/Place/Place'
import Footer from './components/Foooter/Footer'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div className='main-content'>
        <Trust/>
        <Navbar/>
        <Place/>
      </div>
      <Footer/>
    </>
  )
}

export default App
