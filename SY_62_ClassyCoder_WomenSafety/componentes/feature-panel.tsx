"use client"

import { Button } from "@/components/ui/button"
import { BookOpenIcon, FileTextIcon, UserIcon, AlertTriangleIcon, PhoneIcon } from "lucide-react"

interface FeaturePanelProps {
  onFeatureSelect: (feature: string) => void
}

export function FeaturePanel({ onFeatureSelect }: FeaturePanelProps) {
  return (
    <div className="hidden md:flex md:flex-col gap-3 bg-white dark:bg-gray-900 p-4 rounded-lg shadow-md border border-gray-200 dark:border-gray-800">
      <h2 className="font-semibold text-lg mb-2">Features</h2>

      <Button
        variant="outline"
        className="justify-start gap-2 h-auto py-3"
        onClick={() => onFeatureSelect("legal-guidance")}
      >
        <BookOpenIcon className="h-4 w-4 text-purple-600" />
        <div className="flex flex-col items-start">
          <span>Legal Guidance</span>
          <span className="text-xs text-gray-500">Get instant legal advice</span>
        </div>
      </Button>

      <Button
        variant="outline"
        className="justify-start gap-2 h-auto py-3"
        onClick={() => onFeatureSelect("draft-complaint")}
      >
        <FileTextIcon className="h-4 w-4 text-purple-600" />
        <div className="flex flex-col items-start">
          <span>Draft Complaint</span>
          <span className="text-xs text-gray-500">Create formal complaints</span>
        </div>
      </Button>

      <Button
        variant="outline"
        className="justify-start gap-2 h-auto py-3"
        onClick={() => onFeatureSelect("lawyer-connect")}
      >
        <UserIcon className="h-4 w-4 text-purple-600" />
        <div className="flex flex-col items-start">
          <span>Connect with Lawyer</span>
          <span className="text-xs text-gray-500">Find legal professionals</span>
        </div>
      </Button>

      <Button variant="outline" className="justify-start gap-2 h-auto py-3" onClick={() => onFeatureSelect("sos-help")}>
        <AlertTriangleIcon className="h-4 w-4 text-red-600" />
        <div className="flex flex-col items-start">
          <span>SOS Help</span>
          <span className="text-xs text-gray-500">Emergency assistance</span>
        </div>
      </Button>

      <Button
        variant="outline"
        className="justify-start gap-2 h-auto py-3"
        onClick={() => onFeatureSelect("fake-call")}
      >
        <PhoneIcon className="h-4 w-4 text-purple-600" />
        <div className="flex flex-col items-start">
          <span>Fake Legal Call</span>
          <span className="text-xs text-gray-500">Simulate a legal call</span>
        </div>
      </Button>
    </div>
  )
}
