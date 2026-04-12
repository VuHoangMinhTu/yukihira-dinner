import React from 'react';
import { useCart } from '../context/CartContext';
import styles from './Cart.module.css';
import { useNavigate } from 'react-router-dom';
import { useCheckout } from '../context/CheckoutContext';
const Cart = () => {
    const { items, totalQuantity, updateQuantity, removeFromCart } = useCart();
    const { createOrder, checkout, setCheckout } = useCheckout();
    const navigate = useNavigate();

    const handleDecrease = (cartItemId, qty) => {
        if (qty <= 1) return removeFromCart(cartItemId);
        updateQuantity(cartItemId, qty - 1);
    };

    const handleIncrease = (cartItemId, qty) => {
        updateQuantity(cartItemId, qty + 1);
    };

    const handleCheckout = async () => {
        // If your API exposes a checkout endpoint, call it here.
        // For now navigate to orders or show a message.
        setCheckout({
            foodOrderList: items.map(item => ({
                productId: item.productId,
                productName: item.productName,
                quantity: item.quantity,
                categoryId: item.categoryId,
                price: item.basePrice * item.quantity + (item.extraPrice || 0),
                imageUrl: item.imageUrl,
                description: item.description

            })),
            returnUrl: window.location.origin + '/orders',
            cancelUrl: window.location.origin + '/cart',
            address: "",
            subtotal: totalQuantity,
            shipping: 0,
            tax: 0,
            total: totalQuantity,
            paymentMethod: "Chuyển khoản",
            coupons: []
        })
        const checkoutResponse = await createOrder(checkout);
        if (checkoutResponse) {
            console.log("Checkout response:", checkoutResponse);
            let url = checkoutResponse.data.checkoutUrl;
            window.location.href = url;
        } else {
            alert("Lỗi khi đặt hàng. Vui lòng thử lại.");
        }
    };

    return (
        <div className={styles.container}>
            <h2>Giỏ hàng</h2>
            {items.length === 0 ? (
                <p>Giỏ hàng trống.</p>
            ) : (
                <>
                    <ul className={styles.list}>
                        {items.map(item => (
                            <li key={item.cartItemId || item.id || item.productId} className={styles.item}>
                                <img src={item.imageUrl} alt={item.productName} className={styles.thumb} />
                                <div className={styles.info}>
                                    <div className={styles.name}>{item.productName}</div>
                                    <div className={styles.controls}>
                                        <button onClick={() => handleDecrease(item.cartItemId || item.id, item.quantity)}>-</button>
                                        <span className={styles.qty}>{item.quantity}</span>
                                        <button onClick={() => handleIncrease(item.cartItemId || item.id, item.quantity)}>+</button>
                                        <button className={styles.remove} onClick={() => removeFromCart(item.cartItemId || item.id)}>Xóa</button>
                                    </div>
                                </div>
                                <div className={styles.price}>{((item.basePrice * item.quantity || 0) + (item.extraPrice || 0)).toLocaleString()}đ</div>
                            </li>
                        ))}
                    </ul>

                    <div className={styles.footer}>
                        <div>Tổng số lượng: {totalQuantity}</div>
                        <button className={styles.checkout} onClick={handleCheckout}>Thanh toán</button>
                    </div>
                </>
            )}
        </div>
    );
};

export default Cart;
