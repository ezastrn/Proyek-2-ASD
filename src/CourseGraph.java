/**
 * Struktur data Directed Acyclic Graph (DAG) buatan sendiri untuk memodelkan
 * relasi prasyarat antar mata kuliah menggunakan adjacency list berbasis LinkedList.
 */
public class CourseGraph {
    LinkedList<Course> courses = new LinkedList<>();

    public void addCourse(Course c) {
        if (find(c.kode) == null) {
            courses.add(c);
        }
    }

    public Course find(String kode) {
        for (Node<Course> n = courses.head; n != null; n = n.next) {
            if (n.data.kode.equals(kode)) return n.data;
        }
        return null;
    }

    /**
     * Menambahkan sisi berarah: pre -> mk (mk membutuhkan pre).
     */
    public void addPrerequisite(String kodePrasyarat, String kodeMK) {
        Course pre = find(kodePrasyarat);
        Course mk = find(kodeMK);
        if (pre == null || mk == null) return;
        if (!mk.prasyarat.contains(pre)) {
            mk.prasyarat.add(pre);
        }
        if (!pre.lanjutan.contains(mk)) {
            pre.lanjutan.add(mk);
        }
    }

    /**
     * Menghitung jumlah total MK lanjutan (langsung maupun tidak langsung)
     * yang bergantung pada tiap MK menggunakan DFS pada sisi keluar (lanjutan).
     */
    public void computeTotalDependents() {
        for (Node<Course> n = courses.head; n != null; n = n.next) {
            LinkedList<String> visited = new LinkedList<>();
            dfsDependents(n.data, visited);
            n.data.totalDependents = visited.size();
        }
    }

    private void dfsDependents(Course c, LinkedList<String> visited) {
        for (Node<Course> next = c.lanjutan.head; next != null; next = next.next) {
            Course dep = next.data;
            if (!visited.contains(dep.kode)) {
                visited.add(dep.kode);
                dfsDependents(dep, visited);
            }
        }
    }

    /**
     * Mengumpulkan seluruh MK prasyarat (langsung maupun berantai/transitif)
     * yang belum lulus menggunakan DFS post-order, sehingga urutan hasil dimulai
     * dari prasyarat paling awal hingga prasyarat langsung.
     */
    public LinkedList<String> getMissingPrerequisites(Course c, LinkedList<String> lulus) {
        LinkedList<String> visited = new LinkedList<>();
        LinkedList<String> missing = new LinkedList<>();
        dfsMissing(c, lulus, visited, missing);
        return missing;
    }

    private void dfsMissing(Course c, LinkedList<String> lulus,
                            LinkedList<String> visited, LinkedList<String> missing) {
        for (Node<Course> p = c.prasyarat.head; p != null; p = p.next) {
            Course pre = p.data;
            if (visited.contains(pre.kode)) continue;
            visited.add(pre.kode);
            if (!lulus.contains(pre.kode)) {
                dfsMissing(pre, lulus, visited, missing);
                missing.add(pre.kode);
            }
        }
    }

    /**
     * Persyaratan khusus Skripsi (SKR): semua MK wajib selain SKR harus sudah lulus.
     */
    public LinkedList<String> getMissingForSkripsi(LinkedList<String> lulus) {
        LinkedList<String> missing = new LinkedList<>();
        for (Node<Course> n = courses.head; n != null; n = n.next) {
            String kode = n.data.kode;
            if (!kode.equals("SKR") && !lulus.contains(kode)) {
                missing.add(kode);
            }
        }
        return missing;
    }
}
