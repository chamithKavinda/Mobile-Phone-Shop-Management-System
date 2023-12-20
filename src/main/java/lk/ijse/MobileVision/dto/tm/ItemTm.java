package lk.ijse.MobileVision.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemTm {
    private String id;
    private String description;
    private String unitPrice;
    private String qtyOnHand;
}
