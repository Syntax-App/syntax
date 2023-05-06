import React, { useEffect } from "react";
import { useState } from "react";
import useEngine from "./TypingTestInterface/hooks/useEngine";
import Result from "@/components/Result";
import TypeTest from "@/components/TypeTest";
import { requestCode } from "@/helpers/user";
import { useAuth } from "@/contexts/AuthContext";

const languages = ["JAVA", "JAVASCRIPT"];

export default function Home() {
  const [currLang, setCurrLang] = useState("PYTHON");
  const [typeMode, setMode] = useState(false);
  const [stats, setStats] = useState({ acc: 0, lpm: 0 });
  const { userInfo, methods, loading } = useAuth();
  const [gptSays, setGptSays] = useState("");
  const [loadGpt, setLoadGpt] = useState(false);
  const {
    state,
    setState,
    words,
    updateWords,
    timeLeft,
    typed,
    errors,
    restart,
    totalTyped,
    COUNTDOWN_SECONDS,
    timeElapsed
  } = useEngine();

  async function getNewSnippet() {
    setLoadGpt(true);
    requestCode(currLang, userInfo?.email)
      .then((data) => {
        updateWords(data.snippet);
        setGptSays(data.explanation);
        setLoadGpt(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }

  // update words when home mounts
  useEffect(() => {
    if (!loading) {
      getNewSnippet();
    }
  }, [loading]);

  const startTest = () => {
    setMode(true);
    setState("start");
  };

  const newTest = () => {
    setMode(false);
    restart();
    getNewSnippet();
  };

  if (state == "finish") {
    return (
      <Result 
        stats={stats} 
        currLang={currLang} 
        setCurrLang={setCurrLang} 
        newTest={newTest} 
        words={words}
        gptSays={gptSays}
        languages={languages}
        />
    );
  } else {
    return (
      <TypeTest
        getNewSnippet={getNewSnippet}
        state={state}
        typeMode={typeMode}
        currLang={currLang}
        setCurrLang={setCurrLang}
        timeLeft={timeLeft}
        errors={errors}
        typed={typed}
        totalTyped={totalTyped}
        words={words}
        gptSays={gptSays}
        startTest={startTest}
        restart={restart}
        stats={stats}
        setStats={setStats}
        COUNTDOWN_SECONDS={COUNTDOWN_SECONDS}
        timeElapsed={timeElapsed}
        loadGpt={loadGpt}
        languages={languages}
      />
    );
  }
}
