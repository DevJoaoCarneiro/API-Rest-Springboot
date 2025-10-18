import { Routes, Route } from 'react-router-dom';
import Layout from './componentes/layout/layout';
import './App.css';
import CadastroCliente from './assets/pages/Cliente/cadastraCliente';
import ConsultaCliente from './assets/pages/Cliente/consultaCliente';
import EditaCliente from './assets/pages/Cliente/editaCliente';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route path="/clientes/CadastroCliente" element={<CadastroCliente />} />
        <Route path="/clientes/ConsultaCliente" element={<ConsultaCliente />} />
        <Route path="/clientes/EditaCliente/:id" element={<EditaCliente />} />
      </Route>
    </Routes>
  );
}

export default App;