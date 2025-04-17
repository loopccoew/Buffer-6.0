# ------------------ Scholarship and Club Recommender ------------------

# Define a base class for Scholarships
class Scholarship:
    def __init__(self, name, eligibility_criteria, start_date, end_date, applicant_group=None, scholarship_type=None, income_threshold=None):
        self.name = name
        self.eligibility_criteria = eligibility_criteria
        self.start_date = start_date
        self.end_date = end_date
        self.applicant_group = applicant_group
        self.scholarship_type = scholarship_type
        self.income_threshold = income_threshold

    def display_info(self):
        print(f"\n🎓 Scholarship: {self.name}")
        print(f"Eligibility Criteria: {', '.join(self.eligibility_criteria)}")

        print(f"Application Start Date: {self.start_date}")
        print(f"Application End Date: {self.end_date}")
        if self.applicant_group:
            print(f"Applicant Group: {self.applicant_group}")
        if self.scholarship_type:
            print(f"Scholarship Type: {self.scholarship_type}")
        print("-" * 60)

# ------------------ Government and Private Scholarships ------------------

def create_government_scholarships():
    return [
        Scholarship("Post-Matric Scholarship for SC/ST/OBC",
                    ["SC/ST/OBC", "Income below Rs. 2.5 lakh", "Enrolled in recognized institution"],
                    "01-Jan-2025", "31-Mar-2025", applicant_group="SC/ST/OBC", income_threshold=250000),
        Scholarship("Merit-cum-Means Scholarship",
                    ["Minority community", "50% marks minimum", "Income < Rs. 2.5 lakh"],
                    "01-Feb-2025", "30-Apr-2025", applicant_group="Minority Communities", income_threshold=250000),
        Scholarship("Central Sector Scheme",
                    ["80% marks in Class 12", "Income < Rs. 8 lakh", "College student"],
                    "15-Mar-2025", "15-Jun-2025", applicant_group="All Students", income_threshold=800000),
        Scholarship("Scholarship for Girls in Tech",
                    ["Female", "60% marks in Class 12", "Income < Rs. 6 lakh"],
                    "01-Apr-2025", "30-Sep-2025", applicant_group="All Students", income_threshold=600000),
        Scholarship("AICTE Saksham Scholarship",
                    ["40%+ disability", "AICTE approved college", "Income < Rs. 8 lakh"],
                    "01-Jan-2025", "31-Mar-2025", applicant_group="PWD Students", income_threshold=800000),
        Scholarship("AICTE Yashasvi Scheme",
                    ["Meritorious", "Pursuing Core Engineering"],
                    "01-Jan-2025", "31-Mar-2025", applicant_group="Core Engineering Students", income_threshold=None),
    ]

def create_private_scholarships():
    return [
        Scholarship("Reliance Jio Scholarship",
                    ["Enrolled in college", "Financial need"],
                    "01-Feb-2025", "31-May-2025", applicant_group="All Students", income_threshold=500000),
        Scholarship("Tata Trust Scholarship",
                    ["Excellent academics", "Financial need"],
                    "01-Jan-2025", "30-Apr-2025", applicant_group="All Students", income_threshold=500000),
        Scholarship("Infosys Scholarship",
                    ["Top 5% students", "Pursuing technical degree"],
                    "15-Mar-2025", "30-Jun-2025", applicant_group="Technical Students", income_threshold=500000),
        Scholarship("Tata Realty Scholarship for Girls",
                    ["Female B.Tech students", "Financial need"],
                    "01-Apr-2025", "30-Sep-2025", applicant_group="All Students", income_threshold=500000),
        Scholarship("IOCL Scholarship",
                    ["Meritorious", "Financial need"],
                    "15-Mar-2025", "30-Jun-2025", applicant_group="All Students", income_threshold=500000)
    ]

def categorize_scholarships(user_caste, user_income, user_disability=False):
    government_scholarships = create_government_scholarships()
    private_scholarships = create_private_scholarships()
    eligible = []

    # Categorizing Government Scholarships based on user input
    for s in government_scholarships:
        if s.income_threshold and user_income >= s.income_threshold:
            continue  # Skip scholarships that require lower income than user's

        if s.applicant_group == "All Students":
            eligible.append(s)
        elif s.applicant_group == "PWD Students" and user_disability:
            eligible.append(s)
        elif s.applicant_group == "Core Engineering Students":
            eligible.append(s)
        elif user_caste in s.applicant_group and user_income <= 250000:
            eligible.append(s)

    # Categorizing Private Scholarships based on user input
    for s in private_scholarships:
        if s.income_threshold and user_income >= s.income_threshold:
            continue  # Skip scholarships that require lower income than user's
        eligible.append(s)

    return eligible

# ------------------ Club Suggestion System ------------------

clubs_info = {
    "Coding": ["Loop CCEW", "Google Developer Student Club", "Code Club", "Mozilla Campus Club"],
    "AI/ML": ["Artificial Intelligence and Computer Vision Society", "Automation Club"],
    "Robotics": ["Team Aavej", "Automation Club"],
    "Aerospace": ["Team Vinidra", "Team Bharadwaj", "Team Nova"],
    "Photography": ["Photography Club"],
    "Music": ["Music Club - Swarashree"],
    "Dance": ["Dance Club - Insia"],
    "Entrepreneurship": ["E-Cell Yukta"],
    "Astronomy": ["Aasamant - The Astronomy Club"],
    "Debating": ["Debate Club", "The Debating Society"],
    "Finance": ["Finance and Economics Club"],
    "Books": ["Book Club"],
    "Mental Health": ["The Happy Hours"],
    "Drama": ["Cultural Club - Kalawant"]  # New Drama Category Added
}

def suggest_clubs(interests):
    suggested = set()
    for interest in interests:
        matched = clubs_info.get(interest.strip().capitalize())
        if matched:
            suggested.update(matched)
    return list(suggested)

# ------------------ Main Program Flow ------------------

def main():
    print("\n📘 Welcome to the Scholarship and Club Recommender (2025)!\n")
    print("What would you like to explore today?")
    print("1. Scholarships")
    print("2. Clubs")
    choice = input("Enter 1 or 2: ").strip()

    if choice == "1":
        print("\n🎓 Let's help you find eligible scholarships!\n")
        user_caste = input("Enter your category (SC/ST/OBC/General/Minority Communities): ").strip()
        try:
            user_income = int(input("Enter your annual family income (in Rs): ").strip())
        except ValueError:
            print("❌ Invalid income input. Please enter a number.")
            return

        disability_input = input("Do you have a disability of 40% or more? (yes/no): ").strip().lower()
        user_disability = disability_input in ["yes", "y"]

        scholarships = categorize_scholarships(user_caste, user_income, user_disability)
        print("\n🎓 Eligible Scholarships:\n")
        if scholarships:
            for s in scholarships:
                s.display_info()
        else:
            print("❌ No scholarships match your profile currently.")

    elif choice == "2":
        print("\n🎯 Let's suggest some clubs based on your interests!\n")
        print("Available interest categories: ")
        for interest in clubs_info.keys():
            print(f"- {interest}")
        interests_input = input("\nEnter your interests (comma-separated): ").strip()
        user_interests = [interest.strip() for interest in interests_input.split(",")]

        clubs = suggest_clubs(user_interests)
        print("\n🎯 Club Recommendations:\n")
        if clubs:
            for club in clubs:
                print(f"✅ {club}")
        else:
            print("❌ No clubs matched your interests.")

    else:
        print("❌ Invalid choice. Please enter 1 or 2.")

# ------------------ Run the Program ------------------

if __name__ == "__main__":
    main()
