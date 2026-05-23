package in.sp.main.io;
import java.sql.Timestamp;

import in.sp.main.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
	private String userId;
	private String name;
	private String email;
	private Role role;
	private Timestamp createAt;
	private Timestamp updateAt;
}
