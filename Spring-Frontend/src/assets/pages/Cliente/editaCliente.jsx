import { useEffect, useState } from 'react';
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { FaChevronLeft } from "react-icons/fa6";
import { useParams } from "react-router-dom";
import './stylesEdita.css';

const EditaCliente = () => {
    const apiUrl = import.meta.env.VITE_API_URL;
    const { id } = useParams();

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

    console.log(id);
    
    useEffect(() => {
        axios.get(`${apiUrl}/cliente/${id}`)
            .then((res) => {
                const cliente = res.data;

                const [nome, sobrenome = ""] = cliente.nome.split(" ");

                setFormData({
                    nome,
                    sobrenome,
                    documento: cliente.documento,
                    telefone: cliente.telefone,
                    email: cliente.email,
                    rua: cliente.endereco?.rua || "",
                    cep: cliente.endereco?.cep || "",
                    bairro: cliente.endereco?.bairro || "",
                    complemento: cliente.endereco?.complemento || "",
                    numero: cliente.endereco?.numero || ""
                })
            });
    }, [id]);
    


    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handlePutSubmit = async (e) => {
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
            const response = await axios.put(`${apiUrl}/cliente/${id}`, clienteParaEnviar);
            console.log("Cliente com id: "+id + "Atualizado");
            alert("Cliente atualizado com sucesso!");
            navigate("/clientes/ConsultaCliente");
        } catch (error) {
            console.error("Erro ao atualizar cadastrar cliente:", error);
            alert("Erro ao atualizar cadastrar cliente. Tente novamente.");
        }
    };

    const handleDelete = async (e) => {
        e.preventDefault();
        if (!window.confirm("Tem certeza que deseja excluir este cliente?")) return;
        try{
            const response = await axios.delete(`${apiUrl}/cliente/${id}`);
            console.log("Cliente deletado com id.."+id+" Foi deletado com sucesso");
            navigate("/clientes/ConsultaCliente");

        }catch(err){
            console.log("Erro ao excluir o cliente");
            alert("Erro ao excluir o cliente com o id..", id);
        }
    }

    return (
        <div>

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
                            value={formData.cep}
                            onChange={handleChange}
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
                <div className='container_button_edit'>
                    <button
                        onClick={() => {
                            navigate("/clientes/ConsultaCliente")
                        }}
                        className='back_button'
                    ><FaChevronLeft /> Voltar</button>
                    <button className='form_button edita' onClick={handlePutSubmit}>Edita Cliente</button>
                </div>
            </div>
            <div className='container_button_form'>
                <button className='form_button Excluir' onClick={handleDelete}>Excluir Cliente</button>
            </div>
        </div>
    );
};

export default EditaCliente;
