package homeAwayApp.collection;
        import dataStructures.DoublyLinkedList;
        import dataStructures.Iterator;
        import homeAwayApp.student.StudentGetter;

        import java.io.Serial;
        import java.io.Serializable;


        /**
         * Represents a collection of Students.
         * Implements the StudentsCollection interface.
         *
         * @author Suprim Tamang (71310) s.tamang@campus.fct.unl.pt
         * @author Mahdi Srosh   (75593) m.srosh@campus.fct.unl.pt
         */
        public class StudentsClass implements StudentsSetter, Serializable {

            @Serial
            private static final long serialVersionUID = 1L;
            /**
             * A collection containing all the <code>students</code>.
             */
            private final DoublyLinkedList<StudentGetter> students;

            /**
             * The StudentsCollectionClass constructor.
             */
            public StudentsClass(){
                this.students = new DoublyLinkedList<>();
            }

            @Override
            public void reset() {
                while (!students.isEmpty()) {
                    students.removeLast();
                }
            }

            @Override
            public void addStudent(StudentGetter studentGetter){
                Iterator<StudentGetter> it = students.iterator();
                while (it.hasNext()) {
                    if (it.next().getName().equalsIgnoreCase(studentGetter.getName())) {
                        return; // already exists, skip adding
                    }
                }
                students.addLast(studentGetter);
            }

            @Override
            public void removeStudent(StudentGetter studentGetter){
                Iterator<StudentGetter> it = students.iterator();
                int position = 0;
                while (it.hasNext()) {
                    if (it.next().getName().equalsIgnoreCase(studentGetter.getName())) {
                        students.remove(position);
                        return;
                    }
                    position++;
                }
            }

            @Override
            public boolean hasAnyStudents() {
                return !students.isEmpty();
            }

            @Override
            public Iterator<StudentGetter> getStudents() {
                return students.iterator();
            }
        }