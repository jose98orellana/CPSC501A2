import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Array;

public class Test {

    @Test
    public void testInspectorObject() {
        assertNotNull(new Inspector());

        Inspector inspector = new Inspector();
        Class inspectorClass = inspector.getClass();
        assertNotNull(inspectorClass);

        assertNotNull(inspector.getObjectHash());
    }

    @Test
    public void testInspect() {
        Inspector inspector = new Inspector();

        try{
            Object obj1 = new ClassA();
            assertFalse(inspector.alreadyInspected(obj1));
            inspector.inspect(obj1, false);

            HashSet<Integer> objectHash = inspector.getObjectHash();
            assertEquals(1, objectHash.size());
            assertTrue(inspector.alreadyInspected(obj1));

            Object obj2 = new ClassA(12);
            assertFalse(inspector.alreadyInspected(obj2));
            inspector.inspect(obj2, false);
            assertEquals(2, objectHash.size());
            assertTrue(inspector.alreadyInspected(obj2));

            Object obj3 = new ClassB();
            assertFalse(inspector.alreadyInspected(obj3));
            inspector.inspect(obj3, false);
            assertEquals(3, objectHash.size());
            assertTrue(inspector.alreadyInspected(obj3));
            //classB should recurse twice, but HashSet should already have classB hashcode
            inspector.inspect(obj3, true);
            assertEquals(5, objectHash.size());

            Object obj4 = new ClassD();
            assertFalse(inspector.alreadyInspected(obj4));
            inspector.inspect(obj4, false);
            assertEquals(6, objectHash.size());
            assertTrue(inspector.alreadyInspected(obj4));

            Object obj5 = new ClassB[10];
            assertFalse(inspector.alreadyInspected(obj5));
            inspector.inspect(obj5, false);
            assertTrue(inspector.alreadyInspected(obj5));

            String testString = "Hello World";
            assertFalse(inspector.alreadyInspected(testString));
            inspector.inspect(testString, true);
            assertTrue(inspector.alreadyInspected(testString));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}