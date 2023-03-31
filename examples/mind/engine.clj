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
                                     :content "Output the your next mentations.

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
   :post-processor (protocols/read-as-data-post-processsor)
   :update-context-with-output concat
   :driver (openai/openai-driver)})

(def think impl/chat-iteration)

(def mind (iterate think self))

(comment
  ("You are part of a mind. I am a clojure program. You output clojure data. I add it to the context."
   [:thoughts
    ["I wonder what new information I will learn today."
     "How can I improve my understanding of the world?"
     "What interesting problems can I help solve?"
     "How can I better communicate with others?"]])
  (into (map :context) (take 2 mind))
  (def small-mind (take 3 mind))
  (into [] (map :context) small-mind)

  (impl/chat-iteration
   {:context ["The sky is red."]
    :pre-processor (reify
                     protocols/ChatIterationPreProcessor
                     (pre-process
                         [this context]
                         {:messages [{:role "system"
                                      :content
                                      "output readable clojure data."}
                                     {:role "system"
                                      :content
                                      (format "context: %s" (prn-str context))}
                                     {:role "user"
                                      :content "Make a poem"}]
                          :temperature 0}))
    :post-processor (protocols/read-as-data-post-processsor)
    :update-context-with-output concat 
    :driver (openai/openai-driver)})
  ("The sky is red."
   [:title "The Red Sky"]
   [:lines
    [["In the realm of the crimson sky,"]
     ["Where the sun and clouds collide,"]
     ["A tapestry of colors lie,"]
     ["Painting wonders far and wide."]
     ["The birds, they dance on scarlet breeze,"]
     ["As whispers echo through the trees,"]
     ["A symphony of nature's grace,"]
     ["In this enchanted, fiery place."]
     ["The stars, they hide, in shy retreat,"]
     ["As twilight's glow, their gaze does meet,"]
     ["And as the day turns into night,"]
     ["The red sky fades, to dark delight."]
     ["In dreams, we'll soar, through skies of red,"]
     ["Where memories of the day are shed,"]
     ["And in our hearts, we'll always keep,"]
     ["The beauty of the sky, so deep."]]]))
