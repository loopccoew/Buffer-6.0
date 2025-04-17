document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('reminderForm');
    const reminderList = document.getElementById('reminderList');
  
    loadReminders();
  
    form.addEventListener('submit', function (e) {
      e.preventDefault();
  
      const med = document.getElementById('medicine').value.trim();
      const hour = parseInt(document.getElementById('timeHour').value);
      const min = parseInt(document.getElementById('timeMin').value);
      const period = document.getElementById('timePeriod').value;
  
      if (!med || isNaN(hour) || isNaN(min)) {
        alert("Please fill in all fields correctly!");
        return;
      }
  
      const displayTime = `${hour.toString().padStart(2, '0')}:${min.toString().padStart(2, '0')} ${period}`;
      alert(`✅ Reminder set for ${med} at ${displayTime}`);
  
      const reminder = { medicine: med, time: displayTime };
  
      let reminders = JSON.parse(localStorage.getItem('reminders') || '[]');
      reminders.push(reminder);
      localStorage.setItem('reminders', JSON.stringify(reminders));
  
      addReminderToList(reminder, reminders.length - 1);
  
      let hour24 = hour;
      if (period === 'PM' && hour !== 12) hour24 += 12;
      if (period === 'AM' && hour === 12) hour24 = 0;
  
      const now = new Date();
      const target = new Date();
      target.setHours(hour24, min, 0, 0);
  
      const timeDiff = target.getTime() - now.getTime();
      if (timeDiff > 0) {
        setTimeout(() => {
          alert(`⏰ Time to take your medicine: ${med}`);
        }, timeDiff);
      }
  
      form.reset();
    });
  
    function loadReminders() {
      const reminders = JSON.parse(localStorage.getItem('reminders') || '[]');
      reminderList.innerHTML = '';
      reminders.forEach((rem, index) => addReminderToList(rem, index));
    }
  
    function addReminderToList(reminder, index) {
      const li = document.createElement('li');
      li.textContent = `${reminder.medicine} at ${reminder.time}`;
  
      const delBtn = document.createElement('button');
      delBtn.textContent = "Delete";
      delBtn.classList.add('delete-btn');
      delBtn.addEventListener('click', () => deleteReminder(index));
  
      li.appendChild(delBtn);
      reminderList.appendChild(li);
    }
  
    function deleteReminder(index) {
      let reminders = JSON.parse(localStorage.getItem('reminders') || '[]');
      reminders.splice(index, 1);
      localStorage.setItem('reminders', JSON.stringify(reminders));
      loadReminders();
    }

    function updateMedicineProgress(percentage) {
      const bar = document.getElementById('med-progress-bar');
      bar.style.width = `${percentage}%`;
      bar.innerText = `${percentage}%`;
    }
    // Call this whenever needed
    updateMedicineProgress(70); // example
    
  });
  