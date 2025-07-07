import logo from '../../assets/saathi-logo.png';
import './navbar.css';

function Navbar() {
  return (
    <nav className='navbar'>
      <a href="/">
        <img src={logo} alt='logo' className='saathi-logo' />
      </a>  

			<div className='nav-list'>
				<a href='#'>
					<div >Home</div>
				</a>
				<a href='#'>
					<div >Find Route</div>
				</a>
				<a href='#'>
					<div >Report Area</div>
				</a>
				<a href='#'>
					<div >Login</div>
				</a>
			</div>
    </nav>
  );  
}

export default Navbar;
