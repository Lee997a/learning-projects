package com.example.restaurantMap.respository;

import com.example.restaurantMap.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Restaurant> findByUserIdAndCategory(Long userId, String category);
}
