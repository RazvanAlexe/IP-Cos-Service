package com.buyit.cart.services;

import com.buyit.cart.DTOs.CartDTO;
import com.buyit.cart.entities.CartEntity;
import com.buyit.cart.repositories.CartRepository;
import com.buyit.cartItem.entities.CartItemEntity;
import com.buyit.cartItem.DTOs.CartItemDTO;
import com.buyit.cartItem.repositories.CartItemRepository;
import com.buyit.orders.repositories.OrderRepository;
import com.buyit.product.entities.ProductEntity;
import com.buyit.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    @Autowired
    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public CartDTO removeProductFromCart(String productId, String cartId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);

        if (cartEntity.isEmpty()) return null;

        List<CartItemEntity> itemEntities = cartItemRepository.findByCartId(cartId);

        for (CartItemEntity itemEntity : itemEntities) {
            if (itemEntity.getProductId().equals(productId)) {
                int quantity = itemEntity.getQuantity();
                int prodPrice = (int) itemEntity.getPrice() / quantity;

                if (quantity == 1) {
                    cartItemRepository.delete(itemEntity);
                } else {
                    itemEntity.setQuantity(quantity - 1);
                    itemEntity.setPrice(prodPrice * (quantity - 1));
                    itemEntity = cartItemRepository.save(itemEntity);
                }
                cartEntity.get().setTotalPrice(cartEntity.get().getTotalPrice() - prodPrice);
                cartEntity = Optional.of(cartRepository.save(cartEntity.get()));
                return getCartById(cartId);
            }
        }
        return null;
    }

    public CartDTO addProductInCart(String productId, String cartId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);

        if (cartEntity.isEmpty()) return null;

        List<CartItemEntity> itemEntities = cartItemRepository.findByCartId(cartId);

        boolean itemExists = false;
        for (CartItemEntity itemEntity : itemEntities) {
            if (itemEntity.getProductId().equals(productId)) {
                int quantity = itemEntity.getQuantity();
                int prodPrice = (int) itemEntity.getPrice() / quantity;

                itemEntity.setQuantity(quantity + 1);
                itemEntity.setPrice(prodPrice * (quantity + 1));
                itemEntity = cartItemRepository.save(itemEntity);

                cartEntity.get().setTotalPrice(cartEntity.get().getTotalPrice() + prodPrice);
                cartEntity = Optional.of(cartRepository.save(cartEntity.get()));

                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            CartItemEntity itemEntity = new CartItemEntity();

            int size = itemEntities.size();
            itemEntity.setId("ciid" + (size + 1));

            int price = productRepository.findById(productId).get().getPrice();
            itemEntity.setPrice(price);

            itemEntity.setQuantity(1);
            itemEntity.setCartId(cartId);
            itemEntity.setProductId(productId);

            itemEntity = cartItemRepository.save(itemEntity);

            cartEntity.get().setTotalPrice(cartEntity.get().getTotalPrice() + price);
            cartEntity = Optional.of(cartRepository.save(cartEntity.get()));
        }
        return getCartById(cartId);
    }

    public CartDTO getCartById(String cartId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);

        if (cartEntity.isEmpty()) return null;

        CartDTO cartObject = new CartDTO();

        cartObject.setTotalPrice(cartEntity.get().getTotalPrice());

        List<CartItemEntity> itemEntities = cartItemRepository.findByCartId(cartId);

        for (CartItemEntity itemEntity : itemEntities) {
            CartItemDTO itemObject = new CartItemDTO();
            Optional<ProductEntity> productEntity = productRepository.findById(itemEntity.getProductId());

            if (productEntity.isEmpty()) return null;

            itemObject.setPrice(itemEntity.getPrice());
            itemObject.setQuantity(itemEntity.getQuantity());
            itemObject.setProductEntity(productEntity.get());
            cartObject.getItems().add(itemObject);
        }

        return cartObject;
    }

    public CartEntity addNewCart() {
        List<CartEntity> cartEntities = this.cartRepository.findAll();
        int size = cartEntities.size() + 1;
        CartEntity cartEntity = new CartEntity();
        cartEntity.setId("cid" + size);
        cartEntity.setTotalPrice(0);
        return this.cartRepository.save(cartEntity);
    }
}
