import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ComponentesConectados {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java ComponentesConectados <archivo_entrada>");
            return;
        }
        
        String archivoEntrada = args[0];
        List<List<Integer>> listaAdyacencia = new ArrayList<>();
        int numVertices = 0;
        
        try {
            // Leer el archivo de entrada
            BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
            String linea;
            
            // Primera línea contiene el número de vértices
            linea = br.readLine();
            if (linea != null) {
                numVertices = Integer.parseInt(linea.trim());
                
                // Inicializar la lista de adyacencia
                for (int i = 0; i < numVertices; i++) {
                    listaAdyacencia.add(new ArrayList<>());
                }
                
                // Leer las aristas
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.trim().split("\\s+");
                    if (partes.length == 2) {
                        int u = Integer.parseInt(partes[0]);
                        int v = Integer.parseInt(partes[1]);
                        
                        // Grafo no dirigido: agregar arista en ambas direcciones
                        listaAdyacencia.get(u).add(v);
                        listaAdyacencia.get(v).add(u);
                    }
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
        
        // Si no se especificó el número de vértices, calcularlo
        if (numVertices == 0) {
            for (List<Integer> adyacentes : listaAdyacencia) {
                for (int v : adyacentes) {
                    numVertices = Math.max(numVertices, v + 1);
                }
            }
        }
        
        // Encontrar componentes conectados usando BFS
        List<List<Integer>> componentes = encontrarComponentesConectados(listaAdyacencia, numVertices);
        
        // Imprimir los componentes conectados
        System.out.println("Componentes conectados:");
        imprimirComponentes(componentes);
    }
    
    /**
     * Encuentra los componentes conectados en un grafo no dirigido usando BFS.
     * 
     * @param listaAdyacencia Lista de adyacencia que representa el grafo
     * @param numVertices Número de vértices en el grafo
     * @return Lista de componentes conectados, donde cada componente es una lista de vértices
     */
    private static List<List<Integer>> encontrarComponentesConectados(List<List<Integer>> listaAdyacencia, int numVertices) {
        boolean[] visitado = new boolean[numVertices];
        List<List<Integer>> componentes = new ArrayList<>();
        
        for (int i = 0; i < numVertices; i++) {
            if (!visitado[i]) {
                // Encontrar todos los vértices en el mismo componente que i
                List<Integer> componente = new ArrayList<>();
                bfs(listaAdyacencia, i, visitado, componente);
                
                // Ordenar los vértices del componente para facilitar la lectura
                Collections.sort(componente);
                componentes.add(componente);
            }
        }
        
        return componentes;
    }
    
    /**
     * Realiza un recorrido BFS desde un vértice origen y marca todos los vértices alcanzables.
     * 
     * @param listaAdyacencia Lista de adyacencia que representa el grafo
     * @param origen Vértice desde donde comenzar el BFS
     * @param visitado Arreglo que indica qué vértices han sido visitados
     * @param componente Lista donde se almacenarán los vértices del componente
     */
    private static void bfs(List<List<Integer>> listaAdyacencia, int origen, boolean[] visitado, List<Integer> componente) {
        Queue<Integer> cola = new LinkedList<>();
        
        // Marcar el origen como visitado y agregarlo a la cola
        visitado[origen] = true;
        cola.add(origen);
        componente.add(origen);
        
        while (!cola.isEmpty()) {
            int actual = cola.poll();
            
            // Explorar todos los vértices adyacentes
            for (int adyacente : listaAdyacencia.get(actual)) {
                if (!visitado[adyacente]) {
                    visitado[adyacente] = true;
                    cola.add(adyacente);
                    componente.add(adyacente);
                }
            }
        }
    }
    
    /**
     * Imprime los componentes conectados en el formato requerido.
     * 
     * @param componentes Lista de componentes conectados
     */
    private static void imprimirComponentes(List<List<Integer>> componentes) {
        StringBuilder sb = new StringBuilder("{");
        
        for (int i = 0; i < componentes.size(); i++) {
            List<Integer> componente = componentes.get(i);
            sb.append("{");
            
            for (int j = 0; j < componente.size(); j++) {
                sb.append(componente.get(j));
                if (j < componente.size() - 1) {
                    sb.append(",");
                }
            }
            
            sb.append("}");
            if (i < componentes.size() - 1) {
                sb.append(",");
            }
        }
        
        sb.append("}");
        System.out.println(sb.toString());
    }
}