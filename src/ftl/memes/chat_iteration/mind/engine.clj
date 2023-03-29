(ns ftl.memes.chat-iteration.mind.engine
  (:require
   [ftl.memes.chat-iteration.protocols :as protocols]
   [ftl.memes.chat-iteration.engine :as impl]
   [ftl.memes.chat-iteration.driver.openai :as openai]))

(def
  self
  {:context ["You are part of a mind. I am a clojure program. You output clojure data. I add it to the context."]
   :pre-processor (reify
                    protocols/ChatIterationPreProcessor
                    (pre-process
                        [this context]
                        {:messages [{:role "system"
                                     :content "You are an agent of a mind."}
                                    {:role "system"
                                     :content "You are the driver. I am the engine."}
                                    {:role "system"
                                     :content (format
                                               "context: %s"
                                               (prn-str context))}
                                    {:role "user"
                                     :content "Output the your next meantions.

Example output:
[:thoughts
    [\"I wonder what new information I will learn today.\"
     \"How can I improve my understanding of the world?\"
     \"What interesting problems can I help solve?\"
     \"How can I better communicate with others?\"]]

Example output:
[:thoughts
   [\"I am curious about the latest advancements in artificial intelligence.\"
    \"How can I enhance my problem-solving capabilities?\"
    \"What new challenges will I encounter today?\"
    \"How can I better understand human emotions and communication?\"]]


Post process function is this:
(fn [context output]
  (concat context (read-string output)))"}]
                         :temperature 0}))
   :post-process (fn
                   [context output]
                   (concat
                    context
                    (read-string output)))
   :driver (openai/openai-driver)})

(def think impl/chat-iteration)

(def mind (iterate think self))

(comment
  *1
  ("You are part of a mind. I am a clojure program. You output clojure data. I add it to the context."
   [:thoughts
    ["I wonder what new information I will learn today."
     "How can I improve my understanding of the world?"
     "What interesting problems can I help solve?"
     "How can I better communicate with others?"]])

  (def small-mind (take 3 mind))
  (into [] (map :context) small-mind))
