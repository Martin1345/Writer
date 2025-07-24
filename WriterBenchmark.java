import java.io.*;// Entält die Methoden zur Erstelllung von FileWriter und BufferedWriter
import java.util.concurrent.TimeUnit;//Enthält die Methoden zur Speicherplatznutzunng und Laufzeitkomplexität

public class WriterBenchmark {//Klasse zum Benchmarking von FileWriter und BufferedWriter
    private static final int Datensatz_Groesse = 500;// Anzahl der Datensätze die jeweils in die Datei geschrieben werden
    private static final String FileWriter = "filewriter.txt";// Dateiname für die Speicherung mitttels FileWriter
    private static final String BufferedWriter = "bufferedriter.txt";// Dateiname für die Speicherung mitttels BufferedWriter

    public static void main(String[] args) {
        vergleiche_Writer();// Methode zum Vergleich des FileWriters und BufferedWriters aufrufen
    }

    private static void vergleiche_Writer() {
        System.out.println("Speicherplatz vor dem Test: " + Speicherplatznutzung() + " Bytes");// Ausgabe des aktuell genutzten Speichers vor dem Test

        long fileWriter_Zeit= Laufzeitkomplexitaet(new FileWriterSupplier(), FileWriter);// Aufruf der Methode zur Laufzeitkomplexität des FileWriters
        long fileWriter_Speicher = Speicherplatznutzung();// Aufruf der Methode zur Speicherplatznutzung des FileWriters

        long bufferedWriter_Zeit = Laufzeitkomplexitaet(new BufferedWriterSupplier(), BufferedWriter);// Laufzeitkomplexität des BufferedWriters
        long bufferedWriter_Speicher = Speicherplatznutzung();// Speicherplatznutzung des BufferedWriters

        System.out.println("\nLaufzeitvergleich:");// Ankündigung des Laufzeitvergleichers von FileWriter und BufferedWriter
        System.out.println("FileWriter: " + fileWriter_Zeit + " ms");// Ausgabe der Laufzeit des FileWriters in Millisekunden

        System.out.println("BufferedWriter: " + bufferedWriter_Zeit + " ms");// Ausgabe der Laufzeit des BufferedWriters in Millisekunden

        System.out.println("\nSpeicherverbrauch waehrend des Schreibens:");// Ankündigung des Speicherverbrauchs während des Schreibens
        System.out.println("FileWriter: " + fileWriter_Speicher + " Bytes");// Ausgabe des Speicherplatzverbrauches des FileWriters während des Schreibens
        System.out.println("BufferedWriter: " + bufferedWriter_Speicher + " Bytes");// Ausgabe des Speicherplatzverbrauches des BufferedWriters während des Schreibens

        System.out.println("\nSpeicherplatzvergleich der Dateien:");// Ankündigung des Speicherplatzvergleichs der Dateien
        System.out.println("FileWriter-Dateigroesse: " + Speicherkomplexitaet(FileWriter) + " Bytes");// Ausgabe der Dateigröße des FileWriters
        System.out.println("BufferedWriter-Dateigroesse: " + Speicherkomplexitaet(BufferedWriter) + " Bytes");// Ausgabe der Dateigröße des BufferedWriters
    }

    private static long Laufzeitkomplexitaet(WriterSupplier supplier, String fileName) {// Methode zur Berechnung der Laufzeitkomplexität
        long Startzeit = System.nanoTime();// Abruf und Speicherung der Zeit in Nanosekunden vor dem Programmlauf

        try (Writer writer = supplier.getWriter(fileName)) {// Erstellung eines Writers mittels der übergebenen Supplier-Schnittstelle
            for (int i = 0; i < Datensatz_Groesse; i++) {// Schleife zum Schreiben von Datensätzen in die Datei
                writer.write("Datensatz " + i + "\n");// Schreiben von Datensätzen mittels des Writers in die Datei
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der Datei: " + e.getMessage());// Fehlermeldung, falls ein Fehler beim Schreiben der Datei auftritt
        }


        long Endzeit = System.nanoTime();// Abruf der Zeit in Nanosekunden nach dem Programmlauf
        return TimeUnit.NANOSECONDS.toMillis(Endzeit - Startzeit);// Berechnung der Laufzeit in Millisekunden und Rückgabe des Wertes
    }

    private static long Speicherkomplexitaet(String Dateiname) {// Methode zur Berechnung der Dateigröße
        File file = new File(Dateiname);// Erstellung eines File-Objekts mit dem angegebenen Dateinamen
        return file.exists() ? file.length() : -1;// Rückgabe der Dateigröße in Bytes, falls die Datei existiert, ansonsten -1
    }

    private static long Speicherplatznutzung() {
        Runtime runtime = Runtime.getRuntime();// Aufruf der Runtime-Instanz, um den Speicherplatz zu messen
        return runtime.totalMemory() - runtime.freeMemory();// Abruf des aktuell genutzten Speichers vor der Programmausführung in Bytes
    }

    interface WriterSupplier {// Methode zum Erstellen eines Writers, die von verschiedenen Lieferanten implementiert wird
        Writer getWriter(String Dateiname) throws IOException;// Methode zum Abrufen eines Writers mit dem angegebenen Dateinamen
    }

    static class FileWriterSupplier implements WriterSupplier {// Methode zur Erstellung eines FileWriters auf Basis der WriterSupplier-Schnittstelle
        @Override
        public Writer getWriter(String Dateiname) throws IOException {
            return new FileWriter(Dateiname);// Erstellung eines FileWriter-Objekts mit dem Dateinamen
        }
    }

    static class BufferedWriterSupplier implements WriterSupplier {// Methode zur Erstellung eines BufferedWriters auf Basis der WriterSupplier-Schnittstelle
        @Override
        public Writer getWriter(String Dateiname) throws IOException {// Methode zum Abrufen eines BufferedWriters mit dem angegebenen Dateinamen
            return new BufferedWriter(new FileWriter(Dateiname));// Erstellung eines BufferedWriter-Objekts mit dem Dateinamen, das einen FileWriter verwendet
        }
    }
}
