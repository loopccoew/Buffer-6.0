import React from "react";

export function AboutUsSection() {
  return (
    <section className="relative w-full max-w-4xl mx-auto my-12 rounded-2xl bg-gradient-to-br from-pink-50 via-purple-100 to-white dark:from-purple-900 dark:via-pink-950 dark:to-gray-950 shadow-xl p-8 flex flex-col md:flex-row gap-8 items-center animate-fade-in">
      {/* Abstract women group icon */}
      <div className="flex-shrink-0">
        <svg width="110" height="110" viewBox="0 0 110 110" fill="none" xmlns="http://www.w3.org/2000/svg">
          <ellipse cx="35" cy="60" rx="18" ry="26" fill="#f9a8d4" opacity="0.7"/>
          <ellipse cx="65" cy="60" rx="14" ry="20" fill="#fbbf24" opacity="0.7"/>
          <ellipse cx="55" cy="80" rx="10" ry="14" fill="#a78bfa" opacity="0.7"/>
        </svg>
      </div>
      {/* About Us Content */}
      <div>
        <h2 className="text-3xl font-extrabold text-purple-700 dark:text-purple-200 mb-2">About Us</h2>
        <p className="text-lg text-gray-700 dark:text-gray-200 mb-4 max-w-xl">
          SafeGuard is dedicated to empowering women by providing instant, AI-powered legal support, resources, and guidance. Our mission is to create a safer environment for women everywhere, offering tools for protection, education, and community support.
        </p>
        <ul className="flex flex-wrap gap-3 mt-2">
          <li className="flex items-center gap-1 text-pink-600 font-medium"><svg width="18" height="18" viewBox="0 0 18 18"><circle cx="9" cy="9" r="8" fill="#f472b6" opacity="0.2"/><text x="50%" y="55%" textAnchor="middle" fill="#f472b6" fontSize="12" fontWeight="bold" dy=".3em">♀</text></svg> Women’s Rights</li>
          <li className="flex items-center gap-1 text-yellow-500 font-medium"><svg width="18" height="18" viewBox="0 0 18 18"><circle cx="9" cy="9" r="8" fill="#fbbf24" opacity="0.2"/><path d="M9 4 Q12 6 9 14 Q6 6 9 4 Z" fill="#fbbf24"/></svg> Safety Tools</li>
          <li className="flex items-center gap-1 text-purple-600 font-medium"><svg width="18" height="18" viewBox="0 0 18 18"><circle cx="9" cy="9" r="8" fill="#a78bfa" opacity="0.2"/><rect x="7.5" y="12" width="3" height="5" rx="1.5" fill="#a78bfa"/><rect x="4" y="15" width="10" height="3" rx="1.5" fill="#a78bfa"/></svg> Community</li>
        </ul>
      </div>
    </section>
  );
}
