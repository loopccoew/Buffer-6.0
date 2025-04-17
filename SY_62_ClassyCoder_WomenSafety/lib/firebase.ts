import { initializeApp } from "firebase/app"
import { getAuth } from "firebase/auth"
import { getFirestore } from "firebase/firestore"
import { getAnalytics } from "firebase/analytics"

const firebaseConfig = {
  apiKey: "AIzaSyCNQ-NvTWQEk2FGBfn7MIUoR5twr1ToS9U",
  authDomain: "dsa-project-e5ff3.firebaseapp.com",
  projectId: "dsa-project-e5ff3",
  storageBucket: "dsa-project-e5ff3.firebasestorage.app",
  messagingSenderId: "169900810493",
  appId: "1:169900810493:web:0ab5efb1b20b0c0fc55e94",
  measurementId: "G-T8VE2BL4QV"
}

const app = initializeApp(firebaseConfig)
export const auth = getAuth(app)
export const db = getFirestore(app)
export const analytics = getAnalytics(app) 