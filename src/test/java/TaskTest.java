import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.sql2o.*;

public class TaskTest {
  private Task firstTask;
  private Task secondTask;

  @Before
  public void initialize() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
    firstTask = new Task("Mow the lawn", 1);
    secondTask = new Task("Fly to Mars", 1);
  }

  @Test
   public void Task_instantiatesCorrectly_true() {
    assertEquals(true, firstTask instanceof Task);
  }

  @Test
  public void Task_instantiatesWithDescription_String() {
    assertEquals("Mow the lawn", firstTask.getDescription());
  }

  @Test
  public void isCompleted_isFalseAfterInstantiation_false() {
    assertFalse(firstTask.isCompleted());
  }

  @Test
  public void getCreatedAt_instantiateWithCurrentTime_today() {
    assertEquals(LocalDateTime.now().getDayOfWeek(), firstTask.getCreatedAt().getDayOfWeek());
  }

  @Test
  public void setCompleted_changesCompletedToTrue_true() {
    firstTask.setCompleted(true, 1);
    assertTrue(firstTask.isCompleted());
  }

  @Test
  public void all_returnsAllInstancesOfTask_true() {
    firstTask.save();
    secondTask.save();
    assertTrue(Task.all().get(0).equals(firstTask));
    assertTrue(Task.all().get(1).equals(secondTask));
  }

  @Test
  public void getId_tasksInstantiateWithAnId_1() {
    firstTask.save();
    assertTrue(firstTask.getId() > 0);
  }

  @Test
  public void find_returnsTaskWithSameId_secondTask() {
    firstTask.save();
    secondTask.save();
    assertEquals(Task.find(secondTask.getId()), secondTask);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAreTheSame() {
    Task testTask = new Task("Mow the lawn", 1);
    assertTrue(firstTask.equals(testTask));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    Task myTask = new Task("Mow the lawn", 1);
    myTask.save();
    Task savedTask = Task.all().get(0);
    assertTrue(Task.all().get(0).equals(myTask));
    assertEquals(myTask.getId(), savedTask.getId());
  }

  @Test
  public void save_savesCategoryIdIntoDB_true() {
    Category myCategory = new Category("chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn", myCategory.getId());
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertEquals(savedTask.getCategoryId(), myCategory.getId());
  }

  @After
    public void tearDown() {
      try(Connection con = DB.sql2o.open()) {
        String sql = "DELETE FROM tasks *;";
        con.createQuery(sql).executeUpdate();
    }
  }
}
