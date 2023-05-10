import { useCallback, useEffect, useRef, useState } from "react";

// input seconds: amount of time
const useCountdownTimer = (seconds: number) => {
    const [timeLeft, setTimeLeft] = useState(seconds);
    const [timeElapsed, setTimeElapse] = useState(1);
    const [totalTime, setTotalTime] = useState(timeLeft);

    const intervalRef = useRef<NodeJS.Timer | undefined>(undefined);
    const hasTimerEnded = timeLeft <= 0;
    const isRunning =
      intervalRef.current != null || intervalRef.current != undefined;

    const startCountdown = useCallback(() => {
        console.log("starting countdown...");

        // decrement by 1 every second, if not already running
        if (!hasTimerEnded && !isRunning){
            intervalRef.current = setInterval(() => {
                setTimeLeft((prevTimeLeft) => prevTimeLeft - 1);
                setTimeElapse((prevTimeElapse) => prevTimeElapse + 1);
            }, 1000);
        }
    }, [setTimeLeft, hasTimerEnded, isRunning, setTimeElapse]);

    const resetCountdown = useCallback(() => {
        clearInterval(intervalRef.current);
        intervalRef.current = undefined;
        // store original time left from starting amount of seconds
        console.log("totaltime:" + totalTime);
        setTimeLeft(totalTime);
        setTimeElapse(0);

    }, [totalTime]);

    // clear countdown when it reaches 0
    useEffect(() => {
        if (hasTimerEnded){
            clearInterval(intervalRef.current!);
            intervalRef.current = undefined;
        }
    }, [timeLeft, intervalRef])

    // clear interval when component unmounts
    useEffect(() => {
        return () => clearInterval(intervalRef.current!);
    }, []);

    return {timeLeft, startCountdown, resetCountdown, setTimeLeft, timeElapsed, setTotalTime};
}

export default useCountdownTimer;