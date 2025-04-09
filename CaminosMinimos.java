import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CaminosMinimos {
    private static final int INFINITO = Integer.MAX_VALUE;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java CaminosMinimos <archivo_entrada>");
            return;
        }

        String archivoEntrada = args[0];
        List<Arista> aristas = new ArrayList<>();
        int numVertices = 0;

        try {
            // Leer el archivo de entrada
            BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                if (partes.length == 3) {
                    int origen = Integer.parseInt(partes[0]);
                    int destino = Integer.parseInt(partes[1]);
                    int peso = Integer.parseInt(partes[2]);
                    aristas.add(new Arista(origen, destino, peso));
                    numVertices = Math.max(numVertices, Math.max(origen, destino) + 1);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("Error en el formato del archivo: " + e.getMessage());
            return;
        }

        // Crear la matriz de adyacencia
        int[][] grafo = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(grafo[i], INFINITO);
            grafo[i][i] = 0; // Distancia a sí mismo es 0
        }

        for (Arista arista : aristas) {
            grafo[arista.origen][arista.destino] = arista.peso;
        }

        System.out.println("Número de vértices: " + numVertices);
        System.out.println("Número de aristas: " + aristas.size());

        // Ejecutar y medir tiempo de Dijkstra para todos los vértices
        long inicioDijkstra = System.nanoTime();
        int[][] resultadoDijkstra = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            resultadoDijkstra[i] = dijkstra(grafo, i, numVertices);
        }
        long finDijkstra = System.nanoTime();
        double tiempoDijkstra = (finDijkstra - inicioDijkstra) / 1_000_000.0;

        // Ejecutar y medir tiempo de Bellman-Ford para todos los vértices
        long inicioBellmanFord = System.nanoTime();
        int[][] resultadoBellmanFord = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            resultadoBellmanFord[i] = bellmanFord(grafo, i, numVertices);
        }
        long finBellmanFord = System.nanoTime();
        double tiempoBellmanFord = (finBellmanFord - inicioBellmanFord) / 1_000_000.0;

        // Ejecutar y medir tiempo de Floyd-Warshall
        long inicioFloydWarshall = System.nanoTime();
        int[][] resultadoFloydWarshall = floydWarshall(grafo, numVertices);
        long finFloydWarshall = System.nanoTime();
        double tiempoFloydWarshall = (finFloydWarshall - inicioFloydWarshall) / 1_000_000.0;

        // Imprimir resultados
        System.out.println("\nMatriz de costos mínimos (Dijkstra):");
        imprimirMatriz(resultadoDijkstra);

        System.out.println("\nMatriz de costos mínimos (Bellman-Ford):");
        imprimirMatriz(resultadoBellmanFord);

        System.out.println("\nMatriz de costos mínimos (Floyd-Warshall):");
        imprimirMatriz(resultadoFloydWarshall);

        // Verificar que los resultados sean iguales
        boolean sonIguales = compararMatrices(resultadoDijkstra, resultadoBellmanFord) && 
                             compararMatrices(resultadoDijkstra, resultadoFloydWarshall);
        
        System.out.println("\nLos resultados de los tres algoritmos " + 
                          (sonIguales ? "son iguales." : "son diferentes."));

        // Imprimir tiempos de ejecución
        System.out.println("\nTiempos de ejecución:");
        System.out.printf("Dijkstra: %.6f ms\n", tiempoDijkstra);
        System.out.printf("Bellman-Ford: %.6f ms\n", tiempoBellmanFord);
        System.out.printf("Floyd-Warshall: %.6f ms\n", tiempoFloydWarshall);
    }

    // Algoritmo de Dijkstra para un vértice origen
    private static int[] dijkstra(int[][] grafo, int origen, int numVertices) {
        int[] distancias = new int[numVertices];
        boolean[] visitados = new boolean[numVertices];
        
        Arrays.fill(distancias, INFINITO);
        distancias[origen] = 0;
        
        for (int i = 0; i < numVertices - 1; i++) {
            int u = obtenerVerticeMinimo(distancias, visitados, numVertices);
            visitados[u] = true;
            
            for (int v = 0; v < numVertices; v++) {
                if (!visitados[v] && grafo[u][v] != INFINITO && 
                    distancias[u] != INFINITO && 
                    distancias[u] + grafo[u][v] < distancias[v]) {
                    distancias[v] = distancias[u] + grafo[u][v];
                }
            }
        }
        
        return distancias;
    }
    
    // Método auxiliar para Dijkstra
    private static int obtenerVerticeMinimo(int[] distancias, boolean[] visitados, int numVertices) {
        int minimo = INFINITO;
        int indiceMinimo = -1;
        
        for (int v = 0; v < numVertices; v++) {
            if (!visitados[v] && distancias[v] <= minimo) {
                minimo = distancias[v];
                indiceMinimo = v;
            }
        }
        
        return indiceMinimo;
    }
    
    // Algoritmo de Bellman-Ford para un vértice origen
    private static int[] bellmanFord(int[][] grafo, int origen, int numVertices) {
        int[] distancias = new int[numVertices];
        Arrays.fill(distancias, INFINITO);
        distancias[origen] = 0;
        
        // Convertir la matriz de adyacencia a una lista de aristas para Bellman-Ford
        List<Arista> aristas = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && grafo[i][j] != INFINITO) {
                    aristas.add(new Arista(i, j, grafo[i][j]));
                }
            }
        }
        
        // Relajación de aristas
        for (int i = 1; i < numVertices; i++) {
            for (Arista arista : aristas) {
                int u = arista.origen;
                int v = arista.destino;
                int peso = arista.peso;
                
                if (distancias[u] != INFINITO && distancias[u] + peso < distancias[v]) {
                    distancias[v] = distancias[u] + peso;
                }
            }
        }
        
        // Verificar ciclos negativos (no debería haber en este problema)
        for (Arista arista : aristas) {
            int u = arista.origen;
            int v = arista.destino;
            int peso = arista.peso;
            
            if (distancias[u] != INFINITO && distancias[u] + peso < distancias[v]) {
                System.err.println("El grafo contiene un ciclo de peso negativo");
                break;
            }
        }
        
        return distancias;
    }
    
    // Algoritmo de Floyd-Warshall
    private static int[][] floydWarshall(int[][] grafo, int numVertices) {
        int[][] distancias = new int[numVertices][numVertices];
        
        // Inicializar matriz de distancias
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                distancias[i][j] = grafo[i][j];
            }
        }
        
        // Calcular caminos mínimos
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO &&
                        distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }
        
        return distancias;
    }
    
    // Método para imprimir una matriz
    private static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == INFINITO) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(matriz[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
    
    // Método para comparar dos matrices
    private static boolean compararMatrices(int[][] matriz1, int[][] matriz2) {
        if (matriz1.length != matriz2.length) return false;
        
        for (int i = 0; i < matriz1.length; i++) {
            if (matriz1[i].length != matriz2[i].length) return false;
            for (int j = 0; j < matriz1[i].length; j++) {
                if (matriz1[i][j] != matriz2[i][j]) return false;
            }
        }
        
        return true;
    }
    
    // Clase para representar una arista
    static class Arista {
        int origen;
        int destino;
        int peso;
        
        public Arista(int origen, int destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }
}