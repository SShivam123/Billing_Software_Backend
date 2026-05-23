package in.sp.main.io;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private String categoryId;
	private String name;
	private String description;
	private String bgColor;
	private String imageUrl;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Integer item;
	private String shopId;
}
