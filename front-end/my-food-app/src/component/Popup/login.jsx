import React, { useState, useEffect } from "react";
import { Eye, EyeOff, X, LogIn, Mail, Lock, User } from "lucide-react";
import { useAuthContext } from "~/context/AuthContext";
import { getUserDetails } from "~/api/user/userDetails";
import { login } from "~/api/user/login";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import styles from "./LoginPopup.module.css";

const LoginPopup = ({ open, onClose }) => {
  const [mode, setMode] = useState("login");
  const [accountInfo, setAccountInfo] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const { setJwtToken } = useAuthContext();
  const navigate = useNavigate();

  // Đóng popup khi nhấn ESC
  useEffect(() => {
    const handleEsc = (e) => {
      if (e.key === "Escape") onClose();
    };
    window.addEventListener("keydown", handleEsc);
    return () => window.removeEventListener("keydown", handleEsc);
  }, [onClose]);

  if (!open) return null;

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(""); // Reset lỗi trước khi submit
    try {
      const response = await login(accountInfo, password);

      // Kiểm tra mã thành công 
      if (response.code === 200) {
        const token = response.data;
        setJwtToken(token);
        localStorage.setItem("access_token", token);

        // Lấy thông tin user sau khi login thành công
        const userDetails = await getUserDetails();
        if (userDetails !== null) {
          localStorage.setItem("userDetails", JSON.stringify(userDetails.data));
        }

        onClose();
        navigate("/");
      } else {
        setError("Thông tin đăng nhập không chính xác");
      }
    } catch (err) {
      console.error(err);
      setError("Lỗi kết nối hệ thống");
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.popup} onClick={(e) => e.stopPropagation()}>
        {/* Nút đóng */}
        <button className={styles.closeBtn} onClick={onClose}>
          <X size={24} />
        </button>

        <h2 className={styles.title}>
          {mode === "login" ? "Đăng Nhập" : "Quên Mật Khẩu"}
        </h2>

        <form onSubmit={handleLogin} className={styles.form}>
          {error && <div className={styles.errorBanner}>{error}</div>}

          <div className={styles.inputGroup}>
            <label>Tên đăng nhập / Email</label>
            <div className={styles.inputWrapper}>
              <User size={18} className={styles.inputIcon} />
              <input
                type="text"
                placeholder="Nhập tài khoản..."
                value={accountInfo}
                onChange={(e) => setAccountInfo(e.target.value)}
                required
              />
            </div>
          </div>

          {mode === "login" && (
            <div className={styles.inputGroup}>
              <label>Mật khẩu</label>
              <div className={styles.inputWrapper}>
                <Lock size={18} className={styles.inputIcon} />
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Nhập mật khẩu..."
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className={styles.toggleVisible}
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>
          )}

          <button type="submit" className={styles.submitBtn}>
            {mode === "login" ? <LogIn size={20} /> : <Mail size={20} />}
            <span>{mode === "login" ? "Đăng Nhập Ngay" : "Gửi Yêu Cầu"}</span>
          </button>

          <p
            className={styles.toggleMode}
            onClick={() => setMode(mode === "login" ? "forgotPassword" : "login")}
          >
            {mode === "login" ? "Quên mật khẩu?" : "Quay lại đăng nhập"}
          </p>
        </form>
      </div>
    </div>
  );
};

export default LoginPopup;