import { Text } from "@chakra-ui/react";
import Caret from "./Caret";
import { useEffect, useMemo } from "react";

interface UserTypeProps {
  userInput: string;
  words: string;
  typeMode: boolean;
  linesCompleted: number;
  setLinesCompleted: React.Dispatch<React.SetStateAction<number>>;
}

const UserType = (props: UserTypeProps) => {
  const typedChars = props.userInput.split("");
  const words = props.words.trim();

  
  // if (words[i] === "\n") {
  //   props.setLinesCompleted(props.linesCompleted + 1);
  // }

  useMemo(() => {
    if (words[props.userInput.length] === "\n"){
      props.setLinesCompleted(props.linesCompleted + 1);
    }
  }, [props.userInput])

  if (!props.typeMode) return null;

  return (
    <div data-testid="usertype">
      {typedChars.map((char, i) => {
         
        return (
          <Character
            key={`${char}_${i}`}
            actual={char}
            expected={words[i]}
          /> // expected is the character at words[i]
        );
      })}
      <Caret />
    </div>
  );
};

interface CharacterProps {
  actual: string;
  expected: string;
}
const Character = (props: CharacterProps) => {
  // checks if typed char matches expected char
  const isCorrect = props.actual === props.expected;
  
  //console.log("expected:" + props.expected)
  const isWhiteSpace = props.expected === " ";

  return (
    <Text
      as="span"
      bg={isWhiteSpace && !isCorrect ? "#ee6f2a" : "none"}
      color={isCorrect ? "#eac747" : "#ee6f2a"}
    >
      {props.expected}
    </Text>
  );
};

export default UserType;
