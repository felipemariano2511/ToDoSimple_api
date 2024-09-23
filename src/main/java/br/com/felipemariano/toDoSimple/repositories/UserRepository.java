package br.com.felipemariano.toDoSimple.repositories;

import br.com.felipemariano.toDoSimple.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { }
