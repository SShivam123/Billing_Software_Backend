package in.sp.main.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entity.CategoryEntity;
import in.sp.main.entity.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long>{

	Optional<ItemEntity> findByItemId(String id);

	Integer countBycategoryId(Long categoryId);

	boolean existsByNameIgnoreCaseAndCategoryId(String name,Long categoryId);

	@Query("Select i from ItemEntity i where i.category.shop.id=:shopId")
	List<ItemEntity> FindAllItemByShop(Long shopId);

	Integer countByCategory(CategoryEntity entity);
}
