package br.com.ditwowin.api.services;

import br.com.ditwowin.api.models.Client;
import br.com.ditwowin.api.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Set;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;



    public boolean create(Client client) {
        if (Optional.ofNullable(clientRepository.findByCpf(client.getCpf())).isPresent()) {
            return false;
        }

        clientRepository.save(client);
        return true;
    }

    public boolean delete(String clientCpf) {
        Optional<Client> client = Optional.ofNullable(clientRepository.findByCpf(clientCpf));

        if (clientCpf == null || client.isEmpty()) {
            return false;
        }

        clientRepository.deleteById(client.get().getId());
        return true;
    }

    public Client getByCpf(String cpf) {
        if (cpf == null || CPFValidateService.validateCPF(cpf)) {
            return null;
        }

        return clientRepository.findByCpf(cpf);
    }
}
