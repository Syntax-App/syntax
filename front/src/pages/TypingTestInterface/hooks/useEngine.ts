import { useCallback, useEffect, useState } from "react";
import useWords from "./useWords";
import useCountdownTimer from "./useCountdownTimer";
import useTypings from "./useTypings";
import { countErrors } from "../utils/typetesthelper";
import { type } from "os";

// SPEED-TYPING INTERFACE REFERENCED FROM: https://www.youtube.com/watch?v=oc7BMlIU3VY

// states to keep track of the game status
export type State = "init" | "start" | "run" | "finish";

const NUMBER_WORDS = 20;
const COUNTDOWN_SECONDS = 15;

const useEngine = () => {
  const [state, setState] = useState<State>("init");
  const [words, updateWords] = useState<string>("");
  const { timeLeft, startCountdown, resetCountdown } =
    useCountdownTimer(COUNTDOWN_SECONDS);
  // record keyboard strokes during start or run
  const { typed, cursor, clearTyped, resetTotalTyped, totalTyped } = useTypings(
    ((state !== "finish") && (state !== "init"))
  );
  const [errors, setErrors] = useState(0);
  const isStarting = state === "start" && cursor > 0;
  const [linesCompleted, setLinesCompleted] = useState(0);
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
    if (!timeLeft && state === "run") {
      setState("finish");
    }
  }, [timeLeft, state]);

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
    clearTyped();
  }, [clearTyped, resetCountdown, resetTotalTyped]);

  return {
    state,
    setState,
    words,
    updateWords,
    timeLeft,
    typed,
    errors,
    totalTyped,
    restart,
  };
};

export default useEngine;
