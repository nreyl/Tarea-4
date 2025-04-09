# Algoritmos de Grafos - Implementaciones en Java

Este repositorio contiene implementaciones en Java de varios algoritmos de grafos para resolver diferentes problemas. A continuación, se describe cada algoritmo, su propósito, formato de entrada/salida e instrucciones de ejecución.

## 1. Caminos de Costos Mínimos

### Descripción

Implementación de los algoritmos de Dijkstra, Bellman-Ford y Floyd-Warshall para encontrar la matriz de costos mínimos de caminos entre todos los vértices de un grafo dirigido con costos en los números naturales.

### Algoritmos Implementados

1. **Dijkstra**: Encuentra los caminos más cortos desde un vértice origen a todos los demás vértices. Funciona bien con grafos con pesos positivos.

1. Complejidad: O(V²) donde V es el número de vértices.



2. **Bellman-Ford**: Encuentra los caminos más cortos desde un vértice origen a todos los demás vértices. Puede manejar grafos con pesos negativos y detectar ciclos negativos.

1. Complejidad: O(V·E) donde V es el número de vértices y E es el número de aristas.



3. **Floyd-Warshall**: Encuentra los caminos más cortos entre todos los pares de vértices en un solo paso.

1. Complejidad: O(V³) donde V es el número de vértices.





### Formato de Entrada

El archivo de entrada contiene una línea por cada conexión directa entre un nodo fuente y un nodo destino. Cada línea tiene el siguiente formato:

```plaintext
<nodo_fuente> <nodo_destino> <costo>
```

Ejemplo:

```plaintext
0 1 90
0 2 80
1 0 15
1 2 69
1 3 48
...
```

### Instrucciones de Ejecución

```plaintext
javac CaminosMinimos.java
java CaminosMinimos archivo_entrada.txt
```

## 2. BFS para Encontrar Componentes Conectados

### Descripción

Implementación de un algoritmo BFS (Breadth-First Search) para encontrar los componentes conectados en un grafo no dirigido. Un componente conectado es un subconjunto de vértices tal que para cada par de vértices en el subconjunto existe un camino que los conecta.

### Algoritmo Implementado

El programa utiliza el algoritmo BFS para explorar todos los vértices alcanzables desde un vértice dado. Para cada vértice no visitado, realiza un BFS para encontrar todos los vértices en su componente conectado.

- Complejidad: O(V + E), donde V es el número de vértices y E es el número de aristas.


### Formato de Entrada

```plaintext
<número_de_vértices>
<vértice_origen> <vértice_destino>
<vértice_origen> <vértice_destino>
...
```

Ejemplo:

```plaintext
7
0 3
1 5
2 3
4 6
```

### Instrucciones de Ejecución

```plaintext
javac ComponentesConectados.java
java ComponentesConectados archivo_entrada.txt
```

### Formato de Salida

```plaintext
Componentes conectados:
{{v1,v2,...},{v3,v4,...},...}
```

Ejemplo:

```plaintext
Componentes conectados:
{{0,2,3},{1,5},{4,6}}
```

## 3. Optimización de Vías en una Ciudad

### Descripción

Algoritmo para determinar qué vías de una sola dirección deben convertirse en dobles vías en una ciudad, de modo que se pueda transitar entre cualquier par de puntos usando solo dobles vías, minimizando el costo total de conversión.

### Algoritmo Implementado

El programa modela la ciudad como un grafo dirigido y utiliza el algoritmo de Kruskal para encontrar el Árbol de Expansión Mínima (MST) en un grafo no dirigido derivado del grafo original.

- Complejidad: O(m log m), donde m es el número de vías.


### Formato de Entrada

```plaintext
<número_de_intersecciones> <número_de_vías>
<origen> <destino> <costo>
<origen> <destino> <costo>
...
```

Ejemplo:

```plaintext
5 7
0 1 10
1 2 15
2 3 20
3 4 25
4 0 30
1 3 35
2 0 40
```

### Instrucciones de Ejecución

```plaintext
javac OptimizacionVias.java
java OptimizacionVias archivo_entrada.txt
```

### Formato de Salida

```plaintext
Costo total de conversión: <costo>
Vías a convertir en doble vía:
<origen> -> <destino>
<origen> -> <destino>
...
```

## 4. Distribución Óptima de Libros

### Descripción

Algoritmo para determinar la cantidad máxima de libros que se pueden transportar en un día desde fábricas hasta librerías, pasando opcionalmente por bodegas, dadas las restricciones de capacidad de los camiones y las bodegas.

### Algoritmo Implementado

El programa modela el problema como un problema de flujo máximo en una red y utiliza el algoritmo de Ford-Fulkerson para resolverlo.

- Complejidad: O(E * max_flow), donde E es el número de aristas en el grafo y max_flow es el valor del flujo máximo.


### Formato de Entrada

```plaintext
<num_fábricas> <num_librerías> <num_bodegas> <num_camiones>
<capacidad_bodega_1> <capacidad_bodega_2> ... <capacidad_bodega_B>
<capacidad_camión_1> <capacidad_camión_2> ... <capacidad_camión_C>
<origen> <destino> <tipo_origen> <tipo_destino>
<origen> <destino> <tipo_origen> <tipo_destino>
...
```

Donde:

- `<tipo_origen>` es 0 para fábrica, 1 para bodega
- `<tipo_destino>` es 0 para librería, 1 para bodega


Ejemplo:

```plaintext
2 3 2 5
100 150
50 60 70 80 90
0 0 0 0
0 1 0 0
1 0 0 1
1 2 0 0
0 1 1 0
```

### Instrucciones de Ejecución

```plaintext
javac DistribucionLibros.java
java DistribucionLibros archivo_entrada.txt
```

### Formato de Salida

```plaintext
La cantidad máxima de libros que se pueden transportar en un día es: <cantidad>
```

## Notas Generales

- Todos los programas asumen que los vértices están numerados consecutivamente desde 0.
- Los programas están diseñados para ser robustos ante errores en el formato de entrada.
- Se utilizan estructuras de datos eficientes como listas de adyacencia, conjuntos disjuntos (Union-Find) y colas para implementar los algoritmos.
- Los programas incluyen comentarios detallados para facilitar su comprensión y mantenimiento.


## Requisitos del Sistema

- Java Development Kit (JDK) 8 o superior
- Sistema operativo compatible con Java (Windows, macOS, Linux)