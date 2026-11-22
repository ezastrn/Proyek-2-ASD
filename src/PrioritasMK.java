/**
 * Pembanding prioritas rekomendasi MK yang dapat diambil:
 * 1. Total MK lanjutan (dependents) terbanyak (MK pembuka jalur prasyarat diprioritaskan)
 * 2. Semester rencana paling awal
 * 3. SKS terbesar
 * 4. Urutan asli pada matakuliah.csv
 */
public class PrioritasMK implements Cmp<Course> {
    @Override
    public int compare(Course a, Course b) {
        if (a.totalDependents != b.totalDependents) return b.totalDependents - a.totalDependents;
        if (a.semester != b.semester) return a.semester - b.semester;
        if (a.sks != b.sks) return b.sks - a.sks;
        return a.order - b.order;
    }
}
