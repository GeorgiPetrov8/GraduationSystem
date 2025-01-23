const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}

// Function to fetch graduated students
async function fetchGraduatedStudents(startDate, endDate) {
    const listElement = document.getElementById('graduatedStudentsList');
    listElement.innerHTML = '';

    // Use default date range if not provided
    const defaultStartDate = '2000-01-01';
    const defaultEndDate = new Date().toISOString().split('T')[0];
    const queryStartDate = startDate || defaultStartDate;
    const queryEndDate = endDate || defaultEndDate;

    try {
        const response = await fetch(`http://localhost:8080/api/students/graduated?startDate=${queryStartDate}&endDate=${queryEndDate}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (response.ok) {
            const students = await response.json();

            if (students.length === 0) {
                listElement.innerHTML = '<li>No graduated students found for the selected period.</li>';
                return;
            }

            students.forEach(student => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>Name:</strong> ${student.name}<br>
                    <strong>Graduated Date:</strong> ${student.graduated || 'N/A'}<br>
                `;
                listElement.appendChild(li);
            });
        } else {
            alert("Error fetching graduated students.");
        }
    } catch (error) {
        console.error("Error fetching graduated students:", error);
    }
}

// Add event listener for fetching graduated students based on user input
document.getElementById('getGraduatedBtn').addEventListener('click', () => {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert("Please provide both start and end date.");
        return;
    }

    fetchGraduatedStudents(startDate, endDate);
});


document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);