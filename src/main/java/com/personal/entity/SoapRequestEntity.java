package com.personal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "tbl_soap_request")
@Entity
public class SoapRequestEntity extends PanacheEntity {
    @Column(name = "base_xml")
    private String baseXmlRequest;
    @Column(name="method_name")
    private String methodName;
    @Column(name="soap_action")
    private String soapAction;
    @Column(name="content_type")
    private String contentType;

    @OneToOne
    @JoinColumn(name = "id_soap_service", referencedColumnName = "identificator")
    @JsonIgnore
    public SoapServiceEntity soapServiceEntity;

    public static Uni<SoapRequestEntity> findFromId(String idSoapService){
        return find("soapServiceEntity.identify", idSoapService).firstResult();
    }
}
