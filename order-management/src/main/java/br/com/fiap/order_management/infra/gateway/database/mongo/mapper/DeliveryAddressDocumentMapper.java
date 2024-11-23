package br.com.fiap.order_management.infra.gateway.database.mongo.mapper;

import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.DeliveryAddressDocument;

public class DeliveryAddressDocumentMapper {

    public static DeliveryAddressDocument toDocument(DeliveryAddress deliveryAddress) {
        if (deliveryAddress != null) {
            return DeliveryAddressDocument.builder()
                    .id(deliveryAddress.getId())
                    .street(deliveryAddress.getStreet())
                    .number(deliveryAddress.getNumber())
                    .complement(deliveryAddress.getComplement())
                    .district(deliveryAddress.getDistrict())
                    .city(deliveryAddress.getCity())
                    .state(deliveryAddress.getState())
                    .postalCode(deliveryAddress.getPostalCode())
                    .build();
        }
        return null;
    }

    public static DeliveryAddress toDomain(DeliveryAddressDocument deliveryAddressDocument) {
        if (deliveryAddressDocument != null) {
            return DeliveryAddress.builder()
                    .id(deliveryAddressDocument.getId())
                    .street(deliveryAddressDocument.getStreet())
                    .number(deliveryAddressDocument.getNumber())
                    .complement(deliveryAddressDocument.getComplement())
                    .district(deliveryAddressDocument.getDistrict())
                    .city(deliveryAddressDocument.getCity())
                    .state(deliveryAddressDocument.getState())
                    .postalCode(deliveryAddressDocument.getPostalCode())
                    .build();
        }
        return null;
    }

}
