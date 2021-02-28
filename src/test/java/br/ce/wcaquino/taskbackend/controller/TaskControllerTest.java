package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class TaskControllerTest {

    Task task = new Task();
    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController taskController;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void naoDeveSalvarTarefaSemDescricao()  {
        task.setDueDate(LocalDate.now());
        try {
            taskController.save(task);
            Assert.fail("Não deveria chegar neste ponto");
        } catch (ValidationException e) {
            assertEquals("Fill the task description", e.getMessage());
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemData(){
        task.setTask("Teste Unitário");
        try {
            taskController.save(task);
            Assert.fail("Não deveria chegar neste ponto");
        } catch (ValidationException e) {
            assertEquals("Fill the due date", e.getMessage());
        }
    }

    @Test
    public void naoDeveSalvarTarefaComDataPassada(){
        task.setDueDate(LocalDate.of(2019,01,25));
        task.setTask("Teste Unitário 3");
        try {
            taskController.save(task);
            Assert.fail("Não deveria chegar neste ponto");
        } catch (ValidationException e) {
            assertEquals("Due date must not be in past", e.getMessage());
        }
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws ValidationException {
        task.setDueDate(LocalDate.now());
        task.setTask("Teste Unitário 3");
        taskController.save(task);
        Mockito.verify(taskRepo).save(task);
    }
}
