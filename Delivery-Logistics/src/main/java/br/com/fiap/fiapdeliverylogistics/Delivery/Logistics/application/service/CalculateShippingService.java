package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.shipping.CalculateShippingResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalculateShippingService {

    @Value("${originZip}")
    private String originZip;
    @Value("${weightRate}")
    private double weightRate;
    @Value("${baseDeliveryTime}")
    private int baseDeliveryTime;
    @Value("${distanceRate}")
    private double distanceRate;

    public CalculateShippingResponseDto calculateShipping(CalculateShippingRequestDto requestDto) {
        int deliveryTime = estimateDeliveryTimeByRegion(requestDto.getDestinationZipCode());
        double shippingCost = (requestDto.getWeightProducts() * weightRate) + (deliveryTime * distanceRate);
        return new CalculateShippingResponseDto(shippingCost, deliveryTime);
    }
    private int estimateDeliveryTimeByRegion(String destinationZip) {
        int firstDigit = Character.getNumericValue(destinationZip.charAt(0));

        switch (firstDigit) {
            case 2 -> baseDeliveryTime += 2;
            case 3, 8 -> baseDeliveryTime += 3;
            case 4, 7, 9 -> baseDeliveryTime += 4;
            case 5 -> baseDeliveryTime += 5;
            case 6 -> baseDeliveryTime += 6;
            default -> {
                // No additional time for unexpected digits, 0 or 1
            }
        }

        return baseDeliveryTime;
    }

}
