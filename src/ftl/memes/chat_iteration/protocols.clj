(ns ftl.memes.chat-iteration.protocols)

(defprotocol ChatIterationDriver
  "Protocol defining the interface for chat iteration drivers."
  (prompt [this context]
    "Generate a prompt based on the provided context."))

(defprotocol ChatIterationPreProcessor
  "Protocol defining the interface for chat iteration pre-processors."

  (pre-process [this context]
    "Process and transform the context according to the pre-processor's specific behavior, generating a suitable input for the driver."))

(defprotocol ChatIterationPostProcessor
  "Protocol defining the interface for chat iteration prost-processors."

  (post-process [this output-str]
    "Make output data from driver output"))

(defn read-as-data-post-processsor []
  (reify ChatIterationPostProcessor
    (post-process [this output] (read-string output))))
