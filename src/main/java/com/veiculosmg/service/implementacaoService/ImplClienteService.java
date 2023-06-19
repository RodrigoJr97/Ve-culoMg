package com.veiculosmg.service.implementacaoService;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.ElementoEmUmRelacionamentoException;
import com.veiculosmg.exception.MenorDeIdadeException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.model.repository.ClienteRepository;
import com.veiculosmg.model.repository.EnderecoRepository;
import com.veiculosmg.service.interfaceService.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImplClienteService implements ClienteService {

    private final ClienteRepository clienteRepository;

    private final EnderecoRepository enderecoRepository;

    public ImplClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public Cliente salvaNovaEntidade(Cliente cliente) {
        log.info("Criação de novo cliente iniciada.");
        List<String> listaDeAtributosDuplicados = obterAtributosDuplicados(cliente);
        try {
            verificaSeClienteEMaiorDeIdade(cliente.getDataNascimento());

            clienteRepository.save(cliente);

            log.info("Criação de novo cliente concluída.");
            return cliente;
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro. Atributos {} já estão vinculados a outro cliente.", listaDeAtributosDuplicados);
            throw new AtributoDuplicadoException("Os seguintes atributos já estão vinculados a outro cliente: " + listaDeAtributosDuplicados);
        }
    }

    @Override
    public List<Cliente> listaEntidades() {
        log.info("Busca lista de clientes inciada.");

        log.info("Busca lista de clientes concluída.");
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> entidadePorId(Long id) {
        return Optional.of(verificaSeClienteExiste(id));
    }

    @Override
    public void updateEntidade(Cliente clienteAtualizado, Long id) {
        log.info("Atualização do Cliente Id:{}", id + " iniciada.");
        try {
            Cliente clienteCadastradoComIdInformado = verificaSeClienteExiste(id);

            log.info("Verificando se CPF, Número de Telefone ou Email passado no cliente atualizado já está vinculada a outro cliente");
            verificaSeCpfEmailENumeroTelefoneJaEstaCadastrado(clienteCadastradoComIdInformado, clienteAtualizado);

            clienteAtualizado.setId(id);

            log.info("Atualização do cliente realizada com sucesso.");
            clienteRepository.save(clienteAtualizado);
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro ao atualizar Cliente.", ex);
            throw new AtributoDuplicadoException("Atributos duplicados");
        }
    }

    @Override
    public void deletaEntidade(Long id) {
        log.info("Delete do Cliente Id:{}", id + " iniciada.");
        Cliente cliente = verificaSeClienteExiste(id);

        if (!cliente.isDisponivel()) throw new ElementoEmUmRelacionamentoException("Cliente está relacionado a um Aluguel.");

        log.info("Delete do Cliente concluído");
        clienteRepository.deleteById(id);
    }

    /* Privado */
    private void verificaSeCpfEmailENumeroTelefoneJaEstaCadastrado(Cliente clienteCadastrado, Cliente clienteAtualizado) {
        log.info("Iniciando a verificação dos campos atualizados do cliente.");
        List<String> camposDuplicados = new ArrayList<>();
        String cpf = clienteAtualizado.getCpf();
        String numeroTelefone = clienteAtualizado.getNumeroTelefone();
        String email = clienteAtualizado.getEmail();

        if (!clienteCadastrado.getCpf().equals(cpf) && clienteRepository.existsByCpf(cpf)) {
            camposDuplicados.add("CPF");
        }

        if (!clienteCadastrado.getNumeroTelefone().equals(numeroTelefone) && clienteRepository.existsByNumeroTelefone(numeroTelefone)) {
            camposDuplicados.add("Número de Telefone");
        }

        if (!clienteCadastrado.getEmail().equals(email) && clienteRepository.existsByEmail(email)) {
            camposDuplicados.add("E-mail");
        }

        if (!camposDuplicados.isEmpty()) {
            log.error("Erro. Atributos já estão vinculados a outro cliente: {}", camposDuplicados);
            throw new AtributoDuplicadoException("Atributos já estão vinculados a outro cliente: " + camposDuplicados);
        }

        log.info("Verificação concluída.");
    }

    private void verificaSeClienteEMaiorDeIdade(LocalDate dataNascimento) {
        LocalDate dataAtual = LocalDate.now();
        int idade = Period.between(dataNascimento, dataAtual).getYears();

        if (idade < 18) {
            throw new MenorDeIdadeException("Cliente é menor de idade.");
        }
    }

    private Cliente verificaSeClienteExiste(Long id) {
        log.info("Verificando se cliente existe.");
        Optional<Cliente> existeClienteComOIdInformado = clienteRepository.findById(id);

        if (existeClienteComOIdInformado.isEmpty()) {
            log.info("Cliente com Id: {}", id + " não encontrado!");
            throw new RecursoNaoEncontradoException("Cliente com Id: " + id + " Não Encontrado!");
        }

        log.info("Verificação se o cliente existe concluída.");
        return existeClienteComOIdInformado.get();
    }

    private List<String> obterAtributosDuplicados(Cliente clienteParaVerificarCampos) throws DataIntegrityViolationException {
        List<String> listaDeCamposDuplicados = new ArrayList<>();
        clienteRepository.findAll().forEach(cliente -> {
            if (cliente.getCpf().equals(clienteParaVerificarCampos.getCpf())) {
                listaDeCamposDuplicados.add("CPF");
            }
            if (cliente.getNumeroTelefone().equals(clienteParaVerificarCampos.getNumeroTelefone())) {
                listaDeCamposDuplicados.add("Número Telefone");
            }
            if (cliente.getEmail().equalsIgnoreCase(clienteParaVerificarCampos.getEmail())) {
                listaDeCamposDuplicados.add("Email");
            }
        });

        return listaDeCamposDuplicados;
    }


}
