"use client"

import { useState } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { MoonIcon, SunIcon, HelpCircleIcon, MenuIcon } from "lucide-react"
import { useTheme } from "next-themes"
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet"
import { HelpModal } from "./help-modal"
import { AuthButton } from "./auth/auth-button"

export function Header() {
  const { theme, setTheme } = useTheme()
  const [helpOpen, setHelpOpen] = useState(false)

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-white dark:bg-gray-950 shadow-sm">
      <div className="container mx-auto px-4 py-3 flex items-center justify-between">
        <div className="flex items-center">
          <Sheet>
            <SheetTrigger asChild className="md:hidden">
              <Button variant="ghost" size="icon">
                <MenuIcon className="h-5 w-5" />
                <span className="sr-only">Toggle menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-[240px] sm:w-[300px]">
              <nav className="flex flex-col gap-4 mt-8">
                <Link href="/" className="text-lg font-medium">
                  Home
                </Link>
                <Link href="/resources" className="text-lg font-medium">
                  Resources
                </Link>
                <Link href="#about-us" className="text-lg font-medium flex items-center gap-1">
                  <svg width="18" height="18" viewBox="0 0 18 18"><circle cx="9" cy="9" r="8" fill="#f472b6" opacity="0.2"/><text x="50%" y="55%" textAnchor="middle" fill="#f472b6" fontSize="12" fontWeight="bold" dy=".3em">♀</text></svg>
                  About Us
                </Link>
                <Link href="/profile" className="text-lg font-medium">
                  My Profile
                </Link>
              </nav>
            </SheetContent>
          </Sheet>

          <Link href="/" className="flex items-center ml-2 md:ml-0">
            <div className="relative h-8 w-8 mr-2">
              <div className="absolute inset-0 bg-purple-600 rounded-full opacity-90"></div>
              <div className="absolute inset-1 bg-white dark:bg-gray-950 rounded-full flex items-center justify-center">
                <span className="text-purple-600 font-bold text-xs">SG</span>
              </div>
            </div>
            <span className="font-bold text-xl text-purple-700 dark:text-purple-400">SafeGuard</span>
          </Link>
        </div>

        <nav className="hidden md:flex items-center space-x-6">
          <Link href="/" className="text-sm font-medium hover:text-purple-600 transition-colors">
            Home
          </Link>
          <Link href="/resources" className="text-sm font-medium hover:text-purple-600 transition-colors">
            Resources
          </Link>
          <Link href="/profile" className="text-sm font-medium hover:text-purple-600 transition-colors">
            My Profile
          </Link>
        </nav>

        <div className="flex items-center space-x-2">
          <Button variant="ghost" size="icon" onClick={() => setHelpOpen(true)} aria-label="Help">
            <HelpCircleIcon className="h-5 w-5" />
          </Button>

          <Button
            variant="ghost"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            aria-label="Toggle theme"
          >
            {theme === "dark" ? <SunIcon className="h-5 w-5" /> : <MoonIcon className="h-5 w-5" />}
          </Button>

          <AuthButton />
        </div>
      </div>

      <HelpModal open={helpOpen} onOpenChange={setHelpOpen} />
    </header>
  )
}
