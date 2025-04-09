**Algoritmos de Grafos - Implementaciones en Java**

Tabla de Contenidos:

- Algoritmos Implementados
  1. Caminos de Costos Mínimos
  2. BFS para Componentes Conectados
  3. Optimización de Vías en una Ciudad
  4. Distribución Óptima de Libros
- Requisitos del Sistema
- Notas Generales

---

**1. Caminos de Costos Mínimos**

Descripción:
Implementación de los algoritmos de Dijkstra, Bellman-Ford y Floyd-Warshall para encontrar la matriz de costos mínimos de caminos entre todos los vértices de un grafo dirigido con costos en los números naturales.

Algoritmos:

Dijkstra: Encuentra los caminos más cortos desde un vértice origen a todos los demás vértices. Funciona bien con grafos con pesos positivos. Complejidad: O(V²)

Bellman-Ford: Encuentra los caminos más cortos desde un vértice origen a todos los demás vértices. Puede manejar grafos con pesos negativos y detectar ciclos negativos. Complejidad: O(V·E)

Floyd-Warshall: Encuentra los caminos más cortos entre todos los pares de vértices en un solo paso. Complejidad: O(V³)

Formato de Entrada:
Cada línea del archivo representa una conexión directa entre un nodo fuente y uno destino:
<nodo_fuente> <nodo_destino> <costo>

Ejemplo:
0 1 90  
0 2 80  
1 0 15  
1 2 69  
1 3 48

Instrucciones de Ejecución:
javac CaminosMinimos.java  
java CaminosMinimos archivo_entrada.txt

---

**2. BFS para Componentes Conectados**

Descripción:
Implementación de un algoritmo BFS (Breadth-First Search) para encontrar los componentes conectados en un grafo no dirigido.

Algoritmo:
Se utiliza BFS para explorar todos los vértices alcanzables desde un vértice dado. Complejidad: O(V + E)

Formato de Entrada:
<número_de_vértices>  
<vértice_origen> <vértice_destino>  
...

Ejemplo:
7  
0 3  
1 5  
2 3  
4 6

Instrucciones de Ejecución:
javac ComponentesConectados.java  
java ComponentesConectados archivo_entrada.txt

Formato de Salida:
Componentes conectados:  
{{0,2,3},{1,5},{4,6}}

---

**3. Optimización de Vías en una Ciudad**

Descripción:
Algoritmo para decidir qué vías unidireccionales deben convertirse en dobles vías para que haya conectividad total en la ciudad al menor costo.

Algoritmo:
Se usa Kruskal para encontrar el Árbol de Expansión Mínima en el grafo subyacente no dirigido.  
Complejidad: O(m log m)

Formato de Entrada:
<número_de_intersecciones> <número_de_vías>  
<origen> <destino> <costo>  
...

Ejemplo:
5 7  
0 1 10  
1 2 15  
2 3 20  
3 4 25  
4 0 30  
1 3 35  
2 0 40

Instrucciones de Ejecución:
javac OptimizacionVias.java  
java OptimizacionVias archivo_entrada.txt

Formato de Salida:
Costo total de conversión: <costo>  
Vías a convertir en doble vía:  
<origen> -> <destino>  
...

---

**4. Distribución Óptima de Libros**

Descripción:
Determina la cantidad máxima de libros que pueden ser transportados en un día desde fábricas hasta librerías, considerando las bodegas y camiones como intermediarios.

Algoritmo:
Se utiliza Ford-Fulkerson para resolver el problema como flujo máximo.  
Complejidad: O(E * max_flow)

Formato de Entrada:
<num_fábricas> <num_librerías> <num_bodegas> <num_camiones>  
<capacidades_bodegas>  
<capacidades_camiones>  
<origen> <destino> <tipo_origen> <tipo_destino>  
...

Ejemplo:
2 3 2 5  
100 150  
50 60 70 80 90  
0 0 0 0  
0 1 0 0  
1 0 0 1  
1 2 0 0  
0 1 1 0

Instrucciones de Ejecución:
javac DistribucionLibros.java  
java DistribucionLibros archivo_entrada.txt

Formato de Salida:
La cantidad máxima de libros que se pueden transportar en un día es: <cantidad>
