import React, { useEffect } from "react";
import { useState } from "react";
import { useAuth } from "@/contexts/AuthContext";
import useEngine from "./TypingTestInterface/hooks/useEngine";
import { useRouter } from "next/router";
import Result from "@/components/result";
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
  const [ currLang, setcurrLang ] = useState("PYTHON");
  const [typeMode, setMode] = useState(false);
  const {state, setState, words, updateWords, timeLeft, typed, errors, restart, totalTyped} = useEngine();

  const startTest = () => {
    setMode(true);
    setState("start");
  }

  // TODO: change this later
  // update words w hardcoded code
  useEffect(() => {
    updateWords(code);
  }, [words]);
  
  if (state == "finish") {
    return <Result/>
  } else {
    return <TypeTest
              typeMode={typeMode}
              currLang={currLang}
              setcurrLang={setcurrLang}
              timeLeft={timeLeft}
              errors={errors}
              typed={typed}
              totalTyped={totalTyped}
              words={words}
              startTest={startTest}
              restart={restart}
            />
  }
}