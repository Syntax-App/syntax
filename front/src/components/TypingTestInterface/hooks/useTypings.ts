import { useCallback, useEffect, useRef, useState } from "react";

// determines if valid characater is typed
const isKeyboardCodeAllowed = (code: string) => {
    return (
      // code.startsWith("Key") ||
      // code.startsWith("Digit") ||
      // code === "Backspace" ||
      // code === "Space"
      !code.startsWith("Meta") && 
      !code.startsWith("F") && 
      !code.startsWith("Num") &&
      !code.startsWith("Scroll") &&
      !code.startsWith("Shift") &&
      !code.startsWith("Control") 
    );

};


const useTypings = (enabled: boolean) => {
    const [cursor, setCursor] = useState(0);
    const [typed, setTyped] = useState<string>("");
    const totalTyped = useRef(0);

    const keydownHandler = useCallback((keyEvent: KeyboardEvent) => {
        if (!enabled || !isKeyboardCodeAllowed(keyEvent.code)) return;

        switch (keyEvent.key) {
          case "Backspace":
            setTyped((prev) => prev.slice(0, -1)); // remove from typed string
            setCursor(cursor - 1); // set cursor back by 1
            totalTyped.current -= 1;
            break;
          case "Enter":
            setTyped((prev) => prev.concat("\n"));
            break;
          case "Tab":
            keyEvent.preventDefault();
            setTyped((prev) => prev.concat("\t"));
            break;
          default:
            setTyped((prev) => prev.concat(keyEvent.key)); // otherwise add character to typed string
            setCursor(cursor + 1);
            totalTyped.current += 1;
            break;
        }


    }, 
    [cursor, enabled]);

    const clearTyped = useCallback(() => {
        setTyped("");
        setCursor(0);
    }, []);

    // rests total amount of characters typed
    const resetTotalTyped = useCallback(() => {
        totalTyped.current = 0;

    }, []);

    // attach keydown event listener to record keystrokes
    useEffect(() => {
        window.addEventListener("keydown", keydownHandler);

        // remove event listeners on cleanup 
        return () => {
            window.removeEventListener("keydown", keydownHandler);
        };
    }, [keydownHandler]);

    return {
        typed,
        cursor,
        clearTyped,
        resetTotalTyped,
        totalTyped: totalTyped.current,
    };
}

export default useTypings;