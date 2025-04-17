import Image from "next/image";
import React from "react";

export function AnimatedHero() {
  return (
    <section className="relative w-full h-[380px] md:h-[460px] flex items-center justify-center overflow-hidden bg-gradient-to-br from-purple-100 via-pink-50 to-white dark:from-purple-950 dark:via-pink-900 dark:to-gray-950 rounded-lg shadow-lg mb-8 animate-fade-in">
      {/* Main flex row: SVG left, content right */}
      <div className="relative z-10 flex flex-row items-center justify-center w-full h-full max-w-5xl mx-auto px-4">
        {/* SVG on the left */}
        <div className="flex-shrink-0 mr-6 md:mr-12">
          <svg width="340" height="280" viewBox="0 0 340 280" fill="none" xmlns="http://www.w3.org/2000/svg" className="animate-fade-in">
            {/* Hands (left) */}
            <path d="M60 170 Q80 150 120 190 Q130 200 110 210 Q90 220 70 200 Q60 190 60 170 Z" fill="#fbbf24" opacity="0.8" className="animate-bounce-slow"/>
            {/* Hands (right) */}
            <path d="M260 170 Q240 150 200 190 Q190 200 210 210 Q230 220 250 200 Q260 190 260 170 Z" fill="#a78bfa" opacity="0.8" className="animate-bounce-slow"/>
            {/* Female symbol (center) */}
            <circle cx="160" cy="150" r="38" fill="#fff" stroke="#f472b6" strokeWidth="6"/>
            <circle cx="160" cy="150" r="28" fill="#f472b6" opacity="0.7"/>
            <rect x="154" y="188" width="12" height="32" rx="6" fill="#f472b6"/>
            <rect x="144" y="210" width="32" height="12" rx="6" fill="#f472b6"/>
            {/* Female symbol cross */}
            <rect x="154" y="188" width="12" height="32" rx="6" fill="#f472b6"/>
            <rect x="144" y="210" width="32" height="12" rx="6" fill="#f472b6"/>
            {/* Minimalist group of women (side silhouettes) */}
            <ellipse cx="90" cy="90" rx="18" ry="26" fill="#f9a8d4" opacity="0.7" className="animate-float"/>
            <ellipse cx="120" cy="95" rx="14" ry="20" fill="#fbbf24" opacity="0.7" className="animate-float"/>
            <ellipse cx="110" cy="115" rx="10" ry="14" fill="#a78bfa" opacity="0.7" className="animate-float"/>
            {/* Heart for care */}
            <path d="M210 80 Q215 70 225 80 Q235 90 210 110 Q185 90 195 80 Q205 70 210 80 Z" fill="#f472b6" opacity="0.8" className="animate-pulse"/>
            {/* Shield for protection */}
            <path d="M280 60 Q320 90 260 180 Q200 90 240 60 Q260 50 280 60 Z" fill="#a7f3d0" opacity="0.8" className="animate-fade-in"/>
            {/* Sparkles/stars */}
            <circle cx="60" cy="60" r="4" fill="#fbbf24" className="animate-float"/>
            <circle cx="300" cy="100" r="3" fill="#a78bfa" className="animate-float"/>
            <circle cx="250" cy="220" r="2.5" fill="#f472b6" className="animate-float"/>
          </svg>
        </div>
        {/* Hero Content on the right */}
        <div className="text-left max-w-xl">
          <h1 className="text-4xl md:text-5xl font-extrabold text-purple-700 dark:text-purple-300 mb-4 drop-shadow-lg animate-slide-in">
            Empowering Women, Ensuring Safety
          </h1>
          <p className="text-lg md:text-xl text-gray-700 dark:text-gray-200 mb-6 animate-fade-in">
            SafeGuard is your AI-powered legal assistant, always here to help, guide, and protect.
          </p>
          <div className="flex flex-wrap gap-4 animate-fade-in">
            <a href="#chat" className="px-6 py-3 bg-purple-600 text-white rounded-full font-semibold shadow-lg hover:bg-pink-500 transition-transform transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-purple-400 animate-pulse">
              Start Chatting
            </a>
            <a href="/resources" className="px-6 py-3 bg-white text-purple-700 border-2 border-purple-400 rounded-full font-semibold shadow-lg hover:bg-purple-50 hover:text-pink-600 transition-transform transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-purple-400">
              Explore Resources
            </a>
          </div>
        </div>
      </div>
    </section>
  );
}

// Animations (add to globals.css):
// .animate-fade-in { animation: fadeIn 1.2s ease; }
// .animate-slide-in { animation: slideIn 1.2s cubic-bezier(0.4,0,0.2,1); }
// .animate-bounce-slow { animation: bounce 2.5s infinite alternate; }
// .animate-float { animation: float 4s ease-in-out infinite alternate; }
