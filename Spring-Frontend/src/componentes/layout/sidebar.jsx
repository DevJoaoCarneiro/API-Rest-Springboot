import { NavLink } from 'react-router-dom';
import './styles.css';
import { FaCar, FaRegUser, FaCreditCard, FaCartShopping, FaScrewdriverWrench, FaGear } from "react-icons/fa6";

const Sidebar = () => {
    return (
        <aside className='container'>
            <nav className='componentes'>
            
                <NavLink to="/clientes/CadastroCliente" className="container_input"><FaRegUser/> Cliente</NavLink>
                
                <NavLink to="/carro" className="container_input"><FaCar/> Carro</NavLink>
                <NavLink to="/pagamento" className="container_input"><FaCreditCard/> Pagamento</NavLink>
                <NavLink to="/reserva" className="container_input"><FaCartShopping/> Reserva</NavLink>
                <NavLink to="/manutencao" className="container_input"><FaScrewdriverWrench/> Manutenção</NavLink>
            </nav>
            <div className='sidebar-footer'>
                <NavLink to="/configuracoes" className="container_input"><FaGear/> Configurações</NavLink>
            </div>
        </aside>
    );
}

export default Sidebar;
