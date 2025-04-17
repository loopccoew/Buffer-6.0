"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { LogInIcon, LogOutIcon, UserIcon } from "lucide-react"
import { auth } from "../../lib/firebase"
import { signInWithPopup, GoogleAuthProvider, signOut, User } from "firebase/auth"
import { useRouter } from "next/navigation"

export function AuthButton() {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)
  const router = useRouter()

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user: User | null) => {
      setUser(user)
      setLoading(false)
    })

    return () => unsubscribe()
  }, [])

  const handleSignIn = async () => {
    try {
      const provider = new GoogleAuthProvider()
      await signInWithPopup(auth, provider)
      router.refresh()
    } catch (error) {
      console.error("Error signing in:", error)
    }
  }

  const handleSignOut = async () => {
    try {
      await signOut(auth)
      router.refresh()
    } catch (error) {
      console.error("Error signing out:", error)
    }
  }

  if (loading) {
    return <Button variant="outline" size="sm" disabled>Loading...</Button>
  }

  return (
    <div className="flex items-center gap-2">
      {user ? (
        <>
          <Button variant="outline" size="sm" className="flex items-center gap-1">
            <UserIcon className="h-4 w-4" />
            {user.displayName?.split(" ")[0] || "User"}
          </Button>
          <Button 
            variant="outline" 
            size="sm" 
            onClick={handleSignOut}
            className="flex items-center gap-1"
          >
            <LogOutIcon className="h-4 w-4" />
            Sign Out
          </Button>
        </>
      ) : (
        <Button 
          variant="outline" 
          size="sm" 
          onClick={handleSignIn}
          className="flex items-center gap-1"
        >
          <LogInIcon className="h-4 w-4" />
          Sign In
        </Button>
      )}
    </div>
  )
} 