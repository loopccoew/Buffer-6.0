import { Avatar } from "@/components/ui/avatar"
import { cn } from "@/lib/utils"
import type { Message } from "./chat-interface"
import { Loader2 } from "lucide-react"

interface MessageAreaProps {
  messages: Message[]
  isLoading: boolean
}

export function MessageArea({ messages, isLoading }: MessageAreaProps) {
  return (
    <div className="space-y-4">
      {messages.map((message) => (
        <div
          key={message.id}
          className={cn("flex items-start gap-3 max-w-[85%]", message.role === "user" ? "ml-auto" : "")}
        >
          {message.role === "assistant" && (
            <Avatar className="h-8 w-8 bg-purple-100 border border-purple-200">
              <div className="text-xs font-semibold text-purple-700">SG</div>
            </Avatar>
          )}

          <div
            className={cn(
              "rounded-lg px-4 py-2 text-sm",
              message.role === "user"
                ? "bg-purple-600 text-white"
                : "bg-gray-100 dark:bg-gray-800 text-gray-900 dark:text-gray-100",
            )}
          >
            <div className="whitespace-pre-line">{message.content}</div>
            <div className={cn("text-xs mt-1", message.role === "user" ? "text-purple-200" : "text-gray-500")}>
              {message.timestamp.toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </div>
          </div>

          {message.role === "user" && (
            <Avatar className="h-8 w-8 bg-purple-600">
              <div className="text-xs font-semibold text-white">You</div>
            </Avatar>
          )}
        </div>
      ))}

      {isLoading && (
        <div className="flex items-start gap-3">
          <Avatar className="h-8 w-8 bg-purple-100 border border-purple-200">
            <div className="text-xs font-semibold text-purple-700">SG</div>
          </Avatar>
          <div className="rounded-lg px-4 py-2 bg-gray-100 dark:bg-gray-800">
            <Loader2 className="h-4 w-4 animate-spin text-purple-600" />
          </div>
        </div>
      )}
    </div>
  )
}
