package lk.ijse.MobileVision.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryTm {
    private String s_id;
    private String e_id;
    private String month;
    private String amount;
}
