package com.personal.service;

import com.personal.dto.RequestDTO;
import com.personal.entity.SoapRequestEntity;
import com.personal.entity.SoapServiceEntity;
import com.personal.util.XmlTextParserUtilities;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SoapExecutorService {
    private final Vertx vertx;
    private final WebClient client;
    private final XmlTextParserUtilities utilities;

    @Inject
    public SoapExecutorService(Vertx vertx, XmlTextParserUtilities utilities) {
        this.vertx = vertx;
        this.client = WebClient.create(vertx);
        this.utilities = utilities;
    }

    public Uni<String> callMethodService(RequestDTO requestDTO){
        return SoapServiceEntity.findByIdentify(requestDTO.getTransaccionId())
                .map(sconf ->{
                    SoapRequestEntity reqConf = sconf.getSoapRequestEntity();
                    String soapActionValue = reqConf.getSoapAction();
                    String contentTypeValue = reqConf.getContentType();
                    String xmlRequest = utilities.createWithDirectInject(requestDTO.getParametros(), reqConf.getBaseXmlRequest());
                    String urlValue = String.format("%s:%s%s", sconf.getServer(), sconf.getPort(),sconf.getPath());
                    return new String[]{contentTypeValue, soapActionValue, xmlRequest, urlValue};
                }).map(arrays -> {
                    String contentType = arrays[0];
                    String soapAction = arrays[1];
                    String xmlRequest = arrays[2];
                    String url = arrays[3];
                    HttpRequest<Buffer> request = client.requestAbs(HttpMethod.POST, url);
                    request.putHeader(HttpHeaders.CONTENT_TYPE, contentType);
                    request.putHeader("SOAPAction", soapAction);
                    return request.sendBuffer(Buffer.buffer(xmlRequest)).map(HttpResponse::bodyAsString);
                }).flatMap(unitResponse -> unitResponse);
    }
}
