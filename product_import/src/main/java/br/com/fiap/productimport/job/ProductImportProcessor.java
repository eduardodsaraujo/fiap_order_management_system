package br.com.fiap.productimport.job;

import org.springframework.batch.item.ItemProcessor;

public class ProductImportProcessor implements ItemProcessor<ProductImport, ProductImport> {

    @Override
    public ProductImport process(ProductImport item) throws Exception {
        return item;
    }

}
