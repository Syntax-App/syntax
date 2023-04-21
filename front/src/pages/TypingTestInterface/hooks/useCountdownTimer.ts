import { useCallback, useEffect, useRef, useState } from "react";

// input seconds: amount of time
const useCountdownTimer = (seconds: number) => {
    const [timeLeft, setTimeLeft] = useState(seconds);
    const intervalRef = useRef<NodeJS.Timer | null>(null);

    const startCountdown = useCallback(() => {
        console.log("starting countdown...");

        // at every seconds, decrements time left
        intervalRef.current = setInterval(() => {
            setTimeLeft((timeLeft) => timeLeft - 1);
        }, 1000)

    }, [setTimeLeft]);

    const resetCountdown = useCallback(() => {
        console.log("resetting countdown...");

        if (intervalRef.current){
            clearInterval(intervalRef.current);
        }

        // store original time left from starting amount of seconds
        setTimeLeft(seconds);

    }, [seconds]);

    // clear countdown when it reaches 0
    useEffect(() => {
        if (!timeLeft && intervalRef.current){
            console.log("clearing timer...");
            clearInterval(intervalRef.current);
        }
    }, [timeLeft, intervalRef])

    return {timeLeft, startCountdown, resetCountdown};


}

export default useCountdownTimer;