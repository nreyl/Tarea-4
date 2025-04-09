import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OptimizacionVias {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java OptimizacionVias <archivo_entrada>");
            return;
        }
        
        String archivoEntrada = args[0];
        int numIntersecciones = 0;
        List<Arista> aristas = new ArrayList<>();
        Map<Par, Integer> costosConversion = new HashMap<>();
        
        try {
            // Leer el archivo de entrada
            BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
            String linea;
            
            // Primera línea: número de intersecciones y número de vías
            linea = br.readLine();
            String[] partes = linea.trim().split("\\s+");
            numIntersecciones = Integer.parseInt(partes[0]);
            int numVias = Integer.parseInt(partes[1]);
            
            // Leer las vías y sus costos de conversión
            for (int i = 0; i < numVias; i++) {
                linea = br.readLine();
                partes = linea.trim().split("\\s+");
                int origen = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);
                int costo = Integer.parseInt(partes[2]);
                
                // Guardar la arista y su costo de conversión
                Par par = new Par(origen, destino);
                costosConversion.put(par, costo);
                aristas.add(new Arista(origen, destino, costo));
            }
            
            br.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("Error en el formato del archivo: " + e.getMessage());
            return;
        }
        
        // Crear un grafo no dirigido donde cada arista tiene el costo de conversión
        List<Arista> aristasNoDir = new ArrayList<>();
        Map<Par, Boolean> existeDobleVia = new HashMap<>();
        
        // Verificar si ya existen dobles vías
        for (Arista arista : aristas) {
            Par directa = new Par(arista.origen, arista.destino);
            Par inversa = new Par(arista.destino, arista.origen);
            
            if (costosConversion.containsKey(inversa)) {
                // Ya existe una doble vía, el costo es 0
                existeDobleVia.put(directa, true);
                existeDobleVia.put(inversa, true);
            } else {
                existeDobleVia.put(directa, false);
            }
        }
        
        // Crear las aristas para el grafo no dirigido
        for (Arista arista : aristas) {
            Par directa = new Par(arista.origen, arista.destino);
            if (!existeDobleVia.getOrDefault(directa, false)) {
                // Si no es una doble vía, agregar con su costo de conversión
                aristasNoDir.add(new Arista(arista.origen, arista.destino, arista.costo));
            } else {
                // Si ya es una doble vía, el costo es 0
                aristasNoDir.add(new Arista(arista.origen, arista.destino, 0));
            }
        }
        
        // Aplicar el algoritmo de Kruskal para encontrar el MST
        List<Arista> mst = kruskal(aristasNoDir, numIntersecciones);
        
        // Calcular el costo total y las vías a convertir
        int costoTotal = 0;
        List<Par> viasAConvertir = new ArrayList<>();
        
        for (Arista arista : mst) {
            Par directa = new Par(arista.origen, arista.destino);
            Par inversa = new Par(arista.destino, arista.origen);
            
            if (!existeDobleVia.getOrDefault(directa, false) && arista.costo > 0) {
                costoTotal += arista.costo;
                
                // Determinar cuál dirección necesita ser convertida
                if (costosConversion.containsKey(directa)) {
                    viasAConvertir.add(directa);
                } else if (costosConversion.containsKey(inversa)) {
                    viasAConvertir.add(inversa);
                }
            }
        }
        
        // Imprimir resultados
        System.out.println("Costo total de conversión: " + costoTotal);
        System.out.println("Vías a convertir en doble vía:");
        
        for (Par via : viasAConvertir) {
            System.out.println(via.primero + " -> " + via.segundo);
        }
    }
    
    /**
     * Implementación del algoritmo de Kruskal para encontrar el MST.
     * 
     * @param aristas Lista de aristas con sus costos
     * @param numVertices Número de vértices en el grafo
     * @return Lista de aristas que forman el MST
     */
    private static List<Arista> kruskal(List<Arista> aristas, int numVertices) {
        // Ordenar las aristas por costo
        Collections.sort(aristas);
        
        // Inicializar el conjunto disjunto
        DisjointSet ds = new DisjointSet(numVertices);
        
        List<Arista> mst = new ArrayList<>();
        
        for (Arista arista : aristas) {
            int raizOrigen = ds.find(arista.origen);
            int raizDestino = ds.find(arista.destino);
            
            // Si incluir esta arista no forma un ciclo, agregarla al MST
            if (raizOrigen != raizDestino) {
                mst.add(arista);
                ds.union(raizOrigen, raizDestino);
            }
            
            // Si ya tenemos n-1 aristas, hemos completado el MST
            if (mst.size() == numVertices - 1) {
                break;
            }
        }
        
        return mst;
    }
    
    /**
     * Clase para representar una arista con su costo.
     */
    static class Arista implements Comparable<Arista> {
        int origen;
        int destino;
        int costo;
        
        public Arista(int origen, int destino, int costo) {
            this.origen = origen;
            this.destino = destino;
            this.costo = costo;
        }
        
        @Override
        public int compareTo(Arista otra) {
            return Integer.compare(this.costo, otra.costo);
        }
    }
    
    /**
     * Clase para representar un par ordenado (origen, destino).
     */
    static class Par {
        int primero;
        int segundo;
        
        public Par(int primero, int segundo) {
            this.primero = primero;
            this.segundo = segundo;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Par par = (Par) o;
            return primero == par.primero && segundo == par.segundo;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(primero, segundo);
        }
    }
    
    /**
     * Implementación de la estructura de datos Disjoint Set (Union-Find).
     */
    static class DisjointSet {
        int[] padre;
        int[] rango;
        
        public DisjointSet(int n) {
            padre = new int[n];
            rango = new int[n];
            
            // Inicializar: cada elemento es su propio padre
            for (int i = 0; i < n; i++) {
                padre[i] = i;
            }
        }
        
        /**
         * Encuentra el representante (raíz) del conjunto al que pertenece x.
         */
        public int find(int x) {
            if (padre[x] != x) {
                padre[x] = find(padre[x]); // Compresión de camino
            }
            return padre[x];
        }
        
        /**
         * Une los conjuntos que contienen x e y.
         */
        public void union(int x, int y) {
            int raizX = find(x);
            int raizY = find(y);
            
            if (raizX == raizY) return;
            
            // Unión por rango
            if (rango[raizX] < rango[raizY]) {
                padre[raizX] = raizY;
            } else if (rango[raizX] > rango[raizY]) {
                padre[raizY] = raizX;
            } else {
                padre[raizY] = raizX;
                rango[raizX]++;
            }
        }
    }
}