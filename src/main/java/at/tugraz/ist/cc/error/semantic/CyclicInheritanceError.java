package at.tugraz.ist.cc.error.semantic;

/**
 * DO NOT CHANGE THIS CLASS OR PACKAGE!
 * This Class is used for automated testing.
 */
public class CyclicInheritanceError extends SemanticError {

    /**
     * @param class_name
     *        The name of the class currently being defined.
     * @param superclass_name
     *        The class name of the defined superclass.
     * @param line
     * 		  The line number in the input where the superclass identifier is used to specify an inheritance, i.e.,
     * 		  the position of the 'CLASS_ID' on the left-hand side of the ':' operator.
     */
    public CyclicInheritanceError(String class_name, String superclass_name, int line) {
        super("Cyclic inheritance between '"+class_name+ "' and '"+ superclass_name + "'.", line);
    }
}
