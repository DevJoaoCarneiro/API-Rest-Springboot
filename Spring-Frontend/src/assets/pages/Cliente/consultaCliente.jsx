import { useEffect, useState } from 'react';
import axios from "axios";
import { useNavigate } from "react-router-dom";
import './stylesConsulta.css';

const ConsultaCliente = () => {
    const apiUrl = import.meta.env.VITE_API_URL;
    const [clientes, setCliente] = useState([]);
    const [clienteSelecionado, setClienteSelecionado] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get(`${apiUrl}/cliente`)
            .then((response) => setCliente(response.data))
            .catch((error) => console.error("Erro ao buscar clientes:", error));
    }, []);



    return (
        <div>
            <div className='Button_input_select'>
                <button
                    className='style_button'
                    onClick={() => navigate("/clientes/CadastroCliente")}
                >
                    Cadastrar Cliente
                </button>
                <button className='style_button'>Consultar Cliente</button>
            </div>

            <div className='table_container'>
                <table className="table">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th>Rua</th>
                            <th>Bairro</th>
                            <th>Número</th>
                            <th>CEP</th>
                        </tr>
                    </thead>

                    <tbody>
                        {clientes.map((cliente) => (
                            <tr key={cliente.id}
                                onClick={() => setClienteSelecionado(cliente.id)}
                                style={{
                                    cursor: "pointer",
                                    backgroundColor: clienteSelecionado === cliente.id ? "#d6e4ff" : "white"
                                }}
                            >
                                <td>{cliente.nome}</td>
                                <td>{cliente.email}</td>
                                <td>{cliente.telefone}</td>
                                <td>{cliente.endereco?.rua}</td>
                                <td>{cliente.endereco?.bairro}</td>
                                <td>{cliente.endereco?.numero}</td>
                                <td>{cliente.endereco?.cep}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <div className='container_button_form'>
                <button className='form_button'
                    onClick={() => {
                        if (clienteSelecionado) {
                            navigate(`/clientes/EditaCliente/${clienteSelecionado}`)
                        }else{
                            alert("Nenhum cliente selecionado! Selecione para continuar");
                        }
                    }}
                >Consultar Cliente</button>
            </div>
        </div>
    );
};

export default ConsultaCliente;
