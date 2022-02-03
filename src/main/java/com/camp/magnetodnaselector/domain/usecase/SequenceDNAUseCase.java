package com.camp.magnetodnaselector.domain.usecase;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.model.gateway.SequenceDNARepository;
import com.camp.magnetodnaselector.domain.model.gateway.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * expreson y separados por el operador |
     */
    private static final Pattern VALID_CHAR_PATTERN = Pattern.compile("[ATCG]+");

    private final SequenceDNARepository sequenceDNARepository;
    private final StatRepository statRepository;

    public boolean isMutant(SequenceDNAModel sequenceDNAModel) {
        Boolean res = sequenceDNARepository.isMutantSavedDNA(sequenceDNAModel.getDna());
        if (res == null) {
            AtomicInteger count = new AtomicInteger(0);
            String[] dna = sequenceDNAModel.getDna();
            int size = dna.length;
            validDNASequence(count, dna, size);
            if (count.intValue() < MIN_NUMBER_OF_SEQ) {
                generateAndEvalSubsequence(dna, count, size);
            }
            res = count.intValue() >= MIN_NUMBER_OF_SEQ;
            sequenceDNARepository.saveDNA(sequenceDNAModel, res);
        }
        return res;
    }

    private void validDNASequence(AtomicInteger count, String[] dna, int size) {
        for (String seq : dna) {
            if (seq.length() != size) {//No es valida la secuencia si el tamanio de alguno de sus elementos es direfente al taminio del vector
                break;
            }
            Matcher validateMatcher = VALID_CHAR_PATTERN.matcher(seq);//No es valida la secuencia si contiene caracteres diferentes a los establecidos en el patron de validacion
            if (validateMatcher.matches()) {
                evalSequence(seq, count); // Se hace una primera evaluacion de las cadenas que componen el vector, siendo esta la evalucion del las horizontales
            } else {
                break;
            }
        }
    }

    public void generateAndEvalSubsequence(String[] dna, AtomicInteger count, int size) {
        StringBuilder topLeftDiagonalSeq = new StringBuilder("");
        StringBuilder lowerRightDiagonalSeq = new StringBuilder("");
        StringBuilder lowerLeftDiagonalSeq = new StringBuilder("");
        StringBuilder topRightDiagonalSeq = new StringBuilder("");
        StringBuilder verticalSeq = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + SEQUENCE_SIZE) <= size && (j < (size - i))) { //ValidaciOn para evitar las cadenas diagonales de menor logitud al tamanio de la secuencia
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

    private void evalSequence(String seq, AtomicInteger count) {
        if (!seq.isEmpty()) {
            Matcher m = COUNT_SEQUENCE_PATTERN.matcher(seq);
            count.addAndGet((int) m.results().count());
        }
    }

    public StatModel getStat() {
        return statRepository.getStat();
    }

}
