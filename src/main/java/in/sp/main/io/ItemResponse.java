package in.sp.main.io;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
	private String name;
	private String itemid;
	private BigDecimal price;
	private String description;
	private String categoryName;
	private String categoryId;
	private String imageurl;
	private Timestamp createAt;
	private Timestamp updateAt;
	private Integer stock;
}
