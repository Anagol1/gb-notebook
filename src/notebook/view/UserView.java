package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.List;
import java.util.Scanner;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com;

        while (true) {
            String command = prompt("������� �������: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    String firstName = prompt("���: ");
                    String lastName = prompt("�������: ");
                    String phone = prompt("����� ��������: ");
                    User newUser = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .phone(phone).build();
                    userController.saveUser(newUser);
                    break;
                case READ:
                    String id = prompt("������������� ������������: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case LIST:
                    List<User> users = userController.getAllUsers();
                        for (User user: users
                             ) {
                            System.out.println(user);
                        }
                        break;

                case UPDATE:
                    String userId = prompt("������� user id: ");
                    userController.updateUser(userId, createUser());
                    break;

                case DELETE:
                    String userIdDel = prompt("������� user id: ");
                    userController.deleteUser(userIdDel);
                    break;
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
    private User createUser() {
        String firstName = prompt("���: ");
        String lastName = prompt("�������: ");
        String phone = prompt("����� ��������: ");
//        return new User(firstName, lastName, phone);

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .build();
    }
}
