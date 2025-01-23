const authToken = localStorage.getItem('authToken'); // Retrieve token from local storage
const roleLog = localStorage.getItem('role');

if (!authToken) {
    alert("You need to be logged in!");
    window.location.href = 'index.html';
}
else if (roleLog !== "ROLE_ADMIN") {
    alert("You don't have access!");
    window.location.href = 'index.html';
}

const apiHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${authToken}`
};

// Handle create user form submission
document.getElementById("createUserForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const user = {
        username: document.getElementById("userName").value,
        password: document.getElementById("password").value,
        roles: document.getElementById("userRole").value,
    };

    try {
        // Create user
        const userResponse = await fetch("http://localhost:8080/api/admin/users/new", {
            method: "POST",
            headers: apiHeaders,
            body: JSON.stringify(user),
        });

        if (!userResponse.ok) {
            const error = await userResponse.json();
            alert(`Failed to create user: ${error.message || "Unknown error"}`);
            return;
        }

        const createdUser = await userResponse.json();
        const userId = createdUser.id;

        const role = user.roles;
        let additionalData = { user: { id: userId } };

        let roleCreationUrl = "";
        if (role === "STUDENT") {
            additionalData = {
                ...additionalData,
                name: document.getElementById("studentName").value,
                facultyNumber: document.getElementById("studentFacultyNumber").value,
                department: { id: document.getElementById("studentDepartment").value },
            };
            roleCreationUrl = "http://localhost:8080/api/admin/students/new";
        } else if (role === "TEACHER") {
            additionalData = {
                ...additionalData,
                name: document.getElementById("teacherName").value,
                position: document.getElementById("teacherPosition").value,
                department: { id: document.getElementById("teacherDepartment").value },
            };
            roleCreationUrl = "http://localhost:8080/api/admin/teachers/new";
        }

        if (roleCreationUrl) {
            const roleResponse = await fetch(roleCreationUrl, {
                method: "POST",
                headers: apiHeaders,
                body: JSON.stringify(additionalData),
            });

            if (roleResponse.ok) {
                alert(`${role} created successfully!`);
            } else {
                const error = await roleResponse.json();
                alert(`Failed to create ${role.toLowerCase()}: ${error.message || "Unknown error"}`);
                await cleanupUser(userId);
            }
        } else {
            alert("User created successfully without additional role data.");
        }
    } catch (error) {
        console.error("Error during user creation:", error);
        alert("An unexpected error occurred.");
    }
});

// Cleanup user function
async function cleanupUser(userId) {
    try {
        const deleteResponse = await fetch(`http://localhost:8080/api/admin/users/delete/${userId}`, {
            method: "DELETE",
            headers: apiHeaders,
        });

        if (deleteResponse.ok) {
            console.log("User deleted successfully.");
        } else {
            const error = await deleteResponse.text();
            console.error(`Failed to delete user: ${error}`);
        }
    } catch (error) {
        console.error("Error during user deletion:", error);
    }
}


// Handle create department form submission
document.getElementById('createDepartmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const department = {
        name: document.getElementById('departmentName').value
    };

    try {
        const response = await fetch('http://localhost:8080/api/admin/departments/new', {
            method: 'POST',
            headers: apiHeaders,
            body: JSON.stringify(department)
        });

        if (response.ok) {
            alert('Department created successfully!');
            loadDepartments();
        } else {
            alert('Failed to create department.');
        }
    } catch (error) {
        console.error('Error creating department:', error);
    }
});

// Function to load departments
async function loadDepartments() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/departments', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const departments = await response.json();
            populateDepartmentDropdown('teacherDepartment', departments);
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

function toggleRoleFields() {
    const role = document.getElementById("userRole").value;

    // Student fields
    const studentFields = document.getElementById("studentFields");
    const studentInputs = studentFields.querySelectorAll("input, select");

    // Teacher fields
    const teacherFields = document.getElementById("teacherFields");
    const teacherInputs = teacherFields.querySelectorAll("input, select");

    if (role === "STUDENT") {
        studentFields.style.display = "block";
        teacherFields.style.display = "none";

        // Enable and require student inputs
        studentInputs.forEach(input => {
            input.disabled = false;
            input.required = true;
        });

        // Disable teacher inputs
        teacherInputs.forEach(input => {
            input.disabled = true;
            input.required = false;
        });
    } else if (role === "TEACHER") {
        teacherFields.style.display = "block";
        studentFields.style.display = "none";

        // Enable and require teacher inputs
        teacherInputs.forEach(input => {
            input.disabled = false;
            input.required = true;
        });

        // Disable student inputs
        studentInputs.forEach(input => {
            input.disabled = true;
            input.required = false;
        });
    } else {
        // Hide both fields and disable their inputs
        studentFields.style.display = "none";
        teacherFields.style.display = "none";

        studentInputs.forEach(input => {
            input.disabled = true;
            input.required = false;
        });

        teacherInputs.forEach(input => {
            input.disabled = true;
            input.required = false;
        });
    }
}


document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);