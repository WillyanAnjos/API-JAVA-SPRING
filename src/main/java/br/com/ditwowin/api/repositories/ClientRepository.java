package br.com.ditwowin.api.repositories;

import br.com.ditwowin.api.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public Client findByCpf(String cpf);
}