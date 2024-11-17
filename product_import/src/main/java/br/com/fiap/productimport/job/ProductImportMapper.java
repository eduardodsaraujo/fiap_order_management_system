package br.com.fiap.productimport.job;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ProductImportMapper implements FieldSetMapper<ProductImport> {

    @Override
    public ProductImport mapFieldSet(FieldSet fieldSet) throws BindException {
        return ProductImport.builder()
                .code(fieldSet.readString("code"))
                .name(fieldSet.readString("name"))
                .description(fieldSet.readString("description"))
                .category(fieldSet.readString("category"))
                .manufacturer(fieldSet.readString("manufacturer"))
                .enable(fieldSet.readBoolean("enable"))
                .price(fieldSet.readDouble("price"))
                .weight(fieldSet.readDouble("weight"))
                .stockQuantity(fieldSet.readDouble("stockQuantity"))
                .build();
    }

}
