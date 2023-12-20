package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CustomerDto {
    private String tel;
    private String name;
    private String address;
    private String id;
}
