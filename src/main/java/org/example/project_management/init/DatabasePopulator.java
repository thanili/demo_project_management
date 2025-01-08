package org.example.project_management.init;

import org.example.project_management.entity.*;
import org.example.project_management.entity.auth.Role;
import org.example.project_management.entity.auth.User;
import org.example.project_management.repository.ClientRepository;
import org.example.project_management.repository.InvoiceRepository;
import org.example.project_management.repository.ProjectRepository;
import org.example.project_management.repository.ProjectTaskRepository;
import org.example.project_management.repository.auth.RoleRepository;
import org.example.project_management.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class DatabasePopulator {

    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository taskRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabasePopulator(ClientRepository clientRepository,
                             ProjectRepository projectRepository,
                             ProjectTaskRepository taskRepository,
                             InvoiceRepository invoiceRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void populateApiUsers() {
        List<Role> roles = new ArrayList<>();
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        roles.add(roleAdmin);
        roles.add(roleUser);
        Iterable<Role> savedRoles = roleRepository.saveAll(roles);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        //admin.setPassword("password");
        admin.setEnabled(true);
        admin.setRoles(Set.of(roleAdmin));

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        //user.setPassword("password");
        user.setEnabled(true);
        user.setRoles(Set.of(roleUser));

        userRepository.save(admin);
        userRepository.save(user);
    }

    @Transactional
    public void populateDatabase() {
        // Create and save clients
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Client client = new Client();
            client.setName("Client " + i);
            client.setEmail("client" + i + "@example.com");
            client.setPhone("+30 12345678" + String.valueOf(i));

            List<Project> projects = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                Project project = new Project();
                project.setTitle("Project " + j + " for Client " + i);
                project.setClient(client);
                project.setDescription("Description for Project " + j);
                project.setStatus(ProjectStatus.IN_PROGRESS);
                project.setStartDate(LocalDate.now().minusDays(j));
                project.setDeadline(LocalDate.now().plusDays(j * 5));

                // Add tasks to each project
                List<ProjectTask> tasks = new ArrayList<>();
                for (int k = 1; k <= 5; k++) {
                    ProjectTask task = new ProjectTask();
                    task.setTitle("Task " + k + " for Project " + j);
                    task.setStatus(TaskStatus.IN_PROGRESS);
                    task.setDescription("Description for Task " + k);
                    task.setDueDate(LocalDate.now().plusDays(k * 2));
                    task.setProject(project);
                    tasks.add(task);
                }
                project.setTasks(tasks);

                // Add invoices to each project
                List<Invoice> invoices = new ArrayList<>();
                for (int l = 1; l <= 3; l++) {
                    Invoice invoice = new Invoice();
                    invoice.setAmount(1000.00 + l);
                    invoice.setStatus(InvoiceStatus.ISSUED);
                    invoice.setDueDate(LocalDate.now().plusDays(l * 10));
                    invoice.setProject(project);
                    invoices.add(invoice);
                }
                project.setInvoices(invoices);

                projects.add(project);
            }

            client.setProjects(projects);
            clients.add(client);
        }

        clientRepository.saveAll(clients);
        System.out.println("Database populated with sample data.");
    }

    @Transactional
    public void clearDatabase() {
        invoiceRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        clientRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        System.out.println("Database cleared.");
    }
}
