"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogDescription, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Avatar } from "@/components/ui/avatar"
import { PhoneIcon, PhoneOffIcon, MicOffIcon, VolumeXIcon } from "lucide-react"

interface FakeCallModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function FakeCallModal({ open, onOpenChange }: FakeCallModalProps) {
  const [callState, setCallState] = useState<"incoming" | "active" | "ended">("incoming")
  const [callDuration, setCallDuration] = useState(0)
  const [script, setScript] = useState<string[]>([
    "Hello, this is Attorney Sarah Johnson from Legal Aid Services.",
    "I'm calling regarding your case file #LC-2023-0472.",
    "We need to schedule an urgent meeting to discuss the legal documents you submitted.",
    "Can you confirm your availability for tomorrow at 2 PM at our downtown office?",
    "Please bring all the documentation we discussed in our previous meeting.",
    "Do you have any questions about the procedure before we meet?",
    "Great, I'll make a note in your file. We'll see you tomorrow at 2 PM.",
    "Thank you for your time. Have a good day.",
  ])
  const [currentScriptIndex, setCurrentScriptIndex] = useState(0)

  useEffect(() => {
    let timer: NodeJS.Timeout

    if (callState === "active") {
      timer = setInterval(() => {
        setCallDuration((prev) => prev + 1)
      }, 1000)
    }

    return () => {
      if (timer) clearInterval(timer)
    }
  }, [callState])

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`
  }

  const handleAnswer = () => {
    setCallState("active")
  }

  const handleHangUp = () => {
    setCallState("ended")
    setTimeout(() => {
      onOpenChange(false)
      // Reset for next time
      setCallState("incoming")
      setCallDuration(0)
      setCurrentScriptIndex(0)
    }, 1000)
  }

  const handleNextScript = () => {
    if (currentScriptIndex < script.length - 1) {
      setCurrentScriptIndex((prev) => prev + 1)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[400px] p-0 overflow-hidden rounded-xl">
        <div className="bg-purple-600 text-white p-6 flex flex-col items-center">
          <Avatar className="h-20 w-20 bg-white text-purple-600 mb-4">
            <span className="text-xl font-bold">SJ</span>
          </Avatar>

          <DialogTitle className="text-xl font-bold text-white mb-1">
            {callState === "incoming" ? "Incoming Call" : "Attorney Sarah Johnson"}
          </DialogTitle>

          <DialogDescription className="text-purple-100 text-center">
            {callState === "incoming"
              ? "Legal Aid Services"
              : callState === "active"
                ? formatTime(callDuration)
                : "Call ended"}
          </DialogDescription>
        </div>

        {callState === "active" && (
          <div className="p-4 max-h-[200px] overflow-y-auto">
            <div className="bg-gray-100 dark:bg-gray-800 p-3 rounded-lg">
              <h3 className="text-sm font-medium mb-2">Suggested Script:</h3>
              <p className="text-sm">{script[currentScriptIndex]}</p>
            </div>

            <div className="mt-4 flex justify-end">
              <Button size="sm" onClick={handleNextScript} disabled={currentScriptIndex >= script.length - 1}>
                Next Line
              </Button>
            </div>
          </div>
        )}

        <div className="p-6 flex justify-center space-x-8">
          {callState === "incoming" ? (
            <>
              <Button
                size="icon"
                variant="outline"
                className="h-14 w-14 rounded-full border-red-500 text-red-500 hover:bg-red-50 hover:text-red-600"
                onClick={() => onOpenChange(false)}
              >
                <PhoneOffIcon className="h-6 w-6" />
                <span className="sr-only">Decline</span>
              </Button>

              <Button
                size="icon"
                variant="outline"
                className="h-14 w-14 rounded-full border-green-500 text-green-500 hover:bg-green-50 hover:text-green-600"
                onClick={handleAnswer}
              >
                <PhoneIcon className="h-6 w-6" />
                <span className="sr-only">Answer</span>
              </Button>
            </>
          ) : (
            <>
              <Button size="icon" variant="outline" className="h-12 w-12 rounded-full">
                <MicOffIcon className="h-5 w-5" />
                <span className="sr-only">Mute</span>
              </Button>

              <Button size="icon" variant="destructive" className="h-14 w-14 rounded-full" onClick={handleHangUp}>
                <PhoneOffIcon className="h-6 w-6" />
                <span className="sr-only">End Call</span>
              </Button>

              <Button size="icon" variant="outline" className="h-12 w-12 rounded-full">
                <VolumeXIcon className="h-5 w-5" />
                <span className="sr-only">Speaker</span>
              </Button>
            </>
          )}
        </div>
      </DialogContent>
    </Dialog>
  )
}
