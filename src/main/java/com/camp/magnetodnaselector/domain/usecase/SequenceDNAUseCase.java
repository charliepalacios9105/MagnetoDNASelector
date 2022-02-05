package com.camp.magnetodnaselector.domain.usecase;

import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.model.gateway.SequenceDNARepository;
import com.camp.magnetodnaselector.domain.model.gateway.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de negocio especifica que contiene los metodos y constantes para determinar
 * si una secuencia de ADN en cuention es de un mutante o no.
 * <p>
 * Se recibe como parametro un array de Strings que representan cada fila de una matriz
 * de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las
 * cuales representa cada base nitrogenada del ADN.
 * <p>
 * Se determina si un humano es mutante, si se encuentran mas de una secuencia de cuatro letras
 * iguales, de forma diagonal, horizontal o vertical.
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Component
@RequiredArgsConstructor
public class SequenceDNAUseCase {

    /**
     * Numero minimo de secuencias que debe tener la cadena de ADN para determinar si se es un mutante
     */
    private static final int MIN_NUMBER_OF_SEQ = 2;

    /**
     * Cantidad de caracteres repetidos que se deben tener las secuencias a buscar
     */
    private static final int SEQUENCE_SIZE = 4;

    /**
     * Patron de conteo que determina si en una cadena de texto existen una cantidad determinada de caracteres repetidos.
     * En este caso esta cantidad esta dada por SEQUENCE_SIZE.
     * La expresion regular se descompone asi:
     * (\w): Determina cualquier caracter alfanumerico
     * \1: Determina que se espera repeticion de el patron encontrado en la posicion anterior
     * { (SEQUENCE_SIZE - 1) }: Determina la cantidad exacta de veces que se evalua el patron anterior,
     * es SEQUENCE_SIZE - 1 debido a que primer patron se evalua con el \1.
     */
    private static final Pattern COUNT_SEQUENCE_PATTERN = Pattern.compile("(\\w)\\1{" + (SEQUENCE_SIZE - 1) + "}+");

    /**
     * Patron de conteo que determina si en la cadena existen caracteres diferentes a los contenidos en la
     * expreson
     */
    private static final Pattern VALID_CHAR_PATTERN = Pattern.compile("[ATCG]+");

    /**
     * Instancia de la interface {@link SequenceDNARepository} para operaciones realicionadas con el
     * modelo de negocio {@link SequenceDNAModel}
     */
    private final SequenceDNARepository sequenceDNARepository;

    /**
     * Instancia de la interface {@link StatRepository} para operaciones realicionadas con el
     * modelo de negocio {@link StatModel}
     */
    private final StatRepository statRepository;


    /**
     * Llamado principal para determinar si la cadena de ADN pertence a un mutante.
     * <p>
     * Su primera instruccion es buscar si la cadena ya se encuentra almacenada para no evaluarla si
     * no tomar el resultado previamente guardado de la evaluacion, si no se encuentra la cadena guardada
     * sigue con el siguiente proceso:
     * <p>
     * Instancia un objeto inmutable numerico que sirve de contador el cual se parametrizara
     * en los metodos siguientes.
     * Como primer paso realiza una validacion inicial para determinar que el vector
     * de strings cumpla con las condiciones minimas para ser evaluada. Si esta validacion no es
     * exitosa el metodo de validacion lanzara una {@link InvalidDNAException}. El metodo de validacion
     * hace un primer recorrido al vector, por lo cual tambien hace la primera busqueda de cadenas, las de la dimesion horizontal,
     * para evaluar el ADN. En caso de que encuentre la cantidad necesaria de cadenas cadenas de
     * caracteres seguidos no realizara mas busquedas y el resultado sera true.
     * De no encontrar las cadenas necesarias para determinar que el ADN pertence a un mutante
     * sera necesario ejecutar el metodo de busqueda y generacion de cadenas el cual itera sobre el vector
     * generando las posibles cadenas que se deben evaluar en la dimension vertical y diagonal.
     * El ultimo paso para cualquiera de los casos descritos es guardar la cadena y el resultado de la
     * busqueda por medio de la implementacion de metodo de guardado del objeto sequenceDNARepository.
     *
     * @param sequenceDNAModel Modelo de negocio que contiene la cadena a evaluar
     * @return booleano que determina si la cadena evaluada pertenece aun mutante
     * @throws InvalidDNAException Se lanza la excepcion en caso de que la cadena no sea valida para evaluar
     */
    public boolean isMutant(SequenceDNAModel sequenceDNAModel) throws InvalidDNAException {
        Boolean res = sequenceDNARepository.isMutantSavedDNA(sequenceDNAModel.getDna());
        if (res == null) {
            AtomicInteger count = new AtomicInteger(0);
            String[] dna = sequenceDNAModel.getDna();
            int size = dna.length;
            validDNASequence(count, dna, size);
            if (count.intValue() < MIN_NUMBER_OF_SEQ) {
                generateAndEvalSubsequence(count, dna, size);
            }
            res = count.intValue() >= MIN_NUMBER_OF_SEQ;
            sequenceDNARepository.saveDNA(sequenceDNAModel, res);
        }
        return res;
    }

    /**
     * Metodo de validacion de la cadena realiza las siguientes validaciones:
     * <p>
     * Que el vector tenga una longitud minima de el valor dado por la constante {@link #SEQUENCE_SIZE}.
     * Que las variables string que estan en el vector tengan la misma longitud que el vector,
     * garantizando que la matriz sea cuadrada.
     * Que las variables string que estan en el vector no tengan caracteres no validos cumpliendo con el patron {@link #VALID_CHAR_PATTERN}
     * <p>
     * Ya que hay una evaluacion individual de cada cadena en este metodo tambien se va evaluando cada variables string
     * para tenerminar si tienen valores que cumplan con la condicion establecida en la regla de negocio para ser un mutante
     * y en tal caso agregar las ocurreccias encontradas al parametro count.
     *
     * @param count numerico inmutable para contar la cadenas que cumplen el patron de mutacion encontradas
     * @param dna   cadena de ADN a evaluar
     * @param size  tamanio de la cadena de ADN
     * @throws InvalidDNAException Se lanza la excepcion en caso de que la cadena no sea valida para evaluar
     */
    private void validDNASequence(AtomicInteger count, String[] dna, int size) throws InvalidDNAException {
        if (size < SEQUENCE_SIZE) {
            throw new InvalidDNAException("The length of the DNA string is less than the minimum allowed");
        }
        for (String seq : dna) {
            if (seq.length() != size) {//No es valida la secuencia si el tamanio de alguno de sus elementos es direfente al taminio del vector
                throw new InvalidDNAException("The length of an individual string is different than the DNA string");
            }
            Matcher validateMatcher = VALID_CHAR_PATTERN.matcher(seq);//No es valida la secuencia si contiene caracteres diferentes a los establecidos en el patron de validacion
            if (validateMatcher.matches()) {
                evalSequence(seq, count); // Se hace una primera evaluacion de las cadenas que componen el vector, siendo esta la evalucion del las horizontales
            } else {
                throw new InvalidDNAException("The individual string has illegal characters");
            }
        }
    }

    /**
     * Este metodo realiza la generacion de las cadenas totales que componen el vector de strings para
     * su posterior evaluacion. La idea es buscar la mayor cantidad de cadenas en las dimensiones
     * vericales y diagonales por cada iteracion que se hace.
     * <p>
     * Sea la matriz:
     * AACCG
     * TCGAA
     * TCGCA
     * GACTG
     * AAGTC
     * <p>
     * El algoritmo a partir de dos ciclos hara el recorrido generando en cada iteracion minimo una cadena
     * de dimension vertical y hasta 4 de dimension diagonal.
     * En la primera iteracion generara las primera vertical y las dos diaginales principales siendo estas:
     * ATTGA    primera vertical
     * ACGTC    Diagonal principal derecha
     * GAGAA    Diagonal principal izquerda
     * <p>
     * Para la segunda iteracion generara la segunda vertical y las diagonales que estan al lado de cada diagonal
     * principal siendo estas:
     * ACCAA    segunda vertical
     * AGCG     Diagonal superior derecha
     * TCCT     Diagonal inferior derecha
     * CGCG     Diagonal superior izquerda
     * ACCA     Diagonal inferior izquerda
     * <p>
     * El proceso se repite hasta que se recorre toda la matriz siendo la ultima cadena generada la ultima vertical
     * si no se ha cumplido la condicion de mutante antes, la cual es que el valor de count se mayor o igual
     * al dado por la constante {@link #MIN_NUMBER_OF_SEQ}.
     * Al finalizar cada interacion se evaluan las cadenas generadas por medio del
     * metodo {@link #validSequenceList(AtomicInteger, StringBuilder...)} el cual de igualmenera detiene su
     * ejecucion si la condicion del mutante se cumple. Lo cual retornara metodo {@link #isMutant(SequenceDNAModel)}
     *
     * @param count numerico inmutable para contar la cadenas que cumplen el patron de mutacion encontradas
     * @param dna   cadena de ADN a evaluar
     * @param size  tamanio de la cadena de ADN
     */
    private void generateAndEvalSubsequence(AtomicInteger count, String[] dna, int size) {
        StringBuilder topLeftDiagonalSeq = new StringBuilder("");
        StringBuilder lowerRightDiagonalSeq = new StringBuilder("");
        StringBuilder lowerLeftDiagonalSeq = new StringBuilder("");
        StringBuilder topRightDiagonalSeq = new StringBuilder("");
        StringBuilder verticalSeq = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + SEQUENCE_SIZE) <= size && (j < (size - i))) { //Validacion para evitar las cadenas diagonales de menor logitud al tamanio minimo de la secuencia
                    lowerLeftDiagonalSeq.append(dna[i + j].charAt((size - 1) - j));
                    topRightDiagonalSeq.append(dna[j].charAt(j + i));
                    if (i > 0) {//Para que no se repitan las diagonales principales
                        topLeftDiagonalSeq.append(dna[j].charAt((size - 1) - j - i));
                        lowerRightDiagonalSeq.append(dna[j + i].charAt(j));
                    }
                }
                verticalSeq.append(dna[j].charAt(i));
            }
            validSequenceList(count, verticalSeq, topLeftDiagonalSeq, lowerLeftDiagonalSeq, topRightDiagonalSeq, lowerRightDiagonalSeq);
            if (count.intValue() >= MIN_NUMBER_OF_SEQ) {
                return;
            }
        }
    }

    /**
     * Recorre la lista de cadenas de texto ingresadas en el vector sequences y realiza la evalucion
     * de cada una para encontrar la catidad de cadenas que cumplen con el patron  {@link #COUNT_SEQUENCE_PATTERN}.
     * <p>
     * En caso de que el valor del numerico inmutable count sea mayor o igual al valor dado por
     * la constante {@link #MIN_NUMBER_OF_SEQ} el metodo retornara
     *
     * @param count     numerico inmutable para contar la cadenas que cumplen el patron de mutacion encontradas
     * @param sequences listado de secuencias a evaluar
     */
    public void validSequenceList(AtomicInteger count, StringBuilder... sequences) {
        for (StringBuilder sequence : sequences) {
            if (sequence.length() > 0) {
                evalSequence(sequence.toString(), count);
                sequence.setLength(0);
                if (count.intValue() >= MIN_NUMBER_OF_SEQ) {
                    return;
                }
            }
        }
    }

    /**
     * Realiza la validacion de la cadena de texto ingresada por parametro, donde
     * evalua si coincide con el patron {@link #COUNT_SEQUENCE_PATTERN} y le suma la
     * cantidad de ocurrecias al contador inmutable
     *
     * @param seq   secuencia a evaluar
     * @param count numerico inmutable para contar la cadenas que cumplen el patron de mutacion encontradas
     */
    private void evalSequence(String seq, AtomicInteger count) {
        Matcher m = COUNT_SEQUENCE_PATTERN.matcher(seq);
        count.addAndGet((int) m.results().count());
    }

    /**
     * Retorna el objeto con las estadisticas solicitadas segun la implementacion del
     * objeto statRepository
     *
     * @return una instancia de la clase {@link StatModel}
     */
    public StatModel getStat() {
        return statRepository.getStat();
    }

}
