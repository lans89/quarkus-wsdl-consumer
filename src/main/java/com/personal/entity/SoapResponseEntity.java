package com.personal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_soap_response")
public class SoapResponseEntity extends PanacheEntity {
    @Column(name="key_name")
    private String keyName;
    @Column(name="xpath")
    private String xPath;
    @Column(name="default_value")
    private String defaultValue;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_soap_service", referencedColumnName = "identificator")
    private SoapServiceEntity soapServiceEntity;

    public static Uni<List<SoapResponseEntity>> listAllChainRead(String idSoapService){
        return list("soapServiceEntity.identify", idSoapService);
    }
}
