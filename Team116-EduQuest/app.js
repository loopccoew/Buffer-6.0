let role = ''; // Track if the user is an 'admin' or 'student'

document.addEventListener("DOMContentLoaded", function() {
    setRole('student'); // Default to student view on page load
});

function setRole(selectedRole) {
    role = selectedRole;

    if (role === 'student') {
        // Show student view, hide admin view
        document.getElementById('studentView').style.display = 'block';
        document.getElementById('adminView').style.display = 'none';
        loadCourses(); // Load courses for student view
    } else if (role === 'admin') {
        // Show admin view, hide student view
        document.getElementById('studentView').style.display = 'none';
        document.getElementById('adminView').style.display = 'block';
    }
}

function loadCourses() {
    fetch('http://localhost:8081/api/courses/all')
        .then(response => response.json())
        .then(courses => {
            displayCourses(courses);
        })
        .catch(error => console.error("Error loading courses:", error));
}

function displayCourses(courses) {
    const courseList = document.getElementById('courseList');
    courseList.innerHTML = ''; // Clear previous list

    if (courses.length === 0) {
        courseList.innerHTML = '<p>No courses available.</p>';
        return;
    }

    courses.forEach(course => {
        const courseCard = document.createElement('div');
        courseCard.classList.add('course-card');
        
        courseCard.innerHTML = `
            <h3>${course.courseName}</h3>
            <p><strong>Duration:</strong> ${course.duration}</p>
            <p><strong>Validity:</strong> ${course.validity}</p>
            <p><strong>Cost:</strong> ${course.costStatus}</p>
            <p><strong>Level:</strong> ${course.level}</p>
            <p><strong>Rating:</strong> ${course.rating}</p>
            <p><strong>Review:</strong> ${course.review}</p>
        `;

        courseList.appendChild(courseCard);
    });
}

function searchCourses() {
    const query = document.getElementById('courseSearch').value;

    if (query.length < 3) {
        loadCourses(); // If query is too short, load all courses
        return;
    }

    fetch(`http://localhost:8081/api/courses/search?courseName=${query}`)
        .then(response => response.json())
        .then(courses => {
            displayCourses(courses);
        })
        .catch(error => console.error("Error searching courses:", error));
}

// Fetch ranked courses by rating
function getRankedCourses() {
    fetch('http://localhost:8081/api/courses/ranked')
        .then(response => response.json())
        .then(courses => {
            displayCourses(courses);
        })
        .catch(error => console.error("Error fetching ranked courses:", error));
}

// Fetch sorted courses by duration
function getSortedCourses() {
    fetch('http://localhost:8081/api/courses/sorted')
        .then(response => response.json())
        .then(courses => {
            displayCourses(courses);
        })
        .catch(error => console.error("Error fetching sorted courses:", error));
}
