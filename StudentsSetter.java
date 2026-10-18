package homeAwayApp.collection;

public interface StudentsSetter extends StudentsGetter {
    /**
     * Removes all data from the class.
     * Deletes every student in the collection.
     */
    void reset();

    /**
     * Adds a student to the collection of students.
     * @param studentGetter the student to add
     */
    void addStudent(homeAwayApp.student.StudentGetter studentGetter);

    /**
     * Removes a student from the collection of students.
     * @param studentGetter the student to remove
     */
    void removeStudent(homeAwayApp.student.StudentGetter studentGetter);
}
