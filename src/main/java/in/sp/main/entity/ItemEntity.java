package in.sp.main.entity;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items_table")
public class ItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String itemId;
	private String name;
	private BigDecimal price;
	private String description;
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createAt;
	@UpdateTimestamp
	private Timestamp updateAt;
	private String imageurl;
	@ManyToOne
	@JoinColumn(name="category_id",nullable = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	private CategoryEntity category;
	
	@OneToOne(mappedBy = "item",cascade = CascadeType.ALL)
	private StockEntity stock;
}
