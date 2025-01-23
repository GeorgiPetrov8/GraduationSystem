const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}
else if (roleLog !== "ROLE_DEP") {
    alert("You don't have access!");
    window.location.href = 'index.html';
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};


// View Assignments by Number
document.getElementById('viewAssignmentsBtn').addEventListener('click', async () => {

    const facNum = document.getElementById('facultyNumber').value;

    const response = await fetch(`http://localhost:8080/api/department/assignments/number?facNumber=${facNum}`, {
        method: 'GET',
        headers: apiHeaders,
    });

    if (response.ok) {
        const assignments = await response.json();
        const listElement = document.getElementById('assignmentsList');
        listElement.innerHTML = '';

        if (assignments.length === 0) {
            listElement.innerHTML = '<li>No assignments found.</li>';
            return;
        }

        assignments.forEach(assignment => {
            const li = document.createElement('li');
            li.textContent = `ID: ${assignment.id} | Name: ${assignment.student.name} | Position: ${assignment.title} | Status: ${assignment.status}`;
            listElement.appendChild(li);
        });
    } else {
        alert("Error fetching assignments.");
    }
});

// Handle approve assignment
document.getElementById('approveAssignmentBtn').addEventListener('click', async () => {
    const assignmentId = document.getElementById('assignmentId').value;

    try {
        const response = await fetch(`http://localhost:8080/api/department/assignments/approve/${assignmentId}`, {
            method: 'POST',
            headers: apiHeaders
        });

        if (response.ok) {
            alert(await response.text());
        } else {
            alert('Failed to approve assignment.');
        }
    } catch (error) {
        console.error('Error approving assignment:', error);
    }
});

// Handle disapprove assignment
document.getElementById('disapproveAssignmentBtn').addEventListener('click', async () => {
    const assignmentId = document.getElementById('assignmentId').value;

    try {
        const response = await fetch(`http://localhost:8080/api/department/assignments/disapprove/${assignmentId}`, {
            method: 'POST',
            headers: apiHeaders
        });

        if (response.ok) {
            alert(await response.text());
        } else {
            alert('Failed to disapprove assignment.');
        }
    } catch (error) {
        console.error('Error disapproving assignment:', error);
    }
});

document.getElementById('viewDefensesBtn').addEventListener('click', async () => {

    const facNum = document.getElementById('facultyNumberId').value;

    try {
        const response = await fetch(`http://localhost:8080/api/department/defense/number?facNumber=${facNum}`, {
            method: 'GET',
            headers: apiHeaders,
        });


        if (response.ok) {
            const defenses = await response.json();
            const listElement = document.getElementById('defensesList');
            listElement.innerHTML = '';

            if (defenses.length === 0) {
                listElement.innerHTML = '<li>No assignments found.</li>';
                return;
            }

            defenses.forEach(defense => {
                const li = document.createElement('li');
                li.textContent = `ID: ${defense.id} | Date: ${defense.date} | Number: ${defense.facultyNumber}`;
                listElement.appendChild(li);
            });
        } else {
            alert('Error fetching defenses.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while fetching defenses.');
    }
});

// Handle assign grades
document.getElementById("assignGrade").addEventListener("submit", async (e) => {
    e.preventDefault();
    const defenseId = document.getElementById('defenseId').value;
    const grades = document.getElementById('grade').value;

    try {
        const response = await fetch(`http://localhost:8080/api/department/defense/${defenseId}/grade`, {
            method: 'POST',
            headers: apiHeaders,
            body: JSON.stringify(grades)
        });

        if (response.ok) {
            alert('Grade assigned successfully!');
        } else {
            alert('Failed to assign grade.');
        }
    } catch (error) {
        console.error('Error assigning grade:', error);
    }
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);