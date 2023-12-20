package lk.ijse.MobileVision.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepairTm {
    private String r_id;
    private String e_id;
    private String c_tel;
    private String description;
    private String price;
    private String date;
}
