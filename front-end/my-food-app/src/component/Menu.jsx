import React, { useEffect, useState } from 'react';
import { request } from '../api/api';
import { useCart } from '../context/CartContext';
import styles from './Menu.module.css';

const Menu = () => {
    const [foods, setFoods] = useState([]);

    useEffect(() => {
        const fetchFoods = async () => {
            const data = await request('/api/food', null, 'GET');

            if (data) setFoods(data.data.content);
        };
        fetchFoods();
    }, []);

    const { addToCart } = useCart();

    const handleAddToCart = async (foodId) => {
        try {
            await addToCart(foodId, 1, []);
            alert("Đã thêm vào giỏ hàng!");
        } catch (err) {
            alert("Lỗi: " + (err instanceof Error ? err.message : String(err)));
        }
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Thực Đơn Yukihira</h1>
            <div className={styles.grid}>
                {foods.map(food => {
                    // 1. Xử lý lấy ảnh hiển thị (Lấy cái isPrimary hoặc cái đầu tiên)
                    const displayImage = food.imageFoodList?.find(img => img.isThumbnail)?.url
                        || food.imageFoodList?.[0]?.url;

                    return (
                        <div key={food.id} className={styles.card}>
                            <div className={styles.imagePlaceholder}>
                                {displayImage ? (
                                    <img src={displayImage} alt={food.name} />
                                ) : (
                                    <div className={styles.noImage}>Không có hình</div>
                                )}
                            </div>
                            <div className={styles.info}>
                                <h3>{food.name}</h3>
                                {/* 2. Sửa lại cách hiển thị giá tiền */}
                                <p className={styles.price}>
                                    {food.basePrice?.toLocaleString() || 0}đ
                                </p>
                                <p className={styles.stock}>Còn lại: {food.quantity}</p>

                                <button
                                    className={styles.addBtn}
                                    onClick={() => handleAddToCart(food.id)}
                                    disabled={food.quantity <= 0}
                                >
                                    {food.quantity > 0 ? "Thêm vào giỏ" : "Hết món"}
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default Menu;