package command;

import database.ManagerSingleton;

import javax.persistence.EntityManager;

public class CommandRepository {
    ManagerSingleton managerSingleton = ManagerSingleton.getInstance();
    private EntityManager entityManager = managerSingleton.getEntityManagerFactory().createEntityManager();

    public Command findByName(String name) {
        return (Command) entityManager.createNamedQuery("Command.findByName").setParameter("name", name).getSingleResult();
    }

    public String findAllNames() {
        StringBuilder stringBuilder = new StringBuilder();
        var commands = entityManager.createNamedQuery("Command.findAllNames").getResultList();
        for (Object command : commands) {
            stringBuilder.append(command);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
