import { Text } from "@chakra-ui/react";
import Caret from "./Caret";

interface UserTypeProps{
    userInput: string;
    words: string;
}

const UserType = (props: UserTypeProps) => {
    const typedChars = props.userInput.split("");

    return (
        <div>
            {typedChars.map((char, i) => {
                return (
                    <Character 
                    key={`${char}_${i}`} 
                    actual={char}
                    expected={props.words[i]}/> // expected is the character at words[i]
                );
            })}
            <Caret/>
        </div>
    )
}

interface CharacterProps{
    actual: string;
    expected: string;
}
const Character = (props: CharacterProps) => {
    // checks if typed char matches expected char
    const isCorrect = (props.actual === props.expected);
    const isWhiteSpace = (props.expected === " ");


    return (
      <Text as="span" 
            bg= {isWhiteSpace && !isCorrect? "red" : "none"}
            color={isCorrect? "yellow" : "red"}>
        {props.expected}
      </Text>
    ); 
}

export default UserType;