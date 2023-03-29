(ns ftl.memes.chat-iteration.protocols)

(defprotocol ChatIterationDriver
  "Protocol defining the interface for chat iteration drivers."

  (prompt [this context]
    "Generate a prompt based on the provided context."))

(defprotocol ChatIterationPreProcessor
  "Protocol defining the interface for chat iteration pre-processors."

  (pre-process [this context]
    "Process and transform the context according to the pre-processor's specific behavior, generating a suitable input for the driver."))
