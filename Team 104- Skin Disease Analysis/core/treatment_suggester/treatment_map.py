import csv

class TreatmentMap:
    def __init__(self, csv_path):
        self.map = {}
        with open(csv_path, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                disease = row['Disease'].strip()
                treatments = [t.strip() for t in row['Recommended Treatment'].split(',')]
                self.map[disease] = treatments  # Disease as key, list of treatments as value

    def get_treatment(self, disease_name):
        # Get the list of treatments for a given disease name
        return self.map.get(disease_name, ["Consult a specialist for tailored treatment."])

    def match_symptoms(self, input_symptoms):
        # Match input symptoms to diseases and return diseases with common symptoms
        input_set = set(s.strip().lower() for s in input_symptoms.split(','))
        matches = []

        for disease in self.map.keys():
            # Placeholder: Add symptom matching logic here
            matches.append(disease)

        return matches
