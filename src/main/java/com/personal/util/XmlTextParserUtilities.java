package com.personal.util;

import com.personal.dto.ParametroDTO;
import com.personal.enums.FunctionEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ApplicationScoped
public class XmlTextParserUtilities {

    @Inject
    private FunctionUtilities functions;

    private final String directParamPattern = "%(\\w+)%";
    private final ParametroDTO notFoundValue = ParametroDTO.builder().valor("?").build();
    private final String functionParamPattern = "&(\\w+\\\\|\\w.+)&";

    public String createWithDirectInject(List<ParametroDTO> parameters, String xmlBase){
        // Inject direct reference values (%KEY%)
        StringBuilder buffer = new StringBuilder();
        Matcher matcherForVariables = Pattern.compile(directParamPattern).matcher(xmlBase);
        boolean doneForVariables = false;
        while(matcherForVariables.find()){
            String toReplace = matcherForVariables.group(1);
            log.debug("toReplace: {}", toReplace);
            String toInject = parameters.stream().filter(parameter ->
                    Objects.equals(parameter.getNombre(), toReplace))
                    .findFirst().orElse(notFoundValue).getValor();
            log.debug("toInject: {}", toInject);
            matcherForVariables.appendReplacement(buffer, toInject);
            doneForVariables = true;
        }
        if(doneForVariables) matcherForVariables.appendTail(buffer);

        // Inject with functions (&FUNCTION|PARAMS1,...,PARAMS2)
        String xmlVariable = buffer.toString();
        buffer = new StringBuilder();
        Matcher matcherForFunctions = Pattern.compile(functionParamPattern).matcher(xmlVariable);
        boolean doneForFunction = false;
        while(matcherForFunctions.find()){
            String toReplace = matcherForFunctions.group(1);
            String nameFunction = toReplace.split("\\|")[0].trim();
            String paramsFunction = toReplace.split("\\|")[1].trim();
            String toInject = callFunction(nameFunction, paramsFunction);
            log.debug("func:{} parameters:{}", nameFunction, paramsFunction);
            log.debug("result:{}", toInject);
            matcherForFunctions.appendReplacement(buffer, toInject);
            doneForFunction = true;
        }
        if(doneForFunction) {
            matcherForFunctions.appendTail(buffer);
            return buffer.toString();
        }
        return xmlVariable;
    }

    private String callFunction(String nameFunction, String paramsFunction) {
        return switch (FunctionEnum.valueOf(nameFunction)){
            //Funciones 1 parametro
            case TRIM -> functions.espacios(paramsFunction);
            case UPPERCASE -> functions.upperCase(paramsFunction);
            case LOWERCASE -> functions.lowerCase(paramsFunction);
            case TIME -> functions.currentTime(paramsFunction);
            //funciones 2 parametros
            case DECIMAL -> {
                String value = paramsFunction.split(",")[0];
                String decimalFormat = paramsFunction.split(",")[1];
                yield functions.decimalFormat(value, decimalFormat);
            }
            //funciones 3 parametros
            case RIGHTFILL -> {
                String value = paramsFunction.split(",")[0];
                Character gap = paramsFunction.split(",")[1].toCharArray()[0];
                int limit = Integer.parseInt(paramsFunction.split(",")[2]);
                yield functions.rightFill(value, gap, limit);
            }
            case LEFTFILL -> {
                String value = paramsFunction.split(",")[0];
                Character gap = paramsFunction.split(",")[1].toCharArray()[0];
                int limit = Integer.parseInt(paramsFunction.split(",")[2]);
                yield functions.leftFill(value, gap, limit);
            }
        };
    }

}
