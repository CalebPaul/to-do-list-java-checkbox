import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CategoryTest {
  private Category firstCategory;
  private Category secondCategory;
  private Task testTask;
  @Before
  public void initialize() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
    firstCategory = new Category("Inside");
    secondCategory = new Category("Outside");
    testTask = new Task("Make bed", 1);
  }

  @Test
  public void Category_instantiatesCorrectly_true() {
    assertTrue(firstCategory instanceof Category);
  }

  @Test
  public void getName_categoryInstantiatesWithName_Inside() {
    assertEquals("Inside", firstCategory.getName());
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Category testCategory = new Category("Inside");
    assertTrue(firstCategory.equals(testCategory));
  }

  @Test
  public void all_checksIfCategoryListContainsInstance_true() {
    firstCategory.save();
    secondCategory.save();
    assertTrue(Category.all().get(0).equals(firstCategory));
    assertTrue(Category.all().get(1).equals(secondCategory));
   }

  @Test
  public void getID_returnsIDNumber_int() {
    firstCategory.save();
    assertTrue(firstCategory.getId() > 0);
  }

  @Test
  public void getTasks_retrievesAllTasksFromDataBase_tasksList() {
    firstCategory.save();
    Task firstTask = new Task("Mow the lawn", firstCategory.getId());
    firstTask.save();
    Task secondTask = new Task("Do the dishes", firstCategory.getId());
    secondTask.save();
    Task[] tasks = new Task[] { firstTask, secondTask };
    assertTrue(firstCategory.getTasks().containsAll(Arrays.asList(tasks)));
  }

  @Test
  public void find_returnsCategoryWithSameId_secondCategory() {
    firstCategory.save();
    secondCategory.save();
    assertEquals(Category.find(secondCategory.getId()), secondCategory);
  }

  @Test
  public void save_assignsIdToObject() {
    firstCategory.save();
    Category savedCategory = Category.all().get(0);
    assertEquals(firstCategory.getId(), savedCategory.getId());
  }

  @Test
  public void save_savesIntoDatabase_true() {
    firstCategory.save();
    assertTrue(Category.all().get(0).equals(firstCategory));
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM tasks *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }
}
