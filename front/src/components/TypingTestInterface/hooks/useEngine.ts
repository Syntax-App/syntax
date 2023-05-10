import { useCallback, useEffect, useState } from "react";
import useCountdownTimer from "./useCountdownTimer";
import useTypings from "./useTypings";
import { countErrors } from "../utils/typetesthelper";
import { type } from "os";

// SPEED-TYPING INTERFACE REFERENCED FROM: https://www.youtube.com/watch?v=oc7BMlIU3VY

// states to keep track of the game status
export type State = "start" | "run" | "finish" | "idle";

export const NUMBER_WORDS = 20;

const useEngine = (countdownTime: number) => {
  const [state, setState] = useState<State>("start");
  const [words, updateWords] = useState<string>("");
  const { timeLeft, startCountdown, resetCountdown, setTimeLeft, timeElapsed, setTotalTime} =
    useCountdownTimer(1);

  // record keyboard strokes during start or run
  const { typed, cursor, clearTyped, resetTotalTyped, totalTyped } = useTypings(
    state !== "finish" && state !== "idle"
  );

  const [errors, setErrors] = useState(0);
  const isStarting = state === "start" && cursor > 0;
  const finishedTyping = cursor === words.length;

  //update timer based on number of lines in words
  useEffect(() => {
    // split words on /n
    const numLines = words.split("\n").length;
    let time = Math.ceil(2 * numLines);
    if (time > 60) time = 60;
    setTimeLeft(time);
    setTotalTime(time);
  }, [words])

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

  // when user finishes typing
  useEffect(() => {
    if (timeLeft && finishedTyping && state === "run") {
      setState("finish");
    }
  }, [finishedTyping]);

  useEffect(() => {
    const wordsReached = words.substring(0, cursor);
    setErrors(countErrors(typed, wordsReached));
  }, [errors, countErrors, typed]);

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
    timeElapsed,
  };
};

export default useEngine;
