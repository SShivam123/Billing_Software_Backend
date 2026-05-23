package in.sp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.entity.ItemEntity;
import in.sp.main.entity.StockEntity;

public interface StockRepository extends JpaRepository<StockEntity, Long>{

	StockEntity findByItem(ItemEntity existingItem);

}
