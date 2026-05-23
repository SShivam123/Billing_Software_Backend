package in.sp.main.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class adminUpdateRequest {
	@NotBlank(message = "Shop name not be empty")
	private String shopName;
	@NotBlank(message = "name required")
	private String name;
	@NotBlank(message = "Email required")
	@Email(message = "please enter valid email")
	private String email;
	@NotBlank(message = "Address required")
	private String address;
}
