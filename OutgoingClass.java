package homeAwayApp.student;

import homeAwayApp.constants.StudentType;

/**
 * OutgoingStudentClass represents an outgoing student.
 * Implements the OutgoingStudent interface.
 *
 * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
 * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
 */
public class OutgoingClass extends AbstractStudent implements Outgoing {

    /**
     * Constructs a new Student of type Outgoing.
     *
     * @param name Student's name
     * @param country Student's country
     * @param lodging Student's Lodging
     */
    public OutgoingClass(String name, String country, String lodging){
        super(name, country, lodging);
    }

    @Override
    public String getType() {
        return StudentType.OUTGOING.toString();
    }
}
