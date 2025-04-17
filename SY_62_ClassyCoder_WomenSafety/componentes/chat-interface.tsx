"use client"

import type React from "react"

import { useState, useRef, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { ScrollArea } from "@/components/ui/scroll-area"
import { SendIcon, MicIcon } from "lucide-react"
import { FeaturePanel } from "./feature-panel"
import { MessageArea } from "./message-area"
import { LegalGuidanceModal } from "./modals/legal-guidance-modal"
import { ComplaintDraftModal } from "./modals/complaint-draft-modal"
import { LawyerConnectModal } from "./modals/lawyer-connect-modal"
import { SosHelpModal } from "./modals/sos-help-modal"
import { FakeCallModal } from "./modals/fake-call-modal"
import { ChatHistory } from "./chat-history"
import { auth, db } from "@/lib/firebase"
import { collection, addDoc, query, where, getDocs, orderBy, limit } from "firebase/firestore"
import { useAuthState } from "react-firebase-hooks/auth"

export type Message = {
  id: string
  role: "user" | "assistant"
  content: string
  timestamp: Date
}

// Hardcoded responses for common queries
const HARDCODED_RESPONSES: Record<string, string> = {
  "hello": "Hello! I'm SafeGuard, your AI legal assistant. How can I help you today?",
  "hi": "Hi there! I'm here to help with any legal concerns you might have. What would you like to know?",
  "help": "I can help you with:\n1. Legal guidance\n2. Drafting complaints\n3. Connecting with lawyers\n4. Emergency assistance\n5. Fake call service\n\nWhat would you like help with?",
  "legal rights": "As a woman, you have several legal rights including:\n1. Right to equality\n2. Right to protection from domestic violence\n3. Right to workplace safety\n4. Right to privacy\n5. Right to legal aid\n\nWould you like more details about any specific right?",
  "emergency": "For emergencies, please use the SOS Help feature. I can help you:\n1. Contact emergency services\n2. Share your location\n3. Connect with nearby support\n4. Guide you to safety\n\nWould you like me to open the SOS Help panel?",
  "harassment": "If you're facing harassment, here are your options:\n1. File a police complaint\n2. Get a restraining order\n3. Contact women's helpline\n4. Seek legal counsel\n\nWould you like help with any of these?",
  "workplace": "For workplace issues, you can:\n1. Report to HR\n2. File a complaint with labor department\n3. Seek legal counsel\n4. Document evidence\n\nWhat specific workplace issue are you facing?",
  "safety tips": "Here are some safety tips:\n1. Share your location with trusted contacts\n2. Keep emergency numbers handy\n3. Use the fake call feature when needed\n4. Trust your instincts\n5. Know your legal rights\n\nWould you like more specific safety advice?"
}

export function ChatInterface() {
  const [user] = useAuthState(auth)
  const [messages, setMessages] = useState<Message[]>([
    {
      id: "welcome",
      role: "assistant",
      content: "Hello, I'm SafeGuard, your AI legal assistant. How can I help you today?",
      timestamp: new Date(),
    },
  ])
  const [input, setInput] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [activeModal, setActiveModal] = useState<string | null>(null)
  const [recentPrompts, setRecentPrompts] = useState<string[]>([])
  const messagesEndRef = useRef<HTMLDivElement>(null)

  // Load recent chats from Firebase
  useEffect(() => {
    const loadRecentChats = async () => {
      if (!user) return

      try {
        const chatsRef = collection(db, "chats")
        const q = query(
          chatsRef,
          where("userId", "==", user.uid),
          orderBy("timestamp", "desc"),
          limit(10)
        )
        const querySnapshot = await getDocs(q)
        const prompts = querySnapshot.docs.map((doc: { data: () => { prompt: string } }) => doc.data().prompt)
        setRecentPrompts(prompts)
      } catch (error) {
        console.error("Error loading recent chats:", error)
      }
    }

    loadRecentChats()
  }, [user])

  // Auto-scroll to bottom when messages change
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }, [messages])

  const handleSendMessage = async (e?: React.FormEvent) => {
    e?.preventDefault()

    if (!input.trim()) return

    // Add user message
    const userMessage: Message = {
      id: Date.now().toString(),
      role: "user",
      content: input,
      timestamp: new Date(),
    }

    setMessages((prev) => [...prev, userMessage])
    setInput("")
    setIsLoading(true)

    // Save chat to Firebase if user is signed in
    if (user) {
      try {
        await addDoc(collection(db, "chats"), {
          userId: user.uid,
          prompt: input,
          timestamp: new Date(),
        })
      } catch (error) {
        console.error("Error saving chat:", error)
      }
    }

    // Check for hardcoded response
    const lowerInput = input.toLowerCase()
    let botResponse: Message

    if (HARDCODED_RESPONSES[lowerInput]) {
      botResponse = {
        id: (Date.now() + 1).toString(),
        role: "assistant",
        content: HARDCODED_RESPONSES[lowerInput],
        timestamp: new Date(),
      }
    } else {
      // Call Gemini backend API for non-hardcoded responses
      try {
        const res = await fetch("/api/gemini", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ prompt: input })
        })
        const data = await res.json()
        botResponse = {
          id: (Date.now() + 1).toString(),
          role: "assistant",
          content: data.response || "I understand you're looking for help. Here are some general safety tips:\n\n1. Always trust your instincts\n2. Keep emergency contacts handy\n3. Share your location with trusted friends/family\n4. Use the SOS feature in emergencies\n5. Document any incidents with dates and details\n\nWould you like more specific information about any of these points?",
          timestamp: new Date(),
        }
      } catch (err: any) {
        botResponse = {
          id: (Date.now() + 2).toString(),
          role: "assistant",
          content: "I'm here to help you stay safe. Here are some important resources:\n\n1. National Women's Helpline: 1091\n2. Police Emergency: 100\n3. Women's Safety App: Himmat\n4. Local Women's Shelter\n5. Legal Aid Services\n\nWould you like me to help you connect with any of these services?",
          timestamp: new Date(),
        }
      }
    }

    setMessages((prev) => [...prev, botResponse])
    setIsLoading(false)
  }

  const handleClearHistory = async () => {
    if (!user) return

    try {
      const chatsRef = collection(db, "chats")
      const q = query(chatsRef, where("userId", "==", user.uid))
      const querySnapshot = await getDocs(q)
      
      // Note: In a real app, you would want to delete the documents
      // For now, we'll just clear the local state
      setRecentPrompts([])
    } catch (error) {
      console.error("Error clearing history:", error)
    }
  }

  const handleOpenModal = (modalName: string) => {
    setActiveModal(modalName)
  }

  const handleCloseModal = () => {
    setActiveModal(null)
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-5 gap-4 h-[calc(100vh-12rem)]">
      {/* Sidebar: Chat History (visible on md+) */}
      <div className="hidden md:block md:col-span-1">
        <ChatHistory 
          recentPrompts={recentPrompts} 
          onClearHistory={handleClearHistory} 
        />
      </div>
      {/* Feature Panel (mobile and up) */}
      <FeaturePanel onFeatureSelect={handleOpenModal} />

      {/* Main Chat Area */}
      <div className="md:col-span-3 flex flex-col bg-white dark:bg-gray-900 rounded-lg shadow-md overflow-hidden border border-gray-200 dark:border-gray-800">
        <ScrollArea className="flex-1 p-4">
          <MessageArea messages={messages} isLoading={isLoading} />
          <div ref={messagesEndRef} />
        </ScrollArea>

        <div className="p-4 border-t border-gray-200 dark:border-gray-800">
          <form onSubmit={handleSendMessage} className="flex space-x-2">
            <Input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type your message here..."
              className="flex-1"
              disabled={isLoading}
            />
            <Button type="submit" size="icon" disabled={isLoading || !input.trim()} aria-label="Send message">
              <SendIcon className="h-4 w-4" />
            </Button>
            <Button type="button" variant="outline" size="icon" aria-label="Voice input">
              <MicIcon className="h-4 w-4" />
              <span className="sr-only">Voice input</span>
            </Button>
          </form>
          <p className="text-xs text-gray-500 mt-2">
            Your conversations are private and encrypted. For emergencies, please use the SOS Help feature.
          </p>
        </div>
      </div>

      {/* Modals */}
      <LegalGuidanceModal
        open={activeModal === "legal-guidance"}
        onOpenChange={handleCloseModal}
        onSubmit={(data) => {
          const content = `I need legal guidance regarding ${data.issueType}. Details: ${data.details}`
          setInput(content)
          handleCloseModal()
          // Auto-submit after modal closes
          setTimeout(() => {
            handleSendMessage()
          }, 100)
        }}
      />

      <ComplaintDraftModal
        open={activeModal === "draft-complaint"}
        onOpenChange={handleCloseModal}
        onSubmit={(data) => {
          setMessages((prev) => [
            ...prev,
            {
              id: Date.now().toString(),
              role: "user",
              content: `I need help drafting a complaint about ${data.incidentType} that occurred on ${data.date}.`,
              timestamp: new Date(),
            },
            {
              id: (Date.now() + 1).toString(),
              role: "assistant",
              content: `I've prepared a draft complaint based on the information you provided about the ${data.incidentType} incident on ${data.date}. Here's the draft:\n\n[FORMAL COMPLAINT]\nDate: ${new Date().toLocaleDateString()}\nRe: ${data.incidentType} Incident on ${data.date}\n\nTo Whom It May Concern,\n\nI am writing to formally report an incident of ${data.incidentType} that occurred on ${data.date}. ${data.details}\n\nI request that this matter be investigated promptly and appropriate action be taken.\n\nSincerely,\n[Your Name]\n\nYou can copy this draft and modify it as needed. Would you like me to help you with any specific sections of the complaint?`,
              timestamp: new Date(),
            },
          ])
          handleCloseModal()
        }}
      />

      <LawyerConnectModal
        open={activeModal === "lawyer-connect"}
        onOpenChange={handleCloseModal}
        onSubmit={(data) => {
          setMessages((prev) => [
            ...prev,
            {
              id: Date.now().toString(),
              role: "user",
              content: `I need to connect with a lawyer who specializes in ${data.specialization} in ${data.location}.`,
              timestamp: new Date(),
            },
            {
              id: (Date.now() + 1).toString(),
              role: "assistant",
              content: `I've found several lawyers in ${data.location} who specialize in ${data.specialization}:\n\n1. Jane Smith - Women's Rights Attorney\n   Phone: (555) 123-4567\n   Email: jane.smith@legalfirm.com\n   Specializes in: ${data.specialization}, Domestic Violence\n\n2. Robert Johnson - Civil Rights Lawyer\n   Phone: (555) 987-6543\n   Email: robert.j@lawoffices.com\n   Specializes in: ${data.specialization}, Workplace Discrimination\n\n3. Maria Garcia - Family Law Attorney\n   Phone: (555) 456-7890\n   Email: m.garcia@familylaw.com\n   Specializes in: ${data.specialization}, Restraining Orders\n\nWould you like me to help you prepare for your consultation with any of these lawyers?`,
              timestamp: new Date(),
            },
          ])
          handleCloseModal()
        }}
      />

      <SosHelpModal
        open={activeModal === "sos-help"}
        onOpenChange={handleCloseModal}
        onSubmit={(data) => {
          setMessages((prev) => [
            ...prev,
            {
              id: Date.now().toString(),
              role: "user",
              content: "I need to set up my SOS emergency contacts.",
              timestamp: new Date(),
            },
            {
              id: (Date.now() + 1).toString(),
              role: "assistant",
              content: `I've set up your emergency contacts. In case of an emergency, click the SOS button and a message with your location will be sent to:\n\n1. ${data.contact1Name}: ${data.contact1Phone}\n2. ${data.contact2Name}: ${data.contact2Phone}\n\nYour emergency message: "${data.message}"\n\nYou can update these contacts at any time from your profile settings.`,
              timestamp: new Date(),
            },
          ])
          handleCloseModal()
        }}
      />

      <FakeCallModal open={activeModal === "fake-call"} onOpenChange={handleCloseModal} />
    </div>
  )
}
