package br.com.fiap.product_import.job;

import org.springframework.batch.item.ItemProcessor;

public class ProductImportProcessor implements ItemProcessor<ProductImport, ProductImport> {

    @Override
    public ProductImport process(ProductImport item) throws Exception {
        return item;
    }

}
