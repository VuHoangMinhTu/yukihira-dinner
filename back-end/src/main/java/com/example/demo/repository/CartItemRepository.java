package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  @EntityGraph(attributePaths = {"food", "food.category", "listOption"})
    @Query(
            """
    select c from CartItem c where c.cart.id = :cartId
"""
    )
    List<CartItem> getListCartItemByCartId(Long cartId);
}
