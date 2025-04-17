function login() {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;

    const loginData = {
        id: userId,
        password: password
    };

    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.text())
    .then(data => {
        if (data === 'student') {
            localStorage.setItem('userType', 'student');
            window.location.href = "student_dashboard.html";
        } else if (data === 'admin') {
            localStorage.setItem('userType', 'admin');
            window.location.href = "admin_dashboard.html";
        } else {
            document.getElementById('error-msg').textContent = 'Invalid credentials, please try again.';
        }
    })
    .catch(error => console.error('Error:', error));
}

function logout() {
    localStorage.removeItem('userType');
    window.location.href = 'login.html';
}

window.onload = function() {
    const userType = localStorage.getItem('userType');
    if (userType !== 'student' && userType !== 'admin') {
        window.location.href = 'login.html';
        return;
    }

    fetch(`http://localhost:8080/api/auth/courses?userType=${userType}`)
        .then(response => response.json())
        .then(courses => {
            const courseList = document.getElementById('course-list');
            courses.forEach(course => {
                const courseCard = document.createElement('div');
                courseCard.classList.add('course-card');
                courseCard.innerHTML = `
                    <h4>${course.courseName}</h4>
                    <p>${course.description}</p>
                    <p><strong>Rating:</strong> <span class="rating">${course.rating}</span></p>
                    <p><strong>Duration:</strong> ${course.duration}</p>
                    <p><strong>Price:</strong> ${course.price}</p>
                `;
                courseList.appendChild(courseCard);
            });
        })
        .catch(error => console.error('Error:', error));
}
function fetchCourses() {
    fetch('/api/courses/all')
        .then(response => response.json())
        .then(data => displayCourses(data));
}

function searchCourses() {
    const courseName = document.getElementById('searchName').value;
    const minDuration = parseInt(document.getElementById('minDuration').value);
    const maxDuration = parseInt(document.getElementById('maxDuration').value);
    const sortByRating = document.getElementById('sortByRating').checked ? 'yes' : 'no';

    fetch(`/api/courses/search?courseName=${courseName}&minDuration=${minDuration}&maxDuration=${maxDuration}&sortByRating=${sortByRating}`)
        .then(response => response.json())
        .then(data => displayCourses(data));
}

function getRankedCourses() {
    fetch('/api/courses/ranked')
        .then(response => response.json())
        .then(data => displayCourses(data));
}

function getSortedCourses() {
    fetch('/api/courses/sorted')
        .then(response => response.json())
        .then(data => displayCourses(data));
}

function displayCourses(courses) {
    const tableBody = document.getElementById('courseTableBody');
    tableBody.innerHTML = '';

    courses.forEach(course => {
        const row = `<tr>
            <td>${course.courseName}</td>
            <td>${course.instructor}</td>
            <td>${course.duration}</td>
            <td>${course.rating}</td>
        </tr>`;
        tableBody.innerHTML += row;
    });
};
