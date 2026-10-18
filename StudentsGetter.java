package homeAwayApp.collection;

import dataStructures.Iterator;

/**
 * Represents a collection of Students.
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public interface StudentsGetter {
    /**
     * Checks if there are any students in the students collection.
     * @return <code>true</code> if there are any students in the collection,
     *         <code>false</code> otherwise
     */
    boolean hasAnyStudents();

    /**
     * Iterates over every student in a collection.
     * @return an iterator of students
     */
    Iterator<homeAwayApp.student.StudentGetter> getStudents();


}
