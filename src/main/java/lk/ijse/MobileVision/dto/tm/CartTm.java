package lk.ijse.MobileVision.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CartTm {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;
    private Button btn;
}
