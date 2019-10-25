/*
* Name: Jose Orellana
* UCID: 30022585
* Course: CPSC 501
* Tut: T01
* Assignment: 02
*
* Inspector class for reflection
*/

import java.lang.reflect.*;
import java.util.*;

public class Inspector {

	public Inspector() {}

    public void inspect(Object obj, boolean recursive) {
        // Class c = obj.getClass();
        // inspectClass(c, obj, recursive, 0);
        if (obj != null) {
			Class<?> objClass = obj.getClass();
			Vector<Field> objectsToInspect = new Vector<Field>();
			
			System.out.println("inside inspector: " + obj.getClass().getTypeName() + " (recursive = "+ recursive +")");
		
			if(obj.getClass().isArray()){		// This checks if the non-primitive types are an array and inspects them
				for(int i = 0; i < Array.getLength(obj); i++){
					inspect(Array.get(obj, i), recursive);
				}
			} else {
				//inspect the current class
				inspectClass(objClass, obj, recursive, objectsToInspect);
				
				if(recursive) {
					inspectFieldClasses(objClass, obj, recursive, objectsToInspect);
				}
			}
		} else {
			System.out.println("NULL");
		}
    }

    private void inspectClass(Class<?> c, Object obj, boolean recursive, Vector<Field> objectsToInspect) {
    	inspectDeclaringClass(obj);
		inspectImmediateSuperClass(obj);
		inspectInterface(obj);
		inspectClassMethods(obj);
		inspectConstructor(obj);
		inspectFields(obj, c, objectsToInspect);
		
		// traverse hierarchy get constructors/methods/field of superclass
        Class<?> superClassObject = c.getSuperclass();
        if (superClassObject == Object.class) {
        	System.out.print("\n\t");
        	inspectDeclaringClass(superClassObject);
    		inspectImmediateSuperClass(superClassObject);
    		inspectInterface(superClassObject);
    		inspectClassMethods(superClassObject);
    		inspectConstructor(superClassObject);
    		inspectFields(superClassObject, superClassObject, objectsToInspect);
        } else {
        	System.out.print("\n\t");
        	inspectClass(superClassObject, superClassObject, recursive, objectsToInspect);
        }
	}

    private void inspectDeclaringClass(Object obj) {
		System.out.println("\n---------- Declaring Class ----------\n");
		if(!obj.getClass().getName().isEmpty()) {
			System.out.println("Class Name: " + obj.getClass().getName());
		} else {
			System.out.println("NULL");
		}
	}

	private void inspectImmediateSuperClass(Object obj) {
		System.out.println("\n---------- Immediate Superclass ----------\n");
		if(!obj.getClass().getSuperclass().getName().isEmpty()) {
			System.out.println("Immediate Super Class: " + obj.getClass().getSuperclass().getName());
		} else {
			System.out.println("NULL");
		}
	}

	private void inspectInterface(Object obj) {
		System.out.println("\n---------- Interfaces ----------\n");
		if(obj.getClass().getInterfaces().length >= 1) {
	    	for (Class<?> interF : obj.getClass().getInterfaces()) {
	    		System.out.println("Interface: " + interF.getName());
			}
		} else {
			System.out.println("NULL");
		}
	}

	private void inspectClassMethods(Object obj) {
		System.out.println("\n---------- Class Methods ----------\n");
		for (Method method : obj.getClass().getDeclaredMethods()) {
			if(method.getDeclaringClass().equals(obj.getClass())) {
				System.out.println("\nName: " + method.getName());
					// Loops each for exception and parameter
					for (Class<?> excepThrown : method.getExceptionTypes()) {
						System.out.println("Exception Throws: " + excepThrown.getName());
					}
					for (Parameter param : method.getParameters()) {
						System.out.println("Parameter: " + param.getParameterizedType());
					}
					System.out.println("Return Type: " + method.getReturnType().getName());
					System.out.println("Modifier: " + Modifier.toString(method.getModifiers()));
			}
		}
	}

	private void inspectConstructor(Object obj) {
		System.out.println("\n---------- Class Constructor ----------\n");
		
		for (Constructor<?> construc : obj.getClass().getConstructors()) {
			System.out.println("Constructor: " + construc.getName());
			for (Parameter param : construc.getParameters()) {
				System.out.println("Parameter: " + param.getParameterizedType());
			}
			System.out.println("Modifier: " + Modifier.toString(construc.getModifiers()) + "\n");
		}
	}

	private void inspectFields(Object obj, Class<?> ObjClass, Vector<Field> objectsToInspect) {
    	System.out.println("\n---------- Inspecting Fields ----------\n");
    	if(ObjClass.getDeclaredFields().length >= 1) {
    		for (Field f : ObjClass.getDeclaredFields()) {
    			f.setAccessible(true);
    			
    			if(!f.getType().isPrimitive()) 				//This is setting up the recursive part if need
    			    objectsToInspect.addElement(f);			//It's adding b/c we may need to extract detail of class or object
    			try {	
    				print(f, obj);
    			} catch(Exception e) {}    
			}
		if(ObjClass.getSuperclass() != null )
			if(ObjClass.getSuperclass().getDeclaredFields().length >=1)
				inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	    }
	}

	private void print(Field f, Object obj) throws IllegalArgumentException, IllegalAccessException{
		System.out.println("\nField: " + f.getName() + "	\nType: " + f.getType().getName() + " = " + f.get(obj).toString() + "\nModifier: " + Modifier.toString(f.getModifiers()));	
		if (f.getType().isArray()) {
			System.out.format("--------------------------------------- Array%n" + "Length: %s%n" + "Component Type: %s%n", Array.getLength(f.get(obj)), f.getType().getComponentType().getName());
		}
	}

	private void inspectFieldClasses(Class<?> c, Object obj, boolean recursive, Vector<Field> objectsToInspect) {
		if(objectsToInspect.size() > 0 )
		    System.out.println("\n\n================================================"
		    		+ " Inspecting Field Classes "
		    		+ "================================================\n\n");
		
		Enumeration<Field> e = objectsToInspect.elements(); 				//this vector keeps all the non-primitive data 
		while(e.hasMoreElements()) {										// the needs to be further inspected
			Field f = (Field) e.nextElement();
			System.out.println("-------------------------------------------------");
			System.out.println("---------- Inspecting Field: " + f.getName() + " ----------" );
			
			try {
				System.out.println("-------------------------------------------------\n");
				inspect(f.get(obj) , recursive);
				System.out.println("\n-------------------------------------------------");
			}
			catch(Exception exp) { exp.printStackTrace(); }
		}
	}
}