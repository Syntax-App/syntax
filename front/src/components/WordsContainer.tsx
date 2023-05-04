import UserType from "../pages/TypingTestInterface/TypeTestComponents/UserType";
import { useEffect, useRef, useState } from "react";

interface WordsProps {
  userInput: string;
  words: string; // this props.code is for random generated
  typeMode: boolean;
  COUNTDOWN_SECONDS: number;
  //lpm: number;
  setlpm: React.Dispatch<React.SetStateAction<number>>;
  timeElapsed: number;
}

export default function WordsContainer(props: WordsProps) {
  const [linesCompleted, setLinesCompleted] = useState(0);

  const typingBoxRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const typingBox = typingBoxRef.current;

    // prevents tabbing out of the typing box
    if (typingBox) {
      // selects elements that are focusable
      const allFocusableElems = document.querySelectorAll(
        'a, button, input, select, textarea, [tabindex]:not([tabindex="-1"])'
      );

      // sets their tabindex to -1 so they are not focusable
      allFocusableElems.forEach((elem) => {
        elem.setAttribute("tabIndex", "-1");
      });

      // sets focus on the typing box
      typingBox.setAttribute("tabIndex", "0");
      typingBox.focus();

      // resets the tab indices when the component unmounts
      return () => {
        allFocusableElems.forEach((elem) => {
          elem.setAttribute("tabIndex", "0");
        });
      };
    }
  }, []);

  // prevents spacebar from scrolling on typing box
  useEffect(() => {
    const typingBox = typingBoxRef.current;

    function handleKeyDown(e: KeyboardEvent) {
      if (e.key === " " || e.key === "Spacebar") {
        e.preventDefault();
      }
    }

    if (typingBox) {
      typingBox.addEventListener("keydown", handleKeyDown);
    }
  }, []);

  // calculates LPM
  useEffect(() => {
    const perSecond = linesCompleted / props.timeElapsed;
    const linesPerMin = Math.round(perSecond * 60);
    props.setlpm(linesPerMin);
  }, [linesCompleted])

  return (
    <div ref={typingBoxRef} className="typetest">
      <div className="usertyped">
        <pre>
          <code>
            <UserType
              userInput={props.userInput}
              words={props.words}
              typeMode={props.typeMode}
              linesCompleted={linesCompleted}
              setLinesCompleted={setLinesCompleted}
            />
          </code>
        </pre>
      </div>
      <div className="codesnippet">
        <pre>
          <code>{props.words}</code>
        </pre>
      </div>
    </div>
  );
}
