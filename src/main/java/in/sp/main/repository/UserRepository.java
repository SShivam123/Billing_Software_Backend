package in.sp.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.entity.ShopEntity;
import in.sp.main.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long>{
	Optional<UserEntity> findByEmail(String Email);

	Optional<UserEntity> findByUserId(String id);

	boolean existsByEmail(String email);

	List<UserEntity> findByShop(ShopEntity shop);

	Optional<UserEntity> findByUserIdAndShop(String id, ShopEntity shop);


}
