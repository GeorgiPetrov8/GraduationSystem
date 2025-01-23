
const authToken = localStorage.getItem('authToken'); // Retrieve the auth token

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html'; // Redirect to login if no token
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Count negative reviews
document.getElementById('countNegativeReviewsBtn').addEventListener('click', async () => {
    try {
        const response = await fetch('http://localhost:8080/api/assignments/negative/count', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const count = await response.json();
            document.getElementById('negativeReviewsCount').textContent = `Negative Reviews Count: ${count}`;
        } else {
            alert('Failed to fetch negative reviews count.');
        }
    } catch (error) {
        console.error('Error counting negative reviews:', error);
    }
});

// Handle fetching successful defenses by teacher
document.getElementById('teacherDefenseForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const teacherId = document.getElementById('teacherId').value;

    try {
        const response = await fetch(`http://localhost:8080/api/defense/successful/${teacherId}`, {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const count = await response.json();
            document.getElementById('successfulCount').textContent = `Successful Defenses: ${count}`;
        } else {
            alert('Failed to fetch successful defenses count.');
        }
    } catch (error) {
        console.error('Error fetching successful defenses count:', error);
    }
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);
