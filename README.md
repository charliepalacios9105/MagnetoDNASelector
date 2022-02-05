# MagnetoDNASelector
Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men.

Te ha contratado a ti para que desarrolles un proyecto que detecte si un humano es mutante basándose en su secuencia de ADN.
Para eso te ha pedido crear un programa con un método o función con la siguiente firma (En alguno de los siguiente lenguajes: Java / Golang / C-C++ / Javascript (node) / Python / Ruby):

boolean isMutant(String[] dna); // Ejemplo Java

En donde recibirás como parámetro un array de Strings que representan cada fila de una tabla de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las cuales representa cada base nitrogenada del ADN.

<center>
<table>
	<tr>
		<td>
			<table>
				<tr><td>A</td><td>T</td><td>G</td><td>C</td><td>G</td><td>A</td></tr>
				<tr><td>C</td><td>A</td><td>G</td><td>T</td><td>G</td><td>C</td></tr>
				<tr><td>T</td><td>T</td><td>A</td><td>T</td><td>T</td><td>T</td></tr>
				<tr><td>A</td><td>G</td><td>A</td><td>C</td><td>G</td><td>G</td></tr>
				<tr><td>G</td><td>C</td><td>G</td><td>T</td><td>C</td><td>A</td></tr>
				<tr><td>T</td><td>C</td><td>A</td><td>C</td><td>T</td><td>G</td></tr>
		</table>
		</td>
    <td>
      <table>
        <tr><td><b>A</b></td><td>T</td><td>G</td><td>C</td><td><b>G</b></td><td>A</td></tr>
        <tr><td>C</td><td><b>A</b></td><td>G</td><td>T</td><td><b>G</b></td><td>C</td></tr>
        <tr><td>T</td><td>T</td><td><b>A</b></td><td>T</td><td><b>G</b></td><td>T</td></tr>
        <tr><td>A</td><td>G</td><td>A</td><td><b>A</b></td><td><b>G</b></td><td>G</td></tr>
        <tr><td><b>C</b></td><td><b>C</b></td><td><b>C</b></td><td><b>C</b></td><td>T</td><td>A</td></tr>
        <tr><td>T</td><td>C</td><td>A</td><td>C</td><td>T</td><td>G</td></tr>
      </table>
		</td>
	</tr>
  <tr>
    <td>
      No-Mutante
    </td>
    <td>
      Mutante
    </td>
  </tr>  
</table>
<center>

Sabrás si un humano es mutante, si encuentras más de una secuencia de cuatro letras iguales, de forma oblicua, horizontal o vertical.

Ejemplo (Caso mutante):

String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
En este caso el llamado a la función isMutant(dna) devuelve “true”.

Desarrolla el algoritmo de la manera más eficiente posible.

  
## Desafios
  ### Nivel 1
  
Para cumplir con el desafío en su primer nivel se implementó un algoritmo en el lenguaje de programación Java, este de enfoca a encontrar las posibles cadenas que se deben evaluar y por medio de una expresión regular determinar la cantidad de coincidencias que tiene cada cadena para la condición de 4 caracteres repetidos. Se realizan los siguientes pasos para cubrir todas las posibles cadenas que se deben evaluar:
  
  
  Se toma como ejemplo la siguiente matriz:
  
<table>
				<tr><td>A</td><td>T</td><td>G</td><td>C</td><td>G</td><td>A</td></tr>
				<tr><td>C</td><td>A</td><td>G</td><td>T</td><td>G</td><td>C</td></tr>
				<tr><td>T</td><td>T</td><td>A</td><td>T</td><td>T</td><td>T</td></tr>
				<tr><td>A</td><td>G</td><td>A</td><td>C</td><td>G</td><td>G</td></tr>
				<tr><td>G</td><td>C</td><td>G</td><td>T</td><td>C</td><td>A</td></tr>
				<tr><td>T</td><td>C</td><td>A</td><td>C</td><td>T</td><td>G</td></tr>
</table>
  
  
1.	Se realiza una validación inicial del vector recibido donde se valida la longitud del mismo, la cual no debe ser inferior a la cantidad de caracteres mínimos buscados para cumplir la condición del mutante que para el caso puntual es 4. A la par se valida la longitud individual de los elementos del vector la cual debe ser igual a la longitud del vector, cumpliendo con la condición de la matriz NxN. También se valida que las cadenas individuales contenidas en el vector no tengan caracteres no permitidos en este caso los permitidos se restringen a los caracteres A,T,C,G.
2.	Ya que la validación individual de los elementos del vector implica una primera iteración sencilla sobre este, a la par de la validación se realiza la búsqueda de la dimensión horizontal de los 4 caracteres repetidos, por lo que, si a la iteración sobre el vector termina, este es válido y se han contado 2 o más cadenas de caracteres repetidos de longitud 4, el método retorna true y termina su ejecución. La matriz de ejemplo en su dimensión horizontal no tiene tienen ninguna secuencia de caracteres que cumpla con el patrón en cuestión.
3.	Se continua con la búsqueda de en las dimensiones vertical y diagonal donde por medio del dos ciclos repetitivos y de la manipulación sus índices se buscarán mínimo una cadena en la dimensión vertical y hasta cuatro en la dimensión diagonal.
4.	La primera iteración generará la cadena de la primera vertical y las dos diagonales principales de la matriz y las evaluará. 

<table>
<tbody>
<tr><td><strong>A</strong></td><td>T</td><td>G</td><td>C</td><td>G</td><td><strong>A</strong></td></tr>
<tr><td><strong>C</strong></td><td><strong>A</strong></td><td>G</td><td>T</td><td><strong>G</strong></td><td>C</td></tr>
<tr><td><strong>T</strong></td><td>T</td><td><strong>A</strong></td><td><strong>T</strong></td><td>T</td><td>T</td></tr>
<tr><td><strong>A</strong></td><td>G</td><td><strong>A</strong></td><td><strong>C</strong></td><td>G</td><td>G</td></tr>
<tr><td><strong>G</strong></td><td><strong>C</strong></td><td>G</td><td>T</td><td><strong>C</strong></td><td>A</td></tr>
<tr><td><strong>T</strong></td><td>C</td><td>A</td><td>C</td><td>T</td><td><strong>G</strong></td></tr>
</tbody>
</table>
  
5.	La siguiente iteración avanzará en la dimensión vertical y el las dos diagonales paralelas a cada una de la diagonales principales, es decir que este caso las cadenas destinadas a ser evaluadas serán 5. Las cuales serán las que se muestan a continuación. 
  
![image](https://user-images.githubusercontent.com/21184033/152653782-8d21ac4f-4da8-495d-bdaf-268049273cf9.png)

6. Este proceso se repetirá en el caso de la dimensiones diagonales hasta que la cadena resultante sea mayor o igual al minimo requerido para cumplir con el patrón, es decir 4.
  Y se repetira para la dimensión temporal hasta que se hubiera cumplido recorrido toda la matriz. A continuación las imagenes muestran el recorrido de cada iteración.

  ![image](https://user-images.githubusercontent.com/21184033/152654036-7656631e-780a-47f9-af86-178d61ab5330.png)

7. Para las matrices que no cumplen la condición del mutante se realizará todo el recorrido, sin embargo, cada vez que se evalúa una cadena se verifica si la condición del mutante se cumplió lo cual causa que la ejecución del proceso se detenga y se retorne true. A continuación, las interacciones que se hacen para una matriz de ejemplo que si cumple con la condición.

  ![image](https://user-images.githubusercontent.com/21184033/152654472-af0174da-3c99-472e-b8d4-0933687969ea.png)

  
