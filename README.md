# Proyek 2 ASD - Sistem Rekomendasi Pengambilan Mata Kuliah

Program rekomendasi pengambilan mata kuliah wajib berdasarkan peta kurikulum FILKOM, daftar prasyarat, serta riwayat kelulusan mahasiswa.

## Anggota Kelompok
| No | Nama | NIM |
|----|------|-----|
| 1 | Faeyza Safa Izz Deyardi | 255150207111060 |
| 2 | Syamil Fadhlurrahman | 255150200111067 |
| 3 | Naufal Insan Annafi | 255150200111075 |

## Struktur Data & Algoritma (Buatan Sendiri)
Seluruh struktur data dibangun dari nol tanpa menggunakan Java Collections Framework (`ArrayList`, `HashMap`, `TreeMap`, `TreeSet`, dsb.):
- **`LinkedList<T>` & `Node<T>`**: Singly linked list generik dengan pointer `head` dan `tail` ($O(1)$ append), pencarian linear, serta **Merge Sort** ($O(n \log n)$, stabil).
- **`CourseGraph` (DAG / Directed Acyclic Graph)**: Representasi graf prasyarat berbasis *adjacency list* menggunakan `LinkedList<Course>` buatan sendiri (menyimpan sisi masuk `prasyarat` dan sisi keluar `lanjutan`).
- **DFS (Depth-First Search)**:
  - *Post-order DFS* pada sisi `prasyarat` untuk menelusuri seluruh prasyarat berantai (transitif) yang belum lulus, terurut dari prasyarat paling awal ke prasyarat langsung.
  - *DFS* pada sisi `lanjutan` untuk menghitung jumlah total MK lanjutan (*total dependents*) sebagai prioritas rekomendasi.

## Struktur Proyek
```
.
├── src/
│   ├── Node.java          # Node untuk singly linked list
│   ├── Cmp.java           # Interface pembanding untuk merge sort
│   ├── LinkedList.java    # Singly linked list generik + merge sort
│   ├── Course.java        # Simpul (vertex) mata kuliah pada graf
│   ├── CourseGraph.java   # Directed Acyclic Graph (adjacency list + DFS)
│   ├── PrioritasMK.java   # Kriteria prioritas pengurutan rekomendasi MK
│   └── Main.java          # Titik masuk program (CLI)
├── data/
│   ├── matakuliah.csv
│   ├── prasyarat.csv
│   ├── mhs_01.txt
│   ├── mhs_02.txt
│   └── mhs_03.txt
├── Laporan.pdf
└── README.md
```

## Cara Menjalankan
```bash
javac -d out src/*.java
java -cp out Main data/matakuliah.csv data/prasyarat.csv data/mhs_01.txt
```

Ganti `data/mhs_01.txt` dengan `data/mhs_02.txt` atau `data/mhs_03.txt` untuk menguji kasus mahasiswa lainnya. Detail struktur data dan algoritma dijelaskan lengkap pada [Laporan.pdf](Laporan.pdf).
