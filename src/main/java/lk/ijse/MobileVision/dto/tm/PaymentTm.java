package lk.ijse.MobileVision.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTm {
    private String p_id;
    private String c_tel;
    private String o_id;
    private String date;
    private String description;
    private String amount;
}
