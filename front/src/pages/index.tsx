import React, { useEffect } from "react";
import { useState } from "react";
import useEngine from "./TypingTestInterface/hooks/useEngine";
import Result from "@/components/result";
import TypeTest from "@/components/TypeTest";

const code: string = `class Main {\npublic static void main(String[] args) {\n\tMap<String, String> languages = new HashMap<>();\nlanguages.put("pos3", "JS");
    languages.put("pos1", "Java");
    languages.put("pos2", "Python");
    System.out.println("Map: " + languages);
    TreeMap<String, String> sortedNumbers = new TreeMap<>(languages);
    System.out.println("Map with sorted Key" + sortedNumbers); 
    Map<String, String> languages = new HashMap<>(); 
    languages.put("pos3", "JS");
    languages.put("pos1", "Java");
    languages.put("pos2", "Python");
    System.out.println("Map: " + languages);
    TreeMap<String, String> sortedNumbers = new TreeMap<>(languages);
    System.out.println("Map with sorted Key" + sortedNumbers); 
  } 
}`;

export default function Home() {
  const [ currLang, setCurrLang ] = useState("PYTHON");
  const [typeMode, setMode] = useState(false);
  const {state, setState, words, updateWords, timeLeft, typed, errors, restart, totalTyped} = useEngine();
  const [stats, setStats] = useState({acc: 0, lpm: 0});

  const startTest = () => {
    setMode(true);
    setState("start");
  }

  // TODO: change this later
  // update words w hardcoded code
  useEffect(() => {
    updateWords(code);
  }, [words]);
  
  return (
    <>
      <Flex justifyContent="center">
        <Flex
          direction="column"
          alignContent="center"
          alignItems="center"
          w="80%"
          gap={typeMode ? 5 : 14}
          paddingY="14"
        >
          {/* language dropdown and regenerate button // accuracy and lpm in type mode */}
          <TopButtons typeMode={typeMode} 
          currLang={currLang} 
          setcurrLang={setcurrLang} timeLeft={timeLeft}
          errors={errors}
          totalTyped={totalTyped}/>
          
          {/* code box and chatgpt explanations */}
          <Flex direction="row" gap={6}>
            <Box
              className="stats-box"
              borderRadius={30}
              w={typeMode ? "80vw" : "60vw"}
              h="md"
              transition={"ease 1s"}
              padding={5}
              bg={useColorModeValue("light.lightblue", "dark.darkblue")}
              overflowY="scroll"
              
            >
              <WordsContainer userInput={typed} words={words} typeMode={typeMode}/>
            </Box>
            <Box
              display={typeMode ? "none" : "show"}
              transition={"ease 1s"}
              className="stats-box"
              borderRadius={30}
              w="30vw"
              h="md"
              padding={5}
              bg={useColorModeValue("light.indigo", "dark.indigo")}
              overflowY="scroll"
            >
              <Text
                fontSize="xl"
                fontWeight={600}
                textAlign="center"
                color="blue.100"
              >
                ChatGPT Says...
              </Text>
              <br />
              <Text
                fontSize="md"
                fontWeight="medium"
                textAlign="justify"
                color="blue.200"
                px={4}
              >
                {gptSays}
              </Text>
            </Box>
          </Flex>
          {/* start, skip, restart buttons */}
          <ControlButtons typeMode={typeMode} startTest={startTest} restart={restart}/>
        </Flex>
      </Flex>
    </>
  );
}

interface WordsProps{
  userInput: string;
  words: string; // this props.code is for random generated
  typeMode: boolean;
}
const WordsContainer = (props: WordsProps) => {
  return (
    <div
      className="typetest"
      autoFocus={true}
      onBlur={({ target }) => target.focus()}
    >
      <div
        className="usertyped"
        onKeyDown={(e) => {
          if (e.code === "Tab") {
            e.preventDefault();
            console.log("tabbed");
          }
        }}
      >
        <pre>
          <code>
            <UserType
              userInput={props.userInput}
              words={props.words}
              typeMode={props.typeMode}
            />
  }
}