import React, { useEffect } from "react";
import { useState } from "react";
import useEngine from "./TypingTestInterface/hooks/useEngine";
import Result from "@/components/Result";
import TypeTest from "@/components/TypeTest";

const code: string = `class Main {\npublic static void main(String[] args) {\n\tMap<String, String> languages = new HashMap<>();\nlanguages.put("pos3", "JS");
    languages.put("pos1", "Java");
    languages.put("pos2", "Python");
    System.out.println("Map: " + languages);
    TreeMap<String, String> sortedNumbers = new TreeMap<>(languages);
    System.out.println("Map with sorted Key" + sortedNumbers); 
    Map<String, String> languages = new HashMap<>(); 
    languages.put("pos3", "JS");
    languages.put("pos1", "Java");
    languages.put("pos2", "Python");
    System.out.println("Map: " + languages);
    TreeMap<String, String> sortedNumbers = new TreeMap<>(languages);
    System.out.println("Map with sorted Key" + sortedNumbers); 
  } 
}`;

export default function Home() {
  const [currLang, setCurrLang] = useState("PYTHON");
  const [typeMode, setMode] = useState(false);
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
  const [stats, setStats] = useState({ acc: 0, lpm: 0 });

  const startTest = () => {
    setMode(true);
    setState("start");
  };

  const newTest = () => {
    setMode(false);
    restart();
    // TODO: get new code snippets
  };

  // TODO: change this later
  // update words w hardcoded code
  useEffect(() => {
    updateWords(code);
  }, [words]);

  if (state == "finish") {
    return (
      <Result stats={stats} currLang={currLang} setCurrLang={setCurrLang} newTest={newTest} />
    );
  } else {
    return (
      <TypeTest
        state={state}
        typeMode={typeMode}
        currLang={currLang}
        setCurrLang={setCurrLang}
        timeLeft={timeLeft}
        errors={errors}
        typed={typed}
        totalTyped={totalTyped}
        words={words}
        startTest={startTest}
        restart={restart}
        stats={stats}
        setStats={setStats}
        COUNTDOWN_SECONDS={COUNTDOWN_SECONDS}
        timeElapsed={timeElapsed}
      />
    );
  }
}
