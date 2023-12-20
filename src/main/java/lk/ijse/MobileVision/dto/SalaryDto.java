package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SalaryDto {
    private String s_id;
    private String e_id;
    private String month;
    private String amount;
}
