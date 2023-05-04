import { useCallback, useEffect, useState } from "react";
import useWords from "./useWords";
import useCountdownTimer from "./useCountdownTimer";
import useTypings from "./useTypings";
import { countErrors } from "../utils/typetesthelper";
import { type } from "os";

// SPEED-TYPING INTERFACE REFERENCED FROM: https://www.youtube.com/watch?v=oc7BMlIU3VY

// states to keep track of the game status
export type State = "start" | "run" | "finish";

export const NUMBER_WORDS = 20;
export const COUNTDOWN_SECONDS = 10;

const useEngine = () => {
  const [state, setState] = useState<State>("start");
  const [words, updateWords] = useState<string>("");
  const { timeLeft, startCountdown, resetCountdown } =
    useCountdownTimer(COUNTDOWN_SECONDS);
  // record keyboard strokes during start or run
  const { typed, cursor, clearTyped, resetTotalTyped, totalTyped } = useTypings(
    (state !== "finish")
  );

  const [timeElapsed, setTimeElapsed] = useState(1);
  const [errors, setErrors] = useState(0);
  const [linesCompleted, setLinesCompleted] = useState(0);
  const isStarting = state === "start" && cursor > 0;
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
    //setLinesCompleted(countCompletedLines(wordsReached));
  }, [errors, countErrors, typed]);

  useEffect(() => {
    setTimeElapsed(COUNTDOWN_SECONDS - timeLeft);
  }, [timeLeft]);

  const restart = useCallback(() => {
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
    COUNTDOWN_SECONDS,
    timeElapsed
  };
};

export default useEngine;
