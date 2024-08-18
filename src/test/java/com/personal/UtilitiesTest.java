package com.personal;

import com.personal.dto.ParametroDTO;
import com.personal.util.XmlTextParserUtilities;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@Slf4j
@QuarkusTest
class UtilitiesTest {

    @Inject
    XmlTextParserUtilities utilities;


    @Test
    void testReplaceTextXml() {
        List<ParametroDTO> params = List.of(
                ParametroDTO.builder().nombre("EVENTO").valor("dar mensaje").build(),
                ParametroDTO.builder().nombre("DURACION").valor("200").build(),
                ParametroDTO.builder().nombre("MENSAJE").valor("hola mundo").build()
        );
        String xml =
                "<root>\n" +
                "    <evento>%EVENTO%</evento>\n" +
                "    <duracion>%DURACION%</duracion>\n" +
                "    <mensaje>%MENSAJE%</mensaje>\n" +
                "</root>";
        LocalDateTime inicio = LocalDateTime.now();
        String request = utilities.createWithDirectInject(params, xml);
        LocalDateTime meta = LocalDateTime.now();
        log.info("\n{}",request);
        log.info("duracion: {} ms", Duration.between(inicio, meta).toMillis());
        Assertions.assertNotNull(request);
        Assertions.assertTrue(request.contains("200"));
    }

    @Test
    void testApplyFunctionTextXml() {
        List<ParametroDTO> params = List.of(
                ParametroDTO.builder().nombre("evento").valor("dar mensaje").build(),
                ParametroDTO.builder().nombre("duracion").valor("200").build(),
                ParametroDTO.builder().nombre("mensaje").valor("hola mundo").build(),
                ParametroDTO.builder().nombre("monto").valor("5.8").build()
        );
        String xml =
                "<root>\n" +
                "    <evento>%evento%</evento>\n" +
                "    <duracion>%duracion%</duracion>\n" +
                "    <mensaje>%mensaje%</mensaje>\n" +
                "    <fecha>&TIME|yyyyMMdd&</fecha>\n" +
                "    <tiempo>&TIME|HHmmss&</tiempo>\n" +
                "    <transaccion>\n" +
                "        <moneda>&TRIM|HNL    &</moneda>\n" +
                "        <monto>&DECIMAL|%monto%,###0.00&</monto>\n" +
                "    </transaccion>\n" +
                "    <campo>\n" +
                "        <id>&LEFTFILL|123,0,10&</id>\n" +
                "        <valor>&RIGHTFILL|HBAC10, ,10&</valor>\n" +
                "    </campo>\n" +
                "</root>";
        LocalDateTime inicio = LocalDateTime.now();
        String request = utilities.createWithDirectInject(params, xml);
        LocalDateTime meta = LocalDateTime.now();
        log.info("\n{}",request);
        log.info("duracion: {} ms", Duration.between(inicio, meta).toMillis());
        Assertions.assertNotNull(request);
        Assertions.assertTrue(request.contains("200"));
    }

}