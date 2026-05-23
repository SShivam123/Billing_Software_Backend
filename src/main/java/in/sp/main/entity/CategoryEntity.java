package in.sp.main.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name="category_table")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;

	private String categoryId;

	@Column(unique = true)
	private String name;

	private String description;
	private String bgColor;
	private String imageUrl;

	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdAt;
	@UpdateTimestamp
	private Timestamp updatedAt;

	@ManyToOne
	@JoinColumn(name="shopId",nullable = false)
	private ShopEntity shop;
}
