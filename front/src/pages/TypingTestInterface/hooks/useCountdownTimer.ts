import { useCallback, useEffect, useRef, useState } from "react";

// input seconds: amount of time
const useCountdownTimer = (seconds: number) => {
    const [timeLeft, setTimeLeft] = useState(seconds);
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
            }, 1000);
        }
    }, [setTimeLeft, hasTimerEnded, isRunning]);

    const resetCountdown = useCallback(() => {
        clearInterval(intervalRef.current);
        intervalRef.current = undefined;
        // store original time left from starting amount of seconds
        setTimeLeft(seconds);

    }, [seconds]);

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

    return {timeLeft, startCountdown, resetCountdown};
}

export default useCountdownTimer;