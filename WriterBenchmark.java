import java.io.*;// Enthält die Methoden zur Erstellung des FileWriter und BufferdWriter
import java.util.concurrent.TimeUnit;// Enthält die Mehtoden zur Speicherplatz- und Laufzeitmessung

public class WriterBenchmark {// Methode zum Benchmark und FileWriter gegenüber BufferedWriter
    private static final int Datensatz_Groesse = 500;// Anzahl  der zu schreibenden Datensätze
    private static final String FileWriter = "file_writer.txt";// Dateiname unter dem die Datei des FileWriters gespeichert wird
    private static final String BufferedWriter = "buffered_writer.txt";// Dateiname unter dem die Daei des Buffered Writes gespeichert wird

    public static void main(String[] args) {
        compareWriters();// Vergleich der beiden Writer-Implementierungen
    }

    private static void compareWriters() {
        System.out.println("Speicherplatz vor dem Test: " + Speicherplatznutzung()

                + " Bytes");// Aktueller Speicherplatz vor dem Test

        long fileWriter_Zeit= Laufzeitkomplexitaet(new FileWriterSupplier(), FileWriter);// Laufzeitkomplexität des FileWriters
        long fileWriter_Speicher = Speicherplatznutzung();// Speicherplatznutzung des FileWriters

        long bufferedWriter_Zeit = Laufzeitkomplexitaet(new BufferedWriterSupplier(), BufferedWriter);// Laufzeitkomplexität des BufferedWriters
        long bufferedWriter_Speicher = Speicherplatznutzung();// Speicherplatznutzung des BufferedWriters

        System.out.println("\nLaufzeitvergleich:");// Vergleich der Laufzeiten der beiden Writer-Implementierungen
        System.out.println("FileWriter: " + fileWriter_Zeit + " ms");// Laufzeit des FileWriters in Millisekunden

        
        System.out.println("BufferedWriter: " + bufferedWriter_Zeit + " ms");// Laufzeit des BufferedWriters in Millisekunden

        System.out.println("\nSpeicherverbrauch waehrend des Schreibens:");// Vergleich des Speicherverbrauchs während des Schreibens
        System.out.println("FileWriter: " + fileWriter_Speicher + " Bytes");// Speicherplatznutzung des FileWriters
        System.out.println("BufferedWriter: " + bufferedWriter_Speicher + " Bytes");// Speicherplatznutzung des BufferedWriters

        System.out.println("\nSpeicherplatzvergleich der Dateien:");// Vergleich der Dateigrößen der beiden Writer-Implementierungen
        System.out.println("FileWriter-Dateigroesse: " + Speicherkomplexitaet(FileWriter) + " Bytes");// Dateigröße des FileWriters
        System.out.println("BufferedWriter-Dateigroesse: " + Speicherkomplexitaet(BufferedWriter) + " Bytes");// Dateigröße des BufferedWriters
    }

    private static long Laufzeitkomplexitaet(WriterSupplier supplier, String fileName) {
        long Startzeit = System.nanoTime();// Startzeit in Nanosekunden

        try (Writer writer = supplier.getWriter(fileName)) {// Erstellen eines Writers mittels der übergebenen Supplier-Schnittstelle
            for (int i = 0; i < Datensatz_Groesse; i++) {// Schleife zum Schreiben von Datensätzen
                writer.write("Datensatz " + i + "\n");// Schreiben von Datensätzen in die Datei
            }// Schreiben von Datensätzen in die Datei mittels des Writers
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der Datei: " + e.getMessage());// Fehlerbehandlung für IO-Ausnahmen
        }


        long Endzeit = System.nanoTime();// Endzeit in Nanosekunden
        return TimeUnit.NANOSECONDS.toMillis(Endzeit - Startzeit);// Berechnung der Laufzeit in Millisekunden
    }

    private static long Speicherkomplexitaet(String Dateiname) {
        File file = new File(Dateiname);// Erstellen eines File-Objekts mit dem Dateinamen
        return file.exists() ? file.length() : -1;// Rückgabe der Dateigröße in Bytes, falls die Datei existiert, sonst -1
    }

    private static long Speicherplatznutzung() {
        Runtime runtime = Runtime.getRuntime();// Zugriff auf die Laufzeitumgebung
        return runtime.totalMemory() - runtime.freeMemory();// Berechnung des aktuell genutzten Speichers in Bytes
    }

    interface WriterSupplier {
        Writer getWriter(String Dateiname) throws IOException;// Schnittstelle für Writer-Lieferanten
    }

    static class FileWriterSupplier implements WriterSupplier {
        @Override
        public Writer getWriter(String Dateiname) throws IOException {
            return new FileWriter(Dateiname);// Erstellen eines FileWriter-Objekts mit dem Dateinamen
        }
    }

    static class BufferedWriterSupplier implements WriterSupplier {// Schnittstelle für BufferedWriter-Lieferanten
        @Override
        public Writer getWriter(String Dateiname) throws IOException {// Methode zum Erstellen eines BufferedWriter
            return new BufferedWriter(new FileWriter(Dateiname));// Erstellen eines BufferedWriter-Objekts mit einem FileWriter
        }
    }
}
