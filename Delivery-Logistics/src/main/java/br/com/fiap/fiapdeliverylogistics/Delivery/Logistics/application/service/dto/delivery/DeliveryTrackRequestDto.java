package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackRequestDto {
    private Long orderId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
