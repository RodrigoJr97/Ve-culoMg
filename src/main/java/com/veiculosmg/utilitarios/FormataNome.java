package com.veiculosmg.utilitarios;

import java.util.function.UnaryOperator;

public class FormataNome {

    public final static UnaryOperator<String> formatacaoNome = nomeParaFormatar -> {
        String[] partesDoNome = nomeParaFormatar.split(" ");
        StringBuilder nomeFormatado = new StringBuilder();

        for (String parteNome : partesDoNome) {
            if (!parteNome.isEmpty()) {
                char primeiraLetraMaiuscula = Character.toUpperCase(parteNome.charAt(0));
                String restoDoNome = parteNome.substring(1).toLowerCase();

                nomeFormatado.append(primeiraLetraMaiuscula).append(restoDoNome).append(" ");
            }
        }

        nomeFormatado.setLength(nomeFormatado.length() - 1);
        return nomeFormatado.toString();
    };

}
