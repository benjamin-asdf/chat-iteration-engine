(ns
    ftl.memes.chat-iteration.driver.openai
    (:require
     [clojure.java.shell :as shell]
     [clojure.string :as str]
     [ftl.memes.chat-iteration.protocols :as protocols]
     [wkok.openai-clojure.api :as api]))

(defn chat-content [response] (-> response :choices peek :message :content))

(def api-key (memoize (fn [] (str/trim (:out (shell/sh "pass" "united-signals-openai-api-key"))))))

(defn openai-driver []
  (reify protocols/ChatIterationDriver

    (prompt [this input]
      (chat-content
       (api/create-chat-completion
        (assoc input :model "gpt-4")
        {:api-key (api-key)})))))


(comment
  (->
   (api/create-chat-completion
    {:model "gpt-4"
     :messages
     [{:role "system"
       :content "You are an agent of a mind."}
      {:role "system"
       :content "You are the driver. I am the engine."}
      {:role "system"
       :content "I am a clojure program. Answer in clojure code. I eval."}
      {:role "system"
       :content "I am a REPL for you."}]
     :temperature 0})
   chat-content))
