package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto{
    private String id;
    private String description;
    private String unitPrice;
    private String qtyOnHand;
}
