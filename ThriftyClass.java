package homeAwayApp.student;

import homeAwayApp.constants.StudentType;

/**
 * BookishStudentClass represents a Thrifty student.
 * Implements the ThriftyStudent interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class ThriftyClass extends AbstractStudent implements Thrifty {

    /**
     * Constructs a new Student of type Thrifty.
     *
     * @param name Student's name
     * @param country Student's country
     * @param lodging Student's Lodging
     */
    public ThriftyClass(String name, String country, String lodging) {
        super(name, country, lodging);
    }

    @Override
    public String getType() {
        return StudentType.THRIFTY.toString();
    }
}
