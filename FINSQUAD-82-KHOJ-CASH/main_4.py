
import streamlit as st
import os
import subprocess
from streamlit.components.v1 import html

# SetUp 
st.set_page_config(page_title="User vs Business Launcher", layout="centered")

#Styling
html("""
<!DOCTYPE html>
<html>
<head>
  <style>
  body {
  margin: 0;
  overflow: hidden;
  background:linear-gradient(to bottom right, #d5f5e3, #eaffea);
  padding: 20px;
  border-radius: 20px;
}

  .emoji {
    position: fixed;
    font-size: 2em;
    animation: float 6s linear infinite;
    z-index: 9999;
  }

  @keyframes float {
    0% {
      transform: translateY(100vh) rotate(0deg);
      opacity: 1;
    }
    100% {
      transform: translateY(-10vh) rotate(360deg);
      opacity: 0;
    }
  }

 .typing-container {
  width: 80%;
  margin: 20px auto;
  padding: 20px;
  text-align: center;
  font-size: 2.5em;
  font-family: 'Courier New', Courier, monospace;
  color: #4b6cb7;
  background-color: rgba(255, 255, 255, 0.4); /* Light transparent background */
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
 

  .typed-text {
    font-weight: bold;
  }

  .typed-text::after {
    content: "|";
    animation: blink 0.7s infinite;
  }

  .typed-role::after {
    content: "|";
    animation: blink 0.7s infinite;
  }

  @keyframes blink {
    0%, 100% { opacity: 1; }
    50% { opacity: 0; }
  } 
  </style>
</head>
<body>

<!-- Emoji Generator -->
<script>
  const emojis = ['💰','💳','🎉','💸','🤑'];
  function createEmoji() {
    const emoji = document.createElement('div');
    emoji.classList.add('emoji');
    emoji.innerText = emojis[Math.floor(Math.random() * emojis.length)];
    emoji.style.left = Math.random() * 100 + "vw";
    emoji.style.animationDuration = (4 + Math.random() * 3) + "s";
    document.body.appendChild(emoji);
    setTimeout(() => emoji.remove(), 7000);
  }
  setInterval(createEmoji, 300);
</script>

<!-- Typing Welcome Text -->
<div class="typing-container">
  <span id="typed-text" class="typed-text"></span>
  <br>
  <span id="role-text" class="typed-role"></span>
</div>

<script>
  const message = "💳💰KHOJ-CASH💰💳";
  const roleMsg = "Enter Your Role:";
  let i = 0, j = 0;

  function typeWelcome() {
    if (i < message.length) {
      document.getElementById("typed-text").innerHTML += message.charAt(i);
      i++;
      setTimeout(typeWelcome, 100);
    } else {
      setTimeout(typeRole, 300);
    }
  }

  function typeRole() {
    if (j < roleMsg.length) {
      document.getElementById("role-text").innerHTML += roleMsg.charAt(j);
      j++;
      setTimeout(typeRole, 80);
    }
  }

  window.addEventListener('DOMContentLoaded', typeWelcome);
</script>

</body>
</html>
""", height=300)

#Role and Selection Part
user_type = st.selectbox("Choose your role:", ["User", "Business"], index=0)

user_script = "user.py"
business_script = "biz.py"
streamlit_path = r"C:\Users\bhatn\AppData\Roaming\Python\Python313\Scripts\streamlit.exe"

if user_type == "User":
    st.success("You've selected: **User**")
    if st.button("Run User Streamlit App"):
        if os.path.exists(user_script):
            with st.spinner("Launching User app..."):
                try:
                    subprocess.Popen([streamlit_path, "run", user_script])
                    st.info("User app is launching in a new window.")
                except Exception as e:
                    st.error(f"Failed to run Streamlit app: {e}")
        else:
            st.error(f"File not found: {user_script}")
else:
    st.success("You've selected: **Business**")
    if st.button("Run Business Streamlit App"):
        if os.path.exists(business_script):
            with st.spinner("Launching Business app..."):
                try:
                    subprocess.Popen([streamlit_path, "run", business_script])
                    st.info("Business app is launching in a new window.")
                except Exception as e:
                    st.error(f"Failed to run Streamlit app: {e}")
        else:
            st.error(f"File not found: {business_script}")
