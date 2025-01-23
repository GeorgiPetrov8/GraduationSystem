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

// Helper function to add delete button
function createDeleteButton(id, id2, onDelete) {
    const button = document.createElement('button');
    button.textContent = 'Delete';
    button.style.marginLeft = '10px';
    button.style.background = 'red';
    button.addEventListener('click', () => onDelete(id, id2));
    return button;
}

// Function to handle teacher deletion
async function deleteTeacher(teacherId, userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/teachers/delete/${teacherId}`, {
            method: 'DELETE',
            headers: apiHeaders
        });

        if (response.ok) {
            alert(`Teacher with ID ${teacherId} deleted successfully.`);
            await deleteUser(userId);
            fetchTeachers();
        } else {
            alert('Failed to delete teacher.');
        }
    } catch (error) {
        console.error('Error deleting teacher:', error);
    }
}

// Function to handle student deletion
async function deleteStudent(studentId, userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/students/delete/${studentId}`, {
            method: 'DELETE',
            headers: apiHeaders
        });

        if (response.ok) {
            alert(`Student with ID ${studentId} deleted successfully.`);
            await deleteUser(userId);
            fetchStudents();
        } else {
            alert('Failed to delete student.');
        }
    } catch (error) {
        console.error('Error deleting student:', error);
    }
}

// Function to handle user deletion
async function deleteUser(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/users/delete/${userId}`, {
            method: 'DELETE',
            headers: apiHeaders
        });

        if (response.ok) {
            console.log(`User with ID ${userId} deleted successfully.`);
        } else {
            const error = await response.text();
            console.error(`Failed to delete user: ${error}`);
        }
    } catch (error) {
        console.error('Error deleting user:', error);
    }
}

// Function to handle department deletion
async function deleteDepartment(departmentId, id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/departments/delete/${departmentId}`, {
            method: 'DELETE',
            headers: apiHeaders
        });

        if (response.ok) {
            alert(`Department with ID ${departmentId} deleted successfully.`);
            fetchDepartments();
        } else {
            alert('Failed to delete department.');
        }
    } catch (error) {
        console.error('Error deleting department:', error);
    }
}

// Fetch all teachers on page load
async function fetchTeachers() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/teachers', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const teachers = await response.json();
            const teachersList = document.getElementById('teachersList');
            teachersList.innerHTML = '';

            teachers.forEach(teacher => {
                const li = document.createElement('li');
                li.textContent = `ID: ${teacher.id} | Name: ${teacher.name} | Position: ${teacher.user.roles}`;
                li.appendChild(createDeleteButton(teacher.id, teacher.user.id, deleteTeacher));
                teachersList.appendChild(li);
            });
        } else {
            alert('Failed to fetch teachers.');
        }
    } catch (error) {
        console.error('Error fetching teachers:', error);
    }
}

// Fetch all students on page load
async function fetchStudents() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/students', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const students = await response.json();
            const studentsList = document.getElementById('studentsList');
            studentsList.innerHTML = '';

            students.forEach(student => {
                const li = document.createElement('li');
                li.textContent = `ID: ${student.id} | Name: ${student.name} | Position: ${student.user.roles}`;
                li.appendChild(createDeleteButton(student.id, student.user.id, deleteStudent));
                studentsList.appendChild(li);
            });
        } else {
            alert('Failed to fetch students.');
        }
    } catch (error) {
        console.error('Error fetching students:', error);
    }
}

// Fetch all departments on page load
async function fetchDepartments() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/departments', {
            method: 'GET',
            headers: apiHeaders
        });

        if (response.ok) {
            const departments = await response.json();
            const departmentsList = document.getElementById('departmentsList');
            departmentsList.innerHTML = '';

            departments.forEach(department => {
                const li = document.createElement('li');
                li.textContent = `ID: ${department.id} | Name: ${department.name}`;
                li.setAttribute('data-id', department.id);
                li.appendChild(createDeleteButton(department.id, department.id, deleteDepartment));
                departmentsList.appendChild(li);
            });
        } else {
            alert('Failed to fetch departments.');
        }
    } catch (error) {
        console.error('Error fetching departments:', error);
    }
}

// Fetch data on page load
window.addEventListener('DOMContentLoaded', () => {
    fetchTeachers();
    fetchStudents();
    fetchDepartments();
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    // Remove the token from localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('role');
    window.location.href = 'http://localhost:63342/Graduation_System/Graduation_System.main/templates/index.html?_ijt=qnsqp64gvr47fhtaupv4ft9ekn&_ij_reload=RELOAD_ON_SAVE';}
);