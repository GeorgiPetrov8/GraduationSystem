const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html'; // Redirect to login if no token
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Function to fetch defenses by grade range
async function fetchDefensesByGradeRange(minGrade = 0, maxGrade = 10) {
    const defensesList = document.getElementById('defensesList');
    defensesList.innerHTML = '';

    try {
        const response = await fetch(`http://localhost:8080/api/defense/grades?minGrade=${minGrade}&maxGrade=${maxGrade}`, {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const defenses = await response.json();

            if (defenses.length === 0) {
                defensesList.innerHTML = '<li>No defenses found in the specified grade range.</li>';
                return;
            }

            defenses.forEach(defense => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>Defense ID:</strong> ${defense.id}<br>
                    <strong>Grade:</strong> ${defense.grades}<br>
                    <strong>Student Name:</strong> ${defense.students.name || 'Unknown'}
                `;
                defensesList.appendChild(li);
            });
        } else {
            alert('Failed to fetch defenses.');
        }
    } catch (error) {
        console.error('Error fetching defenses:', error);
    }
}

// Add event listener for fetching defenses based on user input
document.getElementById('gradeRangeForm').addEventListener('submit', (e) => {
    e.preventDefault();

    const minGrade = parseFloat(document.getElementById('minGrade').value);
    const maxGrade = parseFloat(document.getElementById('maxGrade').value);

    if (isNaN(minGrade) || isNaN(maxGrade) || minGrade < 0 || maxGrade > 10 || minGrade > maxGrade) {
        alert('Please provide valid grade values (0-10) with minGrade <= maxGrade.');
        return;
    }

    fetchDefensesByGradeRange(minGrade, maxGrade);
});


document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);