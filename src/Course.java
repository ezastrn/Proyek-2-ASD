/**
 * Representasi simpul (vertex) mata kuliah pada graf prasyarat.
 */
public class Course {
    String kode;
    String nama;
    int sks;
    int semester;
    String term;
    int order;

    // Adjacency list berbasis LinkedList buatan sendiri:
    // prasyarat: daftar MK yang harus lulus sebelum MK ini (incoming edges)
    // lanjutan : daftar MK yang membutuhkan MK ini sebagai prasyarat (outgoing edges)
    LinkedList<Course> prasyarat = new LinkedList<>();
    LinkedList<Course> lanjutan = new LinkedList<>();
    int totalDependents;

    public Course(String kode, String nama, int sks, int semester, String term, int order) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.term = term;
        this.order = order;
    }

    public boolean offered(String sem) {
        return term.equalsIgnoreCase("Keduanya") || term.equalsIgnoreCase(sem);
    }
}
