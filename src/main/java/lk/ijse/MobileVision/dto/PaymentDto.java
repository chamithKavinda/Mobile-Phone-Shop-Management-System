package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class PaymentDto {
    private String p_id;
    private String c_tel;
    private String o_id;
    private String date;
    private String description;
    private String amount;
}
