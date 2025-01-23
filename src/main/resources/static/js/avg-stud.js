const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}

document.getElementById('calculateAverageForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert('Please provide both start and end dates.');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/defense/average-students?startDate=${startDate}&endDate=${endDate}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const average = await response.json();
            document.getElementById('averageResult').textContent = `Average Students per Defense: ${average.toFixed(2)}`;
        } else {
            alert('Failed to fetch average students per defense.');
        }
    } catch (error) {
        console.error('Error fetching average students per defense:', error);
        alert('No students in this period.');
    }
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);