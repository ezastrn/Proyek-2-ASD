/**
 * Interface pembanding antar elemen untuk pengurutan (merge sort).
 */
public interface Cmp<T> {
    int compare(T a, T b);
}
