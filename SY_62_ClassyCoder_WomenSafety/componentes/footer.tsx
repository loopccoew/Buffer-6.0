import Link from "next/link"

export function Footer() {
  return (
    <footer className="w-full border-t bg-gradient-to-t from-purple-100 via-pink-50 to-white dark:from-purple-950 dark:via-pink-900 dark:to-gray-950 py-8 mt-16 shadow-inner">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 items-center">
          <div className="flex flex-col items-start">
            <div className="flex items-center gap-2 mb-2">
              <svg width="28" height="28" viewBox="0 0 28 28"><circle cx="14" cy="14" r="13" fill="#f472b6" opacity="0.15"/><path d="M14 5 Q20 8 14 23 Q8 8 14 5 Z" fill="#a78bfa"/></svg>
              <h3 className="font-semibold text-lg text-purple-700 dark:text-purple-200">SafeGuard</h3>
            </div>
            <p className="text-xs text-gray-500 dark:text-gray-400 max-w-xs">
              AI-powered legal assistance for women's safety and empowerment.
            </p>
          </div>

          <div>
            <h3 className="font-semibold text-sm mb-2">Quick Links</h3>
            <ul className="space-y-1">
              <li>
                <Link
                  href="/about"
                  className="text-xs text-gray-500 dark:text-gray-400 hover:text-purple-600 dark:hover:text-purple-400"
                >
                  About Us
                </Link>
              </li>
              <li>
                <Link
                  href="/privacy"
                  className="text-xs text-gray-500 dark:text-gray-400 hover:text-purple-600 dark:hover:text-purple-400"
                >
                  Privacy Policy
                </Link>
              </li>
              <li>
                <Link
                  href="/terms"
                  className="text-xs text-gray-500 dark:text-gray-400 hover:text-purple-600 dark:hover:text-purple-400"
                >
                  Terms of Service
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="font-semibold text-sm mb-2">Emergency Resources</h3>
            <ul className="space-y-1">
              <li className="text-xs text-gray-500 dark:text-gray-400">
                National Domestic Violence Hotline:{" "}
                <a href="tel:18007997233" className="text-purple-600 dark:text-purple-400">
                  1-800-799-7233
                </a>
              </li>
              <li className="text-xs text-gray-500 dark:text-gray-400">
                National Sexual Assault Hotline:{" "}
                <a href="tel:18006564673" className="text-purple-600 dark:text-purple-400">
                  1-800-656-4673
                </a>
              </li>
            </ul>
          </div>
        </div>

        <div className="mt-6 pt-4 border-t border-gray-200 dark:border-gray-800 text-center">
          <p className="text-xs text-gray-500 dark:text-gray-400">
            © {new Date().getFullYear()} SafeGuard. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  )
}
