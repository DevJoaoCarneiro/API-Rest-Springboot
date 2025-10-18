import "./navbar.css";
import { FaUserCircle } from "react-icons/fa";
import { FaCar } from "react-icons/fa6";

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-left">
        <h1 className="navbar-title"><FaCar/>MovCar</h1>
      </div>

      <div className="navbar-right">
        <FaUserCircle className="navbar-icon" />
        <span className="navbar-username">João Victor</span>
      </div>
    </nav>
  );
};

export default Navbar;
