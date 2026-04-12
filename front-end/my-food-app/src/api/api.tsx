const BASE_URL = 'http://localhost:8080';
export const request = async (endpoint: string, dataRequest: any, method: string) => {
    const token = localStorage.getItem('access_token');
    const headers = {
        'Content-Type': 'application/json',
        ... (token && { 'Authorization': `Bearer ${token}` }),
    };
    const payload = {
        method: method.toUpperCase(),
        headers,
        ...(method.toUpperCase() !== 'GET' && { body: JSON.stringify(dataRequest) }),
    }
    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, payload);
        // check khi token hết hạn hoặc không hợp lệ
        if (response.status === 401) {
            localStorage.removeItem('access_token');
            window.dispatchEvent(new Event("unauthorized"));
            return;
        }

        const data = await response.json();
        if (data.code === 200) {
            return data;
        }


    } catch (error) {
        if (error)
            console.error('Error:', error);
    }
}