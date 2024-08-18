package com.personal.entity;

import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Collection;

@Slf4j
@Table(name = "tbl_soap_service")
@Entity
@Getter
@Setter
public class SoapServiceEntity extends PanacheEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "server")
    private String server;
    @Column(name = "port")
    private Integer port;
    @Column(name = "resource")
    private String path;
    @Column(name = "identificator")
    private String identify;

    @OneToOne(mappedBy = "soapServiceEntity")
    @Fetch(FetchMode.JOIN)
    private SoapRequestEntity soapRequestEntity;

    @OneToMany(mappedBy="soapServiceEntity")
    @Fetch(FetchMode.JOIN)
    private Collection<SoapResponseEntity> soapResponseEntities;

    @CacheResult(cacheName = "soap-service-config")
    public static Uni<SoapServiceEntity> findByIdentify(String identify){
        log.info("--db tbl_soap_service.call[{}]", identify);
        return find("identify", identify).firstResult();
    }

}
