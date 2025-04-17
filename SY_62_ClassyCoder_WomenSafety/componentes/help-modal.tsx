import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion"

interface HelpModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function HelpModal({ open, onOpenChange }: HelpModalProps) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md md:max-w-lg">
        <DialogHeader>
          <DialogTitle>Frequently Asked Questions</DialogTitle>
          <DialogDescription>Learn how to use SafeGuard to get legal assistance</DialogDescription>
        </DialogHeader>

        <Accordion type="single" collapsible className="w-full">
          <AccordionItem value="item-1">
            <AccordionTrigger>How do I use the chat?</AccordionTrigger>
            <AccordionContent>
              Simply type your legal question in the chat box and press send. Our AI assistant will respond with helpful
              information and guidance.
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="item-2">
            <AccordionTrigger>What is the Legal Guidance feature?</AccordionTrigger>
            <AccordionContent>
              The Legal Guidance feature provides instant legal advice on issues like harassment, domestic violence, and
              workplace discrimination. Click the "Legal Guidance" button to start.
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="item-3">
            <AccordionTrigger>How does the SOS Help feature work?</AccordionTrigger>
            <AccordionContent>
              The SOS Help feature allows you to set up emergency contacts. In an emergency, click the SOS button to
              send your location and a distress message to your contacts.
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="item-4">
            <AccordionTrigger>Is my information secure?</AccordionTrigger>
            <AccordionContent>
              Yes, all your conversations and personal information are encrypted and kept confidential. We prioritize
              your privacy and security.
            </AccordionContent>
          </AccordionItem>

          <AccordionItem value="item-5">
            <AccordionTrigger>What is the Fake Legal Call feature?</AccordionTrigger>
            <AccordionContent>
              The Fake Legal Call feature simulates a phone call from a legal professional to help you exit
              uncomfortable situations safely.
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      </DialogContent>
    </Dialog>
  )
}
