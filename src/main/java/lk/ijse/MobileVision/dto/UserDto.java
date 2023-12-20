package lk.ijse.MobileVision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserDto {
    private String UserName;
    private String Email;
    private String Password;
}
