package in.sp.main.entity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import in.sp.main.Enums.PaymentMethod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "order_table")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String orderId;
	private String customerName;
	private String mobileNumber;
	private Double subTotal;
	private Double Tax;
	private Double grandTotal;
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "shop_id",nullable = false)
	private ShopEntity shop;

	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL , orphanRemoval = true)
	private List<OrderItemEntity> orderItems = new ArrayList<>();

	@Embedded
	private PaymentDetailEntity paymentDetailEntity;
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@PrePersist
	protected void onCreated() {
		this.orderId="ORD"+System.currentTimeMillis();
		this.createdAt=LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	}
}
