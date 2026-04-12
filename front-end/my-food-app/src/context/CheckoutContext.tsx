import React, { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { request } from "../api/api";

type CheckoutItem = {
    productId?: number | string;
    categoryId?: number | string;
    productName?: string;
    description?: string | null;
    quantity: number;
    price?: number;
    imageUrl?: string;
};

type CheckoutData = {
    foodOrderList: CheckoutItem[];
    returnUrl?: string;
    cancelUrl?: string;
    address?: string;
    subtotal: number;
    shipping?: number;
    tax?: number;
    total: number;
    paymentMethod?: string;
    coupons?: string[];
};

type CheckoutContextType = {
    checkout: CheckoutData | null;
    setCheckout: React.Dispatch<React.SetStateAction<CheckoutData | null>>;
    loading: boolean;
    loadCheckout: () => Promise<void>;
    createOrder: (payload: any) => Promise<any>;
    clearCheckout: () => void;
    updateAddress: (address: string) => Promise<void>;
};

const CheckoutContext = createContext<CheckoutContextType | undefined>(undefined);

export const useCheckout = () => {
    const ctx = useContext(CheckoutContext);
    if (!ctx) throw new Error("useCheckout must be used within CheckoutProvider");
    return ctx;
};

export const CheckoutProvider = ({ children }: { children: ReactNode }) => {
    const [checkout, setCheckout] = useState<CheckoutData | null>(null);
    const [loading, setLoading] = useState(false);

    const normalize = (data: any): CheckoutData => {
        if (!data) return { 
            foodOrderList: [], 
            returnUrl: 'http://localhost:5174/orders', 
            cancelUrl: 'http://localhost:5174',
            address: '',
            subtotal: 0, 
            shipping: 0, 
            tax: 0, 
            total: 0 };
        // adapt to your backend shape as needed
        return {
            foodOrderList: data.foodOrderList || [],
            returnUrl: data.returnUrl || 'http://localhost:5174/orders',
            cancelUrl: data.cancelUrl || 'http://localhost:5174',
            address: data.address || '',
            subtotal: data.subtotal ?? 0,
            shipping: data.shipping ?? 0,
            tax: data.tax ?? 0,
            total: data.total ?? (data.subtotal ?? 0) + (data.shipping ?? 0) + (data.tax ?? 0),
            paymentMethod: data.paymentMethod,
            coupons: data.coupons || [],
        };
    };

    const loadCheckout = async () => {
        setLoading(true);
        try {
            // Placeholder endpoint — replace with your checkout API
            const data = await request('/checkout/get-checkout', null, 'GET');
            setCheckout(normalize(data));
        } catch (err) {
            console.error('loadCheckout error', err);
            setCheckout(null);
        } finally {
            setLoading(false);
        }
    };

    const createOrder = async (payload: any) => {
        setLoading(true);
        try {
            // Placeholder endpoint — replace with your checkout create-order API
            const res = await request('/order/create', payload, 'POST');
            // Optionally reload checkout/clear cart after successful order
            return res;
        } catch (err) {
            console.error('createOrder error', err);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const updateAddress = async (address: string) => {
        setLoading(true);
        try {
            // Placeholder endpoint — replace with your update address API
            await request('/checkout/update-address', { address }, 'PUT');
            setCheckout(prev => prev ? { ...prev, address } : prev);
        } catch (err) {
            console.error('updateAddress error', err);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const applyCoupon = async (code: string) => {
        setLoading(true);
        try {
            // Placeholder endpoint — replace with your coupon API
            await request('/checkout/apply-coupon', { code }, 'POST');
            await loadCheckout();
        } catch (err) {
            console.error('applyCoupon error', err);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const clearCheckout = () => setCheckout(null);

    useEffect(() => {
        loadCheckout();
    }, []);

    return (
        <CheckoutContext.Provider value={{ checkout, setCheckout, loading, loadCheckout, createOrder, updateAddress, clearCheckout }}>
            {children}
        </CheckoutContext.Provider>
    );
};

export default CheckoutContext;
