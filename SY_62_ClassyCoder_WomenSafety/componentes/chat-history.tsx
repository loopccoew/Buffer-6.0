"use client"

import React, { useEffect, useState } from "react"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Button } from "@/components/ui/button"
import { Trash2 } from "lucide-react"

interface ChatHistoryProps {
  recentPrompts: string[]
  onClearHistory: () => void
}

export function ChatHistory({ recentPrompts, onClearHistory }: ChatHistoryProps) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  return (
    <div className="flex flex-col h-full bg-white dark:bg-gray-900 rounded-lg shadow-md border border-gray-200 dark:border-gray-800">
      <div className="flex items-center justify-between p-4 border-b border-gray-200 dark:border-gray-800">
        <h2 className="text-lg font-semibold">Recent Chats</h2>
        <div className="flex space-x-2">
          <Button 
            onClick={onClearHistory} 
            size="sm" 
            variant="outline" 
            className="flex items-center gap-1"
            disabled={recentPrompts.length === 0}
          >
            <Trash2 className="h-4 w-4" />
            Clear History
          </Button>
        </div>
      </div>
      <ScrollArea className="flex-1 p-4">
        {error && <div className="text-red-500">{error}</div>}
        {recentPrompts.length === 0 ? (
          <div className="text-gray-500">No recent chats.</div>
        ) : (
          <ul className="space-y-2">
            {recentPrompts.map((prompt, idx) => (
              <li 
                key={idx} 
                className="bg-purple-100 dark:bg-gray-800 rounded p-2 text-sm hover:bg-purple-200 dark:hover:bg-gray-700 transition-colors cursor-pointer"
                onClick={() => {
                  // You can add functionality to load the chat when clicking on a history item
                }}
              >
                {prompt.length > 50 ? `${prompt.substring(0, 50)}...` : prompt}
              </li>
            ))}
          </ul>
        )}
      </ScrollArea>
    </div>
  )
}
