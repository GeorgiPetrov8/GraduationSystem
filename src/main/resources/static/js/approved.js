const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}

// Function to fetch and display approved assignments
async function fetchApprovedAssignments() {

    try {
        const response = await fetch('http://localhost:8080/api/students/assignments/approved', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
            }
        });

        if (response.ok) {
            const assignments = await response.json();
            const listElement = document.getElementById('assignmentsList');
            listElement.innerHTML = '';

            if (assignments.length === 0) {
                listElement.innerHTML = '<li>No approved assignments found.</li>';
                return;
            }

            assignments.forEach(assignment => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>Title:</strong> ${assignment.title}<br>
                    <strong>Goal:</strong> ${assignment.goal}<br>
                    <strong>Tasks:</strong> ${assignment.tasks}<br>
                    <strong>Technologies:</strong> ${assignment.technologies}<br>
                    <strong>Faculty Number:</strong> ${assignment.facultyNumber}<br>
                    <strong>Student Name:</strong> ${assignment.student?.name || 'N/A'}<br>
                    <strong>Supervisor Name:</strong> ${assignment.supervisorName}<br>
                    <strong>Supervisor Position:</strong> ${assignment.supervisor?.position || 'N/A'}<br>
                    <strong>Status:</strong> ${assignment.status}<br>
                `;
                listElement.appendChild(li);
            });
        } else {
            alert("Error fetching assignments.");
        }
    } catch (error) {
        console.error("Error fetching approved assignments:", error);
    }
}

// Run the fetch function on page load
window.addEventListener('DOMContentLoaded', fetchApprovedAssignments);


document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);