const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}
else if (roleLog !== "ROLE_TEACHER") {
    alert("You don't have access!");
    window.location.href = 'index.html';
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Handle create assignment form submission
document.getElementById('createAssignmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    // Get teacher
    const response = await fetch('http://localhost:8080/api/teacher/me', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${authToken}`, // Include the token in the Authorization header
            'Content-Type': 'application/json'
        }
    });

    let teacherName;

    if (response.ok) {
        const teacher = await response.json();
        teacherName = teacher.name;
        const facultyNumber = document.getElementById('assignmentStudents').value;

        const checkAssignment = await fetch(`http://localhost:8080/api/students/assignments/my?facNumber=${facultyNumber}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (!checkAssignment.ok) {
            alert('Error checking pending theses.');
            return;
        }

        const pendingTheses = await checkAssignment.json();
        const hasPendingThesis = pendingTheses.some(thesis => thesis.status === 'PENDING');

        if (hasPendingThesis) {
            alert("You already have a pending thesis. Please wait for it to be processed before submitting another.");
            return;
        }

        const assignment = {
            title: document.getElementById('assignmentTitle').value,
            goal: document.getElementById('assignmentGoal').value,
            tasks: document.getElementById('assignmentTasks').value,
            technologies: document.getElementById('assignmentTechnologies').value,
            facultyNumber: document.getElementById('assignmentStudents').value,
            supervisorName: teacherName
        };

        try {
            const response = await fetch('http://localhost:8080/api/teacher/assignments', {
                method: 'POST',
                headers: apiHeaders,
                body: JSON.stringify(assignment)
            });

            if (response.ok) {
                alert('Assignment created successfully!');
            } else {
                alert('Failed to create assignment.');
            }
        } catch (error) {
            console.error('Error creating assignment:', error);
        }
    } else {
        alert("Error fetching supervisor details.");
    }

});

// Handle submit review
document.getElementById('submitReviewForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    // Get teacher
    const response = await fetch('http://localhost:8080/api/teacher/me', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${authToken}`, // Include the token in the Authorization header
            'Content-Type': 'application/json'
        }
    });

    let teacherName;

    if (response.ok) {
        const teacher = await response.json();
        teacherName = teacher.name;
        const facultyNumber = document.getElementById('facultyNumberId').value;

        const review = {
            reviewerName: teacherName,
            facultyNumber: facultyNumber,
            text: document.getElementById('reviewContent').value,
            conclusion: document.getElementById('conclusion').value
        };

        // Check if it is approved
        const approvedAss = await fetch(`http://localhost:8080/api/teacher/assignments?facultyNumber=${facultyNumber}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`, // Ensure you include the auth token if needed
                'Content-Type': 'application/json'
            }
        });
        const assignment = await approvedAss.json();

        if (assignment.status !== 'APPROVED'){
            alert('Assignment not approved.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/teacher/reviews', {
                method: 'POST',
                headers: apiHeaders,
                body: JSON.stringify(review)
            });

            if (response.ok) {
                alert('Review submitted successfully!');
            } else {
                alert('Failed to submit review.');
            }
        } catch (error) {
            console.error('Error submitting review:', error);
        }
    } else {
        alert("Error fetching supervisor details.");
    }
});

// Handle schedule defense
document.getElementById('scheduleDefenseForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    // Get teacher
    const response = await fetch('http://localhost:8080/api/teacher/me', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${authToken}`, // Include the token in the Authorization header
            'Content-Type': 'application/json'
        }
    });

    let teacherName;

    if (response.ok) {
        const teacher = await response.json();
        teacherName = teacher.name;
        const facultyNum = document.getElementById('facultyNumber').value;

        const defense = {
            date: document.getElementById('defenseDate').value,
            facultyNumber: facultyNum,
            supervisorName: teacherName,
            departmentName: document.getElementById('studentDepartment').value
        };

        // Check for Assignment
        const approvedAssignment = await fetch(`http://localhost:8080/api/teacher/assignments?facultyNumber=${facultyNum}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`, // Ensure you include the auth token if needed
                'Content-Type': 'application/json'
            }
        });
        const assignment = await approvedAssignment.json();

        if (assignment.status !== 'APPROVED'){
            alert('Assignment not approved.');
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/api/teacher/defenses', {
                method: 'POST',
                headers: apiHeaders,
                body: JSON.stringify(defense)
            });

            if (response.ok) {
                alert('Defense scheduled successfully!');
            } else {
                alert('Failed to schedule defense.');
            }
        } catch (error) {
            console.error('Error scheduling defense:', error);
        }
    }else {
        alert("Error fetching supervisor details.");
    }
});


// Function to load departments
async function loadDepartments() {
    try {
        const response = await fetch('http://localhost:8080/api/teacher/departments', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const departments = await response.json();
            populateDepartmentDropdown('studentDepartment', departments);
        } else {
            console.error('Failed to fetch departments:', response.status);
        }
    } catch (error) {
        console.error('Error fetching departments:', error);
    }
}

// Populate a department dropdown with options
function populateDepartmentDropdown(selectId, departments) {
    const dropdown = document.getElementById(selectId);
    dropdown.innerHTML = ''; // Clear existing options

    departments.forEach(department => {
        const option = document.createElement('option');
        option.value = department.id;
        option.textContent = department.name;
        dropdown.appendChild(option);
    });
}

// Call the loadDepartments function on page load
document.addEventListener('DOMContentLoaded', () => {
    loadDepartments();
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);