package br.com.fiap.productimport.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportFile {

    private String name;
    private String directory;
    private byte[] bytes;

}
