const authToken = localStorage.getItem('authToken'); // Retrieve the auth token

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html'; // Redirect to login if no token
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Function to fetch thesis by keyword
async function fetchThesesByKeyword(keyword = '') {
    const thesisResultsList = document.getElementById('thesisResultsList');
    thesisResultsList.innerHTML = '';

    const url = `http://localhost:8080/api/thesis/search${keyword ? `?keyword=${keyword}` : ''}`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const theses = await response.json();

            if (theses.length > 0) {
                theses.forEach(thesis => {
                    const li = document.createElement('li');
                    li.textContent = `ID: ${thesis.id} | Title: ${thesis.title} | Faculty Number: ${thesis.facultyNumber}`;
                    thesisResultsList.appendChild(li);
                });
            } else {
                const li = document.createElement('li');
                li.textContent = 'No theses found for the given keyword.';
                thesisResultsList.appendChild(li);
            }
        } else {
            alert('Failed to fetch theses.');
        }
    } catch (error) {
        console.error('Error searching theses:', error);
    }
}

// Add event listener for searching thesis by title keyword
document.getElementById('searchThesisForm').addEventListener('submit', (e) => {
    e.preventDefault();

    const keyword = document.getElementById('keyword').value.trim();
    if (!keyword) {
        alert('Please enter a keyword to search.');
        return;
    }

    fetchThesesByKeyword(keyword);
});


document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);