const form = document.getElementById('loginForm');
const errorDiv = document.getElementById('error');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(form);

    try {
        const response = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', },
            body: JSON.stringify({
                username: formData.get('username'),
                password: formData.get('password')
            })
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            const role = data.roles;
            localStorage.setItem('authToken', token);
            localStorage.setItem('role', role);

            if (role === 'ROLE_STUDENT') {
                window.location.href = '../templates/student-dashboard.html';
            } else if (role === 'ROLE_TEACHER') {
                window.location.href = '../templates/teacher-dashboard.html';
            } else if (role === 'ROLE_ADMIN') {
                window.location.href = '../templates/admin-dashboard.html';
            } else if (role === 'ROLE_DEP') {
                window.location.href = '../templates/department-dashboard.html';
            }

        } else {
            errorDiv.style.display = 'block';
        }
    } catch (error) {
        console.error('Error during login:', error);
        errorDiv.style.display = 'block';
    }
});