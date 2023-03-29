(ns ftl.memes.chat-iteration.engine
  (:require [ftl.memes.chat-iteration.protocols :as protocols]))

(defn chat-iteration [{:keys [context pre-processor post-process driver] :as state}]
  (let [prompt (protocols/pre-process pre-processor context)
        output (protocols/prompt driver prompt)
        new-context (post-process context output)]
    (assoc state :context new-context)))


