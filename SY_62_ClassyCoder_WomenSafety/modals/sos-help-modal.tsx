"use client"

import { useState } from "react"
import { z } from "zod"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { AlertTriangleIcon } from "lucide-react"

const formSchema = z.object({
  contact1Name: z.string().min(2, {
    message: "Please enter a name",
  }),
  contact1Phone: z.string().min(10, {
    message: "Please enter a valid phone number",
  }),
  contact2Name: z.string().min(2, {
    message: "Please enter a name",
  }),
  contact2Phone: z.string().min(10, {
    message: "Please enter a valid phone number",
  }),
  message: z.string().min(10, {
    message: "Message must be at least 10 characters",
  }),
})

type FormValues = z.infer<typeof formSchema>

interface SosHelpModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onSubmit: (data: FormValues) => void
}

export function SosHelpModal({ open, onOpenChange, onSubmit }: SosHelpModalProps) {
  const [isSubmitting, setIsSubmitting] = useState(false)

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      contact1Name: "",
      contact1Phone: "",
      contact2Name: "",
      contact2Phone: "",
      message: "I need help. This is an emergency. My current location is: [Location will be added automatically]",
    },
  })

  const handleSubmit = (data: FormValues) => {
    setIsSubmitting(true)

    // Simulate processing
    setTimeout(() => {
      onSubmit(data)
      form.reset({
        contact1Name: "",
        contact1Phone: "",
        contact2Name: "",
        contact2Phone: "",
        message: "I need help. This is an emergency. My current location is: [Location will be added automatically]",
      })
      setIsSubmitting(false)
    }, 500)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-red-600">
            <AlertTriangleIcon className="h-5 w-5" />
            SOS Emergency Setup
          </DialogTitle>
          <DialogDescription>Set up emergency contacts who will receive your SOS messages.</DialogDescription>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4">
            <div className="bg-red-50 dark:bg-red-900/20 p-3 rounded-md text-sm text-red-800 dark:text-red-300 mb-4">
              In case of immediate danger, please call emergency services directly at 911.
            </div>

            <div className="space-y-4">
              <h3 className="text-sm font-medium">Emergency Contact 1</h3>

              <FormField
                control={form.control}
                name="contact1Name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Contact name" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="contact1Phone"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Phone Number</FormLabel>
                    <FormControl>
                      <Input placeholder="Contact phone number" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="space-y-4">
              <h3 className="text-sm font-medium">Emergency Contact 2</h3>

              <FormField
                control={form.control}
                name="contact2Name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Contact name" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="contact2Phone"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Phone Number</FormLabel>
                    <FormControl>
                      <Input placeholder="Contact phone number" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="message"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Emergency Message</FormLabel>
                  <FormControl>
                    <Textarea placeholder="Your emergency message" className="min-h-[80px]" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                Cancel
              </Button>
              <Button type="submit" variant="destructive" disabled={isSubmitting}>
                {isSubmitting ? "Saving..." : "Save Emergency Contacts"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
