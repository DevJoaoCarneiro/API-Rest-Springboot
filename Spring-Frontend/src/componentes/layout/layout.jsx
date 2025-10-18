import { Outlet } from 'react-router-dom';
import Sidebar from './sidebar';
import Navbar from './navbar';
import "./layout.css";

const Layout = () => {
  return (
    <div className="app-container">
      <Sidebar />
      <div className="content-wrapper">
        <Navbar />
        <main className="page-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
