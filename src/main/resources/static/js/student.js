const authToken = localStorage.getItem('authToken'); // Get the token from localStorage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}
else if (roleLog !== "ROLE_STUDENT") {
    alert("You don't have access!");
    window.location.href = 'index.html';
}

// View My Assignments

document.getElementById('viewMyAssignmentsBtn').addEventListener('click', async () => {

    const facNum = document.getElementById('facultyNumberId').value;

    try {
        const response = await fetch(`http://localhost:8080/api/students/assignments/my?facNumber=${facNum}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
            }
        });

        if (response.ok) {
            const defenses = await response.json();
            const listElement = document.getElementById('myAssignmentsList');
            listElement.innerHTML = '';

            if (defenses.length === 0) {
                listElement.innerHTML = '<li>No assignments found.</li>';
                return;
            }

            defenses.forEach(defense => {
                const li = document.createElement('li');
                li.textContent = `ID: ${defense.id} | Faculty Number: ${defense.facultyNumber} | Status: ${defense.status}`;
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

// Submit Thesis
document.getElementById('submitThesisForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const facultyNumber = document.getElementById('facultyNumber').value;

    try {
        const checkResponse = await fetch(`http://localhost:8080/api/students/assignments/my?facNumber=${facultyNumber}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (!checkResponse.ok) {
            alert('Error checking pending theses.');
            return;
        }

        const pendingTheses = await checkResponse.json();
        const hasPendingThesis = pendingTheses.some(thesis => thesis.status === 'PENDING');

        if (pendingTheses.length === 0 || hasPendingThesis) {
            alert("You can't submit an assignment.");
            return;
        }

        const thesisData = {
            title: document.getElementById('thesisTitle').value,
            text: document.getElementById('thesisContent').value,
            facultyNumber: facultyNumber
        };

        const submitResponse = await fetch('http://localhost:8080/api/students/thesis', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(thesisData)
        });

        if (submitResponse.ok) {
            alert("Thesis submitted successfully!");
            document.getElementById('submitThesisForm').reset();
        } else {
            alert("Error submitting thesis.");
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while submitting the thesis.');
    }
});



document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);