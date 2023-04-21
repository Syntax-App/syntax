import { useCallback, useEffect, useState } from "react";
import useWords from "./useWords";
import useCountdownTimer from "./useCountdownTimer";
import useTypings from "./useTypings";
import { countErrors } from "../utils/typetesthelper";
import { type } from "os";

export type State = "start" | "run" | "finish";

const NUMBER_WORDS = 20;
const COUNTDOWN_SECONDS = 10;

const useEngine = () => {
  const [state, setState] = useState<State>("start");
  const { words, updateWords } = useWords(NUMBER_WORDS);
  const { timeLeft, startCountdown, resetCountdown } =
    useCountdownTimer(COUNTDOWN_SECONDS);

  // record keyboard strokes during start or run
  const { typed, cursor, clearTyped, resetTotalTyped, totalTyped } = useTypings(
    state !== "finish"
  );

  const [errors, setErrors] = useState(0);

  // summation of all errors since the beginning
//   const sumErrors = useCallback(() => {
//     const wordsReached = words.substring(0, cursor);
//     setErrors((prevErrors) => prevErrors + countErrors(typed, wordsReached));
//   }, [typed, words, cursor]);

  const isStarting = state === "start" && cursor > 0;
  // keeps track of when user finishes
  const finishedTyping = cursor === words.length;

  // begins timer as soon as user starts typing
  useEffect(() => {
    if (isStarting) {
      setState("run");
      startCountdown();
    }
  }, [isStarting, startCountdown, cursor]);



  // when time reaches 0
  useEffect(() => {
    if (!timeLeft) {
      console.log("time is up!");
      setState("finish");
      //sumErrors();
    }
  }, [timeLeft]);

  

    useEffect(() => {
        const wordsReached = words.substring(0, cursor);
        setErrors(countErrors(typed, wordsReached));
    }, [errors, countErrors, typed]);


  const restart = useCallback(() => {
    console.log("restarting..");
    resetCountdown();
    resetTotalTyped();
    setState("start");
    setErrors(0);
    updateWords();
    clearTyped();

  }, [clearTyped, updateWords, resetCountdown, resetTotalTyped])

  return {
    state,
    words,
    timeLeft,
    typed,
    errors,
    totalTyped,
    restart
  };
};

export default useEngine;