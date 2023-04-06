package notebook.model.repository.impl;

import logger.Log;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Слой с набором методов для взаимодействия с базой данных
 */
public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final FileOperation operation;
    private static final Logger log = Log.log(UserRepository.class.getName());

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        log.log(Level.INFO, "Создал список контактов");
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        log.log(Level.INFO, "Создается новый контакт");
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long id, User update) {

        try {
            List<User> users = findAll();
            User editUser = users.stream()
                    .filter(u -> u.getId()
                            .equals(id))
                    .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
            editUser.setFirstName(update.getFirstName());
            editUser.setLastName(update.getLastName());
            editUser.setPhone(update.getPhone());
            write(users);
            log.log(Level.INFO, "Обновление контакта");
            return Optional.of(editUser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long id) {
        try {

            List<User> users = findAll();
            User deleteUser = users.stream()
                    .filter(u -> u.getId()
                            .equals(id))
                    .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
            users.remove(deleteUser);

            write(users);
            log.log(Level.INFO, "Удаление контакта");
            return Optional.of(deleteUser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
    }
}
