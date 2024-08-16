import "./header.css";
import logo from '../../assets/logo-head.png';

const Header = ({ onInputChange }) => {
    return (
        <>
            <div className="navbarBgDark sticky-top" data-bs-theme="dark">
                <nav className="navbar">
                    <div className="container-fluid">
                        <a className="navbar-brand">
                            <img src={logo} alt="Logo" width="140px"/>
                        </a>
                        <form style={{width: "35%"}}>
                            <input className="form-control me-2 bg-white text-black" type="search" placeholder="Search"
                                   aria-label="Search" onChange={onInputChange}/>
                        </form>
                    </div>
                </nav>
            </div>
        </>
    );
}

export default Header;
