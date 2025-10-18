import { useState } from 'react';
import './stylesCadastro.css';
import axios from "axios";
import { useNavigate } from 'react-router-dom';

const CadastroCliente = () => {
    const apiUrl = import.meta.env.VITE_API_URL;


    const [formData, setFormData] = useState({
        nome: "",
        sobrenome: "",
        documento: "",
        rua: "",
        cep: "",
        bairro: "",
        complemento: "",
        numero: "",
        telefone: "",
        email: ""
    })

    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleCep = async () => {
        const cep = formData.cep.replace(/\D/g, "");
        if (cep.length !== 8) {
            return
        }

        try {
            const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`);
            if (!response.data.erro) {
                setFormData({
                    ...formData,
                    rua: response.data.logradouro,
                    bairro: response.data.bairro,
                    complemento: response.data.complemento || "",
                });
            } else {
                alert("CEP não encontrado!");
            }
        } catch {
            console.error("Erro ao buscar CEP:", error);
            alert("Erro ao buscar CEP. Tente novamente.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const nomeCompleto = `${formData.nome} ${formData.sobrenome}`.trim();

        const clienteParaEnviar = {
            nome: nomeCompleto,
            email: formData.email,
            telefone: formData.telefone,
            documento: formData.documento,
            endereco: {
                rua: formData.rua,
                cep: formData.cep,
                bairro: formData.bairro,
                complemento: formData.complemento,
                numero: formData.numero
            }
        };

        try {
            const response = await axios.post(`${apiUrl}/cliente`, clienteParaEnviar);
            console.log("Cliente cadastrado:", response.data);
            alert("Cliente cadastrado com sucesso!");
        } catch (error) {
            console.error("Erro ao cadastrar cliente:", error);
            alert("Erro ao cadastrar cliente. Tente novamente.");
        }
    };


    return (
        <form onSubmit={handleSubmit}>
            <div className='Button_input_select'>
                <button className='style_button'>Cadastrar Cliente</button>
                <button className='style_button' onClick={() => navigate("/clientes/ConsultaCliente")}>Consultar Cliente</button>
            </div>
            <div className='form_container'>
                <div className='container_space'>
                    <div className='container_input_label'>
                        <label htmlFor="nome">Nome</label>
                        <input
                        id="nome"
                            name="nome"
                            value={formData.nome}
                            onChange={handleChange}
                            placeholder="Digite seu nome..."
                            className="form_input"
                            required />

                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Sobrenome</label>
                        <input
                            name="sobrenome"
                            value={formData.sobrenome}
                            onChange={handleChange}
                            placeholder="Digite seu sobrenome..."
                            className="form_input" />
                    </div>
                </div>

                <div className='container_space'>
                    <div className='container_input_label'>
                        <label htmlFor="nome">Documento</label>
                        <input
                            name="documento"
                            type='number'
                            value={formData.documento}
                            onChange={handleChange}
                            placeholder="Digite seu documento..."
                            className="form_input"
                            required
                        />

                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Telefone</label>
                        <input
                            name="telefone"
                            type='number'
                            value={formData.telefone}
                            onChange={handleChange}
                            placeholder="Digite sua telefone..."
                            className="form_input" />
                    </div>
                </div>

                <div className='container_space_info'>
                    <div className='container_input_label'>
                        <label htmlFor="nome">Cep</label>
                        <input
                            name="cep"
                            type='number'
                            value={formData.cep}
                            onChange={handleChange}
                            onBlur={handleCep}
                            placeholder="Digite seu cep..."
                            className="form_input" />
                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Bairro</label>
                        <input
                            name="bairro"
                            value={formData.bairro}
                            onChange={handleChange}
                            placeholder="Digite seu bairro..."
                            className="form_input" />
                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Complemento</label>
                        <input
                            name="complemento"
                            value={formData.complemento}
                            onChange={handleChange}
                            placeholder="Digite seu complemento..."
                            className="form_input" />
                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Numero</label>
                        <input
                            name="numero"
                            type='number'
                            value={formData.numero}
                            onChange={handleChange}
                            placeholder="Numero..."
                            className="form_input" />
                    </div>
                </div>

                <div className='container_space_contato'>
                    <div className='container_input_label'>
                        <label htmlFor="nome">Rua</label>
                        <input
                            name="rua"
                            value={formData.rua}
                            onChange={handleChange}
                            placeholder="Digite seu rua..."
                            className="form_input" />
                    </div>

                    <div className='container_input_label'>
                        <label htmlFor="nome">Email</label>
                        <input
                            name="email"
                            type='email'
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Digite seu email..."
                            className="form_input"
                            required />

                    </div>
                </div>
            </div>
            <div className='container_button_form'>
                <button className='form_button'>Cadastrar Cliente</button>
            </div>
        </form>
    );
}

export default CadastroCliente;