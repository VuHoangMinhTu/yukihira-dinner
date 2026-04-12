import React, { useEffect, useState } from 'react';
import { Link, useNavigate, Outlet } from 'react-router-dom';
import { UserCircle, LogIn, UserPlus, ShoppingCart, LogOut } from 'lucide-react';
import { request } from '../api/api';
import { useCart } from '../context/CartContext';
import styles from './Navbar.module.css';
// 1. Import LoginPopup vào đây
import LoginPopup from '~/component/Popup/login';

const Navbar = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);


    // 2. Thêm state để quản lý việc đóng/mở Popup
    const [isLoginOpen, setIsLoginOpen] = useState(false);

    const { totalQuantity, loadCart, clearCart } = useCart();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            request('/users/profile', 'GET')
                .then(data => setUser(data))
                .catch(() => {
                    localStorage.removeItem('token');
                    setUser(null);
                });

            // cart context already loads on provider mount; sync once here
            loadCart().catch(() => { });
        }
    }, []);
    useEffect(() => {
        const handleUnauthorized = () => {
            setIsLoginOpen(true);
        }
        window.addEventListener('unauthorized', handleUnauthorized);
        return () => {
            window.removeEventListener('unauthorized', handleUnauthorized);
        };
    }, [])

    const handleLogout = () => {
        localStorage.removeItem('token');
        setUser(null);
        clearCart();
        navigate('/');
    };

    return (
        <>
            <nav className={styles.navbar}>
                <div className={styles.container}>
                    <Link to="/" className={styles.logo}>
                        Yukihira Restaurant
                    </Link>

                    <div className={styles.navLinks}>
                        <Link to="/" className={styles.navLink}>Thực Đơn</Link>
                        <Link to="/orders" className={styles.navLink}>Đơn Hàng</Link>
                    </div>

                    <div className={styles.navActions}>
                        <Link to="/cart" className={styles.cartBtn}>
                            <ShoppingCart size={24} />
                            {totalQuantity > 0 && <span className={styles.cartBadge}>{totalQuantity}</span>}
                        </Link>

                        {user ? (
                            <div className={styles.userProfile}>
                                <UserCircle size={28} className={styles.avatarIcon} />
                                <span className={styles.userName}>{user.fullName || user.username}</span>
                                <button onClick={handleLogout} className={styles.logoutBtn}>
                                    <LogOut size={18} />
                                    <span>Đăng xuất</span>
                                </button>
                            </div>
                        ) : (
                            <div className={styles.authLinks}>
                                {/* 3. Thay đổi Link thành Button hoặc ngăn chặn chuyển trang để mở Popup */}
                                <button
                                    className={styles.authBtn}
                                    onClick={() => setIsLoginOpen(true)}
                                >
                                    <LogIn size={20} />
                                    <span>Đăng nhập</span>
                                </button>

                                <Link to="/register" className={styles.registerBtn}>
                                    <UserPlus size={20} />
                                    <span>Đăng ký</span>
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </nav>

            {/* 4. Chèn Component LoginPopup vào cuối */}
            <LoginPopup
                open={isLoginOpen}
                onClose={() => setIsLoginOpen(false)}
            />

            <Outlet />
        </>
    );
};

export default Navbar;