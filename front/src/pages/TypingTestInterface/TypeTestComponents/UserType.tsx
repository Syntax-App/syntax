import { Text } from "@chakra-ui/react";
import Caret from "./Caret";

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

  if (!props.typeMode) return null;

  return (
    <div>
      {typedChars.map((char, i) => {
        return (
          <Character
            key={`${char}_${i}`}
            actual={char}
            expected={words[i]}
            linesCompleted={props.linesCompleted}
            setLinesCompleted={props.setLinesCompleted}
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
  linesCompleted: number;
  setLinesCompleted: React.Dispatch<React.SetStateAction<number>>;
}
const Character = (props: CharacterProps) => {
  // checks if typed char matches expected char
  const isCorrect = props.actual === props.expected;
  if (props.expected === "\n") {
    props.setLinesCompleted(props.linesCompleted + 1);
  }
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
