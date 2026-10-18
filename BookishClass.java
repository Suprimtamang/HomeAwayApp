package homeAwayApp.student;

import homeAwayApp.constants.StudentType;

/**
 * BookishStudentClass represents an outgoing student.
 * Implements the BookishStudent interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */

public class BookishClass extends AbstractStudent implements Bookish {

    /**
     * Constructs a new Student of type Bookish.
     *
     * @param name Student's name
     * @param country Student's country
     * @param lodging Student's Lodging
     */
    public BookishClass(String name, String country, String lodging) {
        super(name,country, lodging);
    }

    @Override
    public String getType() {
        return StudentType.BOOKISH.toString();
    }
}
