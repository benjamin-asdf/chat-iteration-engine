(ns ftl.memes.chat-iteration.engine
  (:require [ftl.memes.chat-iteration.protocols :as protocols]))

(defn chat-iteration [{:keys [context pre-processor driver post-processor update-context-with-output] :as state}]
  (let [prompt (protocols/pre-process pre-processor context)
        output (protocols/prompt driver prompt)
        output-data (protocols/post-process post-processor output)
        new-context (update-context-with-output context output-data)]
    (assoc state :context new-context)))


