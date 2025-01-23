const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html'; // Redirect to login if no token
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Function to fetch assignments by supervisor
async function fetchAssignmentsBySupervisor(teacherId = null) {
    const assignmentsList = document.getElementById('supervisorAssignmentsList');
    assignmentsList.innerHTML = '';

    let url = 'http://localhost:8080/api/assignments/supervisor';
    if (teacherId) {
        url += `/${teacherId}`;
    }

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const assignments = await response.json();

            if (assignments.length === 0) {
                const li = document.createElement('li');
                li.textContent = teacherId
                    ? `No assignments found for supervisor ID: ${teacherId}`
                    : 'No assignments found for any supervisor.';
                assignmentsList.appendChild(li);
                return;
            }

            assignments.forEach(assignment => {
                const li = document.createElement('li');
                li.textContent = `Assignment ID: ${assignment.id}, Title: ${assignment.title}`;
                assignmentsList.appendChild(li);
            });
        } else {
            alert('Failed to fetch assignments for the supervisor.');
        }
    } catch (error) {
        console.error('Error fetching assignments by supervisor:', error);
    }
}

// Add event listener for fetching assignments for a specific supervisor
document.getElementById('supervisorForm').addEventListener('submit', (e) => {
    e.preventDefault();

    const teacherId = document.getElementById('teacherId').value;
    if (!teacherId) {
        alert('Please provide a valid supervisor (teacher) ID.');
        return;
    }

    fetchAssignmentsBySupervisor(teacherId);
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);