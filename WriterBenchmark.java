import java.io.*;// Enthält die Methoden zur Erstellung des FileWriter und BufferdWriter
import java.util.concurrent.TimeUnit;// Enthält die Methoden zur Speicherplatz- und Laufzeitmessung

public class WriterBenchmark {// Methode zum Benchmark und FileWriter gegenüber BufferedWriter
    private static final int Datensatz_Groesse = 500;// Anzahl der zu schreibenden Datensätze
    private static final String FileWriter = "file_writer.txt";// Dateiname, unter dem die Datei des FileWriters gespeichert wird
    private static final String BufferedWriter = "buffered_writer.txt";// Dateiname, unter dem die Datei des Buffered Writes gespeichert wird

    public static void main(String[] args) {
        compareWriters();// Aufruf der Methode zum Vergleich des FileWriters mit dem BufferedWriter
    }

    private static void compareWriters() {
        System.out.println("Speicherplatz vor dem Test: " + Speicherplatznutzung()+ " Bytes");// Ausgabe des genutzen Speichers vor der Ausführung 

        long fileWriter_Zeit= Laufzeitkomplexitaet(new FileWriterSupplier(), FileWriter)// Aufruf der Mehtode zur Messung der 
            //Laufzeitkomplexität des FileWrites
        long fileWriter_Speicher = Speicherplatznutzung();// Aufruf der Mehtode zur Messung des  Speicherplatznutzunng des FileWriters 

        long bufferedWriter_Zeit = Laufzeitkomplexitaet(new BufferedWriterSupplier(), BufferedWriter);// Aufruf der Mehode zur Messsung der 
        //Laufzeitkomplexität des Bufferd Writers
        long bufferedWriter_Speicher = Speicherplatznutzung();// Aufruf der Methode zur Messsung der Speicherplatznutzung des BufferdWriter

        System.out.println("\nLaufzeitvergleich:");// Anküundigung des Vergleiches des FileWriters mit dem Buffered Writer 
        System.out.println("FileWriter: " + fileWriter_Zeit + " ms");// Ausgabe der Laufzeit des FileWriters in Millisekunden

        
        System.out.println("BufferedWriter: " + bufferedWriter_Zeit + " ms");// Ausgabe der Laufzeit des BufferdWriters in Mllisekunden

        System.out.println("\nSpeicherverbrauch waehrend des Schreibens:");// Ausgabe des Speicherplatzkomplexität während des schriebens der Datei
        System.out.println("FileWriter: " + fileWriter_Speicher + " Bytes");// Ausgabe der Speiicherplatznuntzung des 
        //FileWriter während des Schreibens der Datei
        System.out.println("BufferedWriter: " + bufferedWriter_Speicher + " Bytes");// Ausagbe der Speidcherplatznutzung des 
        //BufferedWriters während des Schreibens der Daei

        System.out.println("\nSpeicherplatzvergleich der Dateien:");// Ankündigung des Vergleiches der Dateigrößen von FileWriter und Buffered Writer
        System.out.println("FileWriter-Dateigroesse: " + Speicherkomplexitaet(FileWriter) + " Bytes");// Ausgabe der Dateigröße des FileWriter
        System.out.println("BufferedWriter-Dateigroesse: " + Speicherkomplexitaet(BufferedWriter) + " Bytes");// Ausgabe der Dateigröße 
        //des Bufferd Writer
    }

    private static long Laufzeitkomplexitaet(WriterSupplier supplier, String fileName) {// Methode zur Messung der Laufzeitkomplexität 
        //des FileWriter und BufferedWriter
        long Startzeit = System.nanoTime();// Abruf und Speicherung der Zeit in Nanosekunden vor Ausführung des Codes

        try (Writer writer = supplier.getWriter(fileName)) {// Erstellung eines FileWriter auf der Datei mittels des übergebenen Suppliers
            for (int i = 0; i < Datensatz_Groesse; i++) {// So lange die geforderte Länge der Datei noch nicht errreicht wurde. 
                writer.write("Datensatz " + i + "\n");// Schreibe weitere Datensätze in die Datei
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der Datei: " + e.getMessage());// Falls die Dateri nicht erstellt werden kann, 
            //gebe eine Fehlermeldung aus
        }


        long Endzeit = System.nanoTime();// Nach Erfolgreichem Schreibprozess rufe die Zeit in Nanosekunden ab, und speichere diese.
        return TimeUnit.NANOSECONDS.toMillis(Endzeit - Startzeit);// Berechne aus der Startzeit und Endzeit in Nanosekunden 
        //die Laufzeit in Millisekunden
    }

    private static long Speicherkomplexitaet(String Dateiname) {// Mehtode zur Berechnung der Dateigröße
        File file = new File(Dateiname);// Erstelle eine neue Datei mit dem Dateinamen
        return file.exists() ? file.length() : -1;// Gebe die Größé der Datei aus, falls diese erfolgreich generiert wurde, sonst -1
    }

    private static long Speicherplatznutzung() {// Mehode zur Berechnung der Speicherplatzkomplexität 
        Runtime runtime = Runtime.getRuntime();// Rufe die Laufzeit von deie
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
