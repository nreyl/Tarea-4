import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DistribucionLibros {
 
    // Infinito para representar capacidad ilimitada
    private static final int INFINITO = Integer.MAX_VALUE;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java DistribucionLibros <archivo_entrada>");
            return;
        }
        
        String archivoEntrada = args[0];
        
        try {
            // Leer el archivo de entrada
            BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
            
            // Leer número de fábricas, librerías, bodegas y camiones
            String[] partes = br.readLine().trim().split("\\s+");
            int numFabricas = Integer.parseInt(partes[0]);
            int numLibrerias = Integer.parseInt(partes[1]);
            int numBodegas = Integer.parseInt(partes[2]);
            int numCamiones = Integer.parseInt(partes[3]);
            
            // Leer capacidades de las bodegas
            int[] capacidadesBodegas = new int[numBodegas];
            partes = br.readLine().trim().split("\\s+");
            for (int i = 0; i < numBodegas; i++) {
                capacidadesBodegas[i] = Integer.parseInt(partes[i]);
            }
            
            // Leer capacidades de los camiones
            int[] capacidadesCamiones = new int[numCamiones];
            partes = br.readLine().trim().split("\\s+");
            for (int i = 0; i < numCamiones; i++) {
                capacidadesCamiones[i] = Integer.parseInt(partes[i]);
            }
            
            // Leer rutas de los camiones
            int[][] rutas = new int[numCamiones][2];
            for (int i = 0; i < numCamiones; i++) {
                partes = br.readLine().trim().split("\\s+");
                int origen = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);
                int tipoOrigen = Integer.parseInt(partes[2]);  // 0: fábrica, 1: bodega
                int tipoDestino = Integer.parseInt(partes[3]); // 0: librería, 1: bodega
                
                // Convertir a índices internos
                if (tipoOrigen == 1) {  // Si es bodega
                    origen = origen + numFabricas;  // Las bodegas se indexan después de las fábricas
                }
                
                if (tipoDestino == 1) {  // Si es bodega
                    destino = destino + numLibrerias;  // Las bodegas se indexan después de las librerías
                }
                
                rutas[i][0] = origen;
                rutas[i][1] = destino;
            }
            
            br.close();
            
            // Construir el grafo de flujo
            int numNodos = 2 + numFabricas + numLibrerias + 2 * numBodegas + numCamiones;
            int[][] capacidad = new int[numNodos][numNodos];
            
            // Índices de los nodos
            int idxFuente = 0;
            int idxSumidero = 1;
            int idxFabricas = 2;
            int idxLibrerias = idxFabricas + numFabricas;
            int idxBodegasEntrada = idxLibrerias + numLibrerias;
            int idxBodegasSalida = idxBodegasEntrada + numBodegas;
            int idxCamiones = idxBodegasSalida + numBodegas;
            
            // Conectar fuente a fábricas
            for (int i = 0; i < numFabricas; i++) {
                capacidad[idxFuente][idxFabricas + i] = INFINITO;
            }
            
            // Conectar librerías a sumidero
            for (int i = 0; i < numLibrerias; i++) {
                capacidad[idxLibrerias + i][idxSumidero] = INFINITO;
            }
            
            // Conectar entradas y salidas de bodegas
            for (int i = 0; i < numBodegas; i++) {
                capacidad[idxBodegasEntrada + i][idxBodegasSalida + i] = capacidadesBodegas[i];
            }
            
            // Conectar camiones según sus rutas
            for (int i = 0; i < numCamiones; i++) {
                int origen = rutas[i][0];
                int destino = rutas[i][1];
                int idxCamion = idxCamiones + i;
                
                // Determinar el tipo de origen y destino
                if (origen < numFabricas) {  // Origen es una fábrica
                    capacidad[idxFabricas + origen][idxCamion] = capacidadesCamiones[i];
                } else {  // Origen es una bodega
                    origen -= numFabricas;
                    capacidad[idxBodegasSalida + origen][idxCamion] = capacidadesCamiones[i];
                }
                
                if (destino < numLibrerias) {  // Destino es una librería
                    capacidad[idxCamion][idxLibrerias + destino] = capacidadesCamiones[i];
                } else {  // Destino es una bodega
                    destino -= numLibrerias;
                    capacidad[idxCamion][idxBodegasEntrada + destino] = capacidadesCamiones[i];
                }
            }
            
            // Calcular el flujo máximo
            int flujoMaximo = fordFulkerson(capacidad, idxFuente, idxSumidero);
            
            // Imprimir resultado
            System.out.println("La cantidad máxima de libros que se pueden transportar en un día es: " + flujoMaximo);
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error en el formato del archivo: " + e.getMessage());
        }
    }
    
    /**
     * Implementación del algoritmo de Ford-Fulkerson para encontrar el flujo máximo.
     * 
     * @param capacidad Matriz de capacidades del grafo
     * @param fuente Nodo fuente
     * @param sumidero Nodo sumidero
     * @return Flujo máximo desde la fuente hasta el sumidero
     */
    private static int fordFulkerson(int[][] capacidad, int fuente, int sumidero) {
        int numNodos = capacidad.length;
        int[][] flujo = new int[numNodos][numNodos];
        int flujoMaximo = 0;
        
        // Crear grafo residual
        int[][] capacidadResidual = new int[numNodos][numNodos];
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                capacidadResidual[i][j] = capacidad[i][j];
            }
        }
        
        // Buscar caminos de aumento mientras existan
        int[] padre = new int[numNodos];
        while (bfs(capacidadResidual, fuente, sumidero, padre)) {
            // Encontrar la capacidad mínima en el camino de aumento
            int flujoRuta = Integer.MAX_VALUE;
            for (int v = sumidero; v != fuente; v = padre[v]) {
                int u = padre[v];
                flujoRuta = Math.min(flujoRuta, capacidadResidual[u][v]);
            }
            
            // Actualizar capacidades residuales y flujos
            for (int v = sumidero; v != fuente; v = padre[v]) {
                int u = padre[v];
                capacidadResidual[u][v] -= flujoRuta;
                capacidadResidual[v][u] += flujoRuta;
                flujo[u][v] += flujoRuta;
                flujo[v][u] -= flujoRuta;
            }
            
            flujoMaximo += flujoRuta;
        }
        
        return flujoMaximo;
    }
    
    /**
     * Implementación de BFS para encontrar un camino de aumento en el grafo residual.
     * 
     * @param capacidadResidual Matriz de capacidades residuales
     * @param fuente Nodo fuente
     * @param sumidero Nodo sumidero
     * @param padre Arreglo para almacenar el camino encontrado
     * @return true si se encontró un camino de aumento, false en caso contrario
     */
    private static boolean bfs(int[][] capacidadResidual, int fuente, int sumidero, int[] padre) {
        int numNodos = capacidadResidual.length;
        boolean[] visitado = new boolean[numNodos];
        
        Queue<Integer> cola = new LinkedList<>();
        cola.add(fuente);
        visitado[fuente] = true;
        padre[fuente] = -1;
        
        while (!cola.isEmpty()) {
            int u = cola.poll();
            
            for (int v = 0; v < numNodos; v++) {
                if (!visitado[v] && capacidadResidual[u][v] > 0) {
                    cola.add(v);
                    padre[v] = u;
                    visitado[v] = true;
                }
            }
        }
        
        return visitado[sumidero];
    }
}