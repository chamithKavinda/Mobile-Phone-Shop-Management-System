package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class RepairDto {
    private String r_id;
    private String e_id;
    private String description;
    private String price;
    private String date;
    private String c_tel;
}
