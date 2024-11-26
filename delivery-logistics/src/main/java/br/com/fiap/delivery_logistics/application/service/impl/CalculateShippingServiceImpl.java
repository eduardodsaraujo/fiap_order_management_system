package br.com.fiap.delivery_logistics.application.service.impl;

import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.service.CalculateShippingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Setter
public class CalculateShippingServiceImpl implements CalculateShippingService {

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
