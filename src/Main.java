import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Sistem Rekomendasi Pengambilan Mata Kuliah (Proyek 2 ASD).
 * Input: matakuliah.csv, prasyarat.csv, dan mhs_xx.txt.
 */
public class Main {

    static String stripBom(String s) {
        if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }

    static LinkedList<String[]> readCsv(String path) throws IOException {
        LinkedList<String[]> rows = new LinkedList<>();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line = r.readLine(); // lewati baris header
            while ((line = r.readLine()) != null) {
                line = stripBom(line).trim();
                if (!line.isEmpty()) {
                    rows.add(line.split(";"));
                }
            }
        }
        return rows;
    }

    static String pad(String s, int w) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < w) sb.append(' ');
        return sb.toString();
    }

    static String joinList(LinkedList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (Node<String> n = list.head; n != null; n = n.next) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(n.data);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Pemakaian: java Main <matakuliah.csv> <prasyarat.csv> <mhs_xx.txt>");
            return;
        }

        CourseGraph graph = new CourseGraph();

        // 1. Baca daftar mata kuliah (simpul graf)
        int idx = 0;
        for (Node<String[]> n = readCsv(args[0]).head; n != null; n = n.next) {
            String[] c = n.data;
            if (c.length < 5) continue;
            graph.addCourse(new Course(
                    c[0].trim(),
                    c[1].trim(),
                    Integer.parseInt(c[2].trim()),
                    Integer.parseInt(c[3].trim()),
                    c[4].trim(),
                    idx++
            ));
        }

        // 2. Baca relasi prasyarat (sisi berarah graf)
        for (Node<String[]> n = readCsv(args[1]).head; n != null; n = n.next) {
            if (n.data.length < 2) continue;
            graph.addPrerequisite(n.data[0].trim(), n.data[1].trim());
        }
        graph.computeTotalDependents();

        // 3. Baca data mahasiswa (semester berikutnya dan daftar MK lulus)
        String semBerikut = "";
        LinkedList<String> lulus = new LinkedList<>();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(args[2]), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = stripBom(line).trim();
                if (line.startsWith("semester_berikutnya=")) {
                    semBerikut = line.substring("semester_berikutnya=".length()).trim();
                } else if (line.startsWith("lulus=")) {
                    String isi = line.substring("lulus=".length()).trim();
                    if (!isi.isEmpty()) {
                        for (String k : isi.split(",")) {
                            String kode = k.trim();
                            if (!kode.isEmpty() && !lulus.contains(kode)) {
                                lulus.add(kode);
                            }
                        }
                    }
                }
            }
        }

        // 4. Klasifikasi MK yang bisa diambil dan belum bisa diambil
        LinkedList<Course> bisa = new LinkedList<>();
        LinkedList<Course> belum = new LinkedList<>();
        LinkedList<String> alasan = new LinkedList<>();

        for (Node<Course> n = graph.courses.head; n != null; n = n.next) {
            Course c = n.data;
            if (lulus.contains(c.kode) || !c.offered(semBerikut)) continue;

            if (c.kode.equals("SKR")) {
                LinkedList<String> kurang = graph.getMissingForSkripsi(lulus);
                if (!kurang.isEmpty()) {
                    belum.add(c);
                    alasan.add("belum lulus semua MK wajib (" + kurang.size()
                            + " MK belum lulus: " + joinList(kurang) + ")");
                } else {
                    bisa.add(c);
                }
            } else {
                LinkedList<String> kurang = graph.getMissingPrerequisites(c, lulus);
                if (!kurang.isEmpty()) {
                    belum.add(c);
                    alasan.add("belum lulus " + joinList(kurang));
                } else {
                    bisa.add(c);
                }
            }
        }

        // 5. Urutkan daftar MK yang bisa diambil dengan merge sort buatan sendiri
        bisa.sort(new PrioritasMK());

        // 6. Cetak hasil
        System.out.println("Semester berikutnya: " + semBerikut);
        System.out.println("Daftar MK yang bisa diambil:");
        int i = 1;
        int totalSks = 0;
        for (Node<Course> n = bisa.head; n != null; n = n.next, i++) {
            Course c = n.data;
            System.out.printf("%2d. %s%s%d SKS%n", i, pad(c.kode, 6), pad(c.nama, 48), c.sks);
            totalSks += c.sks;
        }
        if (bisa.isEmpty()) {
            System.out.println("(tidak ada)");
        }
        System.out.println("Total: " + totalSks + " SKS");

        System.out.println();
        System.out.println("Daftar MK yang belum bisa diambil semester depan:");
        Node<String> a = alasan.head;
        for (Node<Course> n = belum.head; n != null; n = n.next, a = a.next) {
            System.out.println(n.data.kode + ": " + a.data);
        }
        if (belum.isEmpty()) {
            System.out.println("(tidak ada)");
        }
    }
}
