package br.com.felipemariano.toDoSimple.services;

import br.com.felipemariano.toDoSimple.models.User;
import br.com.felipemariano.toDoSimple.models.enums.ProfileEnum;
import br.com.felipemariano.toDoSimple.repositories.TaskRepository;
import br.com.felipemariano.toDoSimple.repositories.UserRepository;
import br.com.felipemariano.toDoSimple.services.exceptions.DataBindingViolationException;
import br.com.felipemariano.toDoSimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado! Id: " + id + ". Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);

        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));

        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        }catch (Exception e) {
            throw new DataBindingViolationException("Não foi possível excluir o usuário, pois existem entidades relacionadas a ele!");
        }
    }
}
