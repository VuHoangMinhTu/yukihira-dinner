import React, { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { request } from "../api/api";

type CartItem = {
    cartItemId?: string | number; // id of the cart item on server
    productId?: string | number;
    productName?: string;
    basePrice?: number;
    extraPrice?: number;
    quantity: number;
    imageUrl?: string;
    categoryId?: number;
    categoryName?: string;
};

type CartContextType = {
    items: CartItem[];
    totalQuantity: number;
    loadCart: () => Promise<void>;
    addToCart: (productId: string | number, quantity?: number, selectedOptions?: any[]) => Promise<void>;
    updateQuantity: (cartItemId: string | number, quantity: number) => Promise<void>;
    removeFromCart: (cartItemId: string | number) => Promise<void>;
    clearCart: () => void;
};

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
    const ctx = useContext(CartContext);
    if (!ctx) throw new Error("useCart must be used within CartProvider");
    return ctx;
};

export const CartProvider = ({ children }: { children: ReactNode }) => {
    const [items, setItems] = useState<CartItem[]>([]);

    const normalize = (data: any) => {
        if (!data) return [];
        // server returns payload under data or directly; common key is items
        const raw = data.items || data.data?.items || data.data || data;
        if (!Array.isArray(raw)) return [];
        return raw.map((it: any) => ({
            cartItemId: it.cartItemId ?? it.id ?? it.cartItem?.id,
            productId: it.productId ?? it.product?.id,
            productName: it.productName ?? it.product?.name,
            basePrice: it.basePrice ?? it.product?.basePrice,
            extraPrice: it.extraPrice ?? 0,
            quantity: it.quantity ?? it.qty ?? it.count ?? 1,
            imageUrl: it?.imageUrl || "" ,
        }));
    };

    const loadCart = async () => {
        try {
            const data = await request('/cart/get-cart', null, 'GET');
            const list = normalize(data);
            setItems(list);
        } catch (err) {
            setItems([]);
        }
    };

    const addToCart = async (productId: string | number, quantity = 1, selectedOptions: any[] = []) => {
        try {
            const body = { productId: Number(productId), quantity: Number(quantity), selectedOptions };
            await request('/cart/add-to-cart', body, 'POST');
            await loadCart();
        } catch (err) {
            console.error(err);
            throw err;
        }
    };

    const updateQuantity = async (cartItemId: string | number, quantity: number) => {
        try {
            const endpoint = `/cart/update-quantity/${cartItemId}/${quantity}`;
            await request(endpoint, null, 'PUT');
            await loadCart();
        } catch (err) {
            console.error(err);
        }
    };

    const removeFromCart = async (cartItemId: string | number) => {
        try {
            // server may remove when quantity set to 0
            await updateQuantity(cartItemId, 0);
        } catch (err) {
            console.error(err);
        }
    };

    const clearCart = () => setItems([]);

    useEffect(() => {
        loadCart();
    }, []);

    const totalQuantity = items.reduce((s, it) => s + (it.quantity || 0), 0);

    return (
        <CartContext.Provider value={{ items, totalQuantity, loadCart, addToCart, updateQuantity, removeFromCart, clearCart }}>
            {children}
        </CartContext.Provider>
    );
};

export default CartContext;
