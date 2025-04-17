from collections import deque

# --- Existing Job-related Classes ---

class Job:
    def __init__(self, employer_name, organization_name, job_title, category, vacancies, salary, requirements, qualifications):
        self.employer_name = employer_name
        self.organization_name = organization_name
        self.job_title = job_title
        self.category = category
        self.vacancies = vacancies
        self.salary = salary
        self.requirements = requirements
        self.qualifications = qualifications

class JobManager:
    def __init__(self):
        self.category_map = {}
        self.user_category_graph = {}

    def add_job(self, job):
        if job.category not in self.category_map:
            self.category_map[job.category] = []
        self.category_map[job.category].append(job)

    def connect_user_to_category(self, user, category):
        if user not in self.user_category_graph:
            self.user_category_graph[user] = set()
        self.user_category_graph[user].add(category)

    def recommend_jobs(self, user):
        if user not in self.user_category_graph:
            return []
        recommended_jobs = []
        visited = set()
        queue = deque(self.user_category_graph[user])

        while queue:
            current_category = queue.popleft()
            if current_category in visited:
                continue
            visited.add(current_category)
            jobs = self.get_jobs_by_category(current_category)
            recommended_jobs.extend(jobs[:2])  # Suggest top 2 jobs per category

        return recommended_jobs

    def get_jobs_by_category(self, category):
        jobs = self.category_map.get(category, [])
        return sorted(jobs, key=lambda job: job.salary, reverse=True)

# --- Women Profile Linked List ---

class WomanNode:
    def __init__(self, name, categories, location):
        self.name = name
        self.categories = categories
        self.location = location
        self.next = None

class WomenProfileLinkedList:
    def __init__(self):
        self.head = None

    def add_profile(self, name, categories, location):
        new_node = WomanNode(name, categories, location)
        if not self.head:
            self.head = new_node
        else:
            current = self.head
            while current.next:
                current = current.next
            current.next = new_node

    def get_profile(self, name):
        current = self.head
        while current:
            if current.name == name:
                return current
            current = current.next
        return None

# --- Application Tracking System ---

class StatusNode:
    def __init__(self, status):
        self.status = status
        self.next = None

class StatusLinkedList:
    def __init__(self):
        self.head = None

    def add_status(self, status):
        new_node = StatusNode(status)
        if not self.head:
            self.head = new_node
        else:
            temp = self.head
            while temp.next:
                temp = temp.next
            temp.next = new_node

    def get_statuses(self):
        statuses = []
        current = self.head
        while current:
            statuses.append(current.status)
            current = current.next
        return statuses

class Application:
    def __init__(self, job):
        self.job = job
        self.status_history = StatusLinkedList()
        self.status_history.add_status("Applied")

    def update_status(self, status):
        self.status_history.add_status(status)

    def get_status(self):
        return self.status_history.get_statuses()

class ApplicationManager:
    def __init__(self):
        self.applications = deque()  # Queue for applications

    def apply(self, job):
        app = Application(job)
        self.applications.append(app)
        print(f"Applied to {job.job_title} at {job.organization_name}")
        return app

    def update_status(self, index, status):
        if 0 <= index < len(self.applications):
            self.applications[index].update_status(status)
            print(f"Updated status to '{status}'")
        else:
            print("Invalid application index.")

    def view_applications(self):
        for idx, app in enumerate(self.applications):
            print(f"{idx}. {app.job.job_title} at {app.job.organization_name} | Status: {app.get_status()}")

# --- Data Collection ---

def collect_women_data():
    women_profile_list = WomenProfileLinkedList()
    n = int(input("Enter number of women profiles: "))
    for _ in range(n):
        name = input("Enter woman's name: ")
        categories = input("Enter categories of interest (comma-separated): ").split(',')
        location = input("Enter location: ")
        women_profile_list.add_profile(name, [cat.strip() for cat in categories], location)
    return women_profile_list

def collect_employer_data():
    employers = []
    n = int(input("Enter number of employers: "))
    for _ in range(n):
        employer_name = input("Enter employer's name: ")
        organization_name = input("Enter organization name: ")
        job_title = input("Enter job title: ")
        job_category = input("Enter job category (e.g., IT, Finance): ")
        vacancies = int(input("Enter number of vacancies: "))
        salary = float(input("Enter salary offered for the job: "))
        requirements = input("Enter job requirements (comma-separated): ").split(",")
        qualifications = input("Enter qualifications (comma-separated): ").split(",")
        employers.append({
            "employer_name": employer_name,
            "organization_name": organization_name,
            "job_title": job_title,
            "job_category": job_category,
            "vacancies": vacancies,
            "salary": salary,
            "requirements": [req.strip() for req in requirements],
            "qualifications": [qual.strip() for qual in qualifications]
        })
    return employers

# --- Main Driver Code ---

if __name__ == "__main__":
    women_profile_list = collect_women_data()
    employer_data = collect_employer_data()

    job_manager = JobManager()
    for employer in employer_data:
        job = Job(
            employer["employer_name"],
            employer["organization_name"],
            employer["job_title"],
            employer["job_category"],
            employer["vacancies"],
            employer["salary"],
            employer["requirements"],
            employer["qualifications"]
        )
        job_manager.add_job(job)

    woman_name = input("Enter the woman's name to get job recommendations: ")
    woman_profile = women_profile_list.get_profile(woman_name)

    if woman_profile:
        print(f"\n{woman_name}'s Profile Found!")
        print(f"Categories of interest: {woman_profile.categories}")
        print(f"Location: {woman_profile.location}")

        for category in woman_profile.categories:
            job_manager.connect_user_to_category(woman_name, category)

        print(f"\n\U0001F4E2 Recommended Jobs for {woman_name}:")
        recommended = job_manager.recommend_jobs(woman_name)
        if not recommended:
            print("No recommendations available.")
        else:
            app_manager = ApplicationManager()
            for i, job in enumerate(recommended):
                print(f"{i}. {job.job_title} at {job.organization_name} | ₹{job.salary}")

            choice = input("Do you want to apply to any jobs? (y/n): ").lower()
            if choice == 'y':
                indices = input("Enter job numbers to apply (comma-separated): ").split(',')
                for idx in indices:
                    idx = int(idx.strip())
                    if 0 <= idx < len(recommended):
                        app_manager.apply(recommended[idx])

                while True:
                    action = input("Do you want to update/view applications? (update/view/exit): ").lower()
                    if action == "update":
                        idx = int(input("Enter application index to update: "))
                        status = input("Enter new status (interview scheduled/selected/rejected): ")
                        app_manager.update_status(idx, status)
                    elif action == "view":
                        app_manager.view_applications()
                    elif action == "exit":
                        break
                    else:
                        print("Invalid choice.")
    else:
        print(f"Profile for {woman_name} not found.")
