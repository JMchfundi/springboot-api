package co.ke.finsis.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String idNumber;
    private String groupName;
}
