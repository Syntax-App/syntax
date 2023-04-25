import React, { useEffect } from "react";
import { useState } from "react";
import {
  Text,
  Flex,
  Button,
  HStack,
  VStack,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  Icon,
  Box,
  useColorModeValue,
} from "@chakra-ui/react";
import { useAuth } from "@/contexts/AuthContext";
import { RepeatIcon } from "@chakra-ui/icons";
import { IoIosArrowDropdownCircle } from "react-icons/io";
import RestartButton from "./TypingTestInterface/TypeTestComponents/RestartButton";
import TestResults from "./TypingTestInterface/TypeTestComponents/TestResults";
import UserType from "./TypingTestInterface/TypeTestComponents/UserType";
import useEngine from "./TypingTestInterface/hooks/useEngine";
import { calculateAccuracy } from "./TypingTestInterface/utils/typetesthelper";
import { useRouter } from "next/router";

const languages = ["PYTHON", "JAVA", "JAVASCRIPT", "C++", "C"];

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

const gptSays = `This Java program starts by creating a HashMap named "languages" to store a mapping of programming languages and their positions. It adds three key-value pairs to the map using the put() method, with the keys being string values representing the positions (e.g. "pos1", "pos2", "pos3") and the values being string values representing the programming languages (e.g. "Java", "Python", "JS").`;

export default function Home() {
  const { currentUser, methods } = useAuth();
  const [ currLang, setcurrLang ] = useState("PYTHON");
  const [typeMode, setMode] = useState(false);
  const {setState, words, updateWords, timeLeft, typed, errors, restart, totalTyped} = useEngine();

  const startTest = () => {
    setMode(true);
    setState("start");
  }

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

interface ButtonsInterface{
  startTest: () => void;
  typeMode: boolean;
  restart: () => void;
}
const ControlButtons = (props: ButtonsInterface) => {
  return (
    <HStack gap={4}>
      <Button
        borderRadius={30}
        height={10}
        width={32}
        variant="solid"
        bgColor="green.200"
        onClick={props.startTest}
        display={props.typeMode ? "none" : "show"}
      >
        START
      </Button>
      <Button
        borderRadius={30}
        height={8}
        width={28}
        variant="outline"
        onClick={props.startTest}
        display={props.typeMode ? "none" : "show"}
      >
        SKIP
      </Button>
      <RestartButton
        typeMode={props.typeMode}
        onRestart={props.restart}
      ></RestartButton>
    </HStack>
  );
}

interface TopButtonsProps {
  typeMode: boolean;
  currLang: string;
  setcurrLang: (lang: string) => void;
  timeLeft: number;
  errors: number;
  totalTyped: number;
}
const TopButtons = (props: TopButtonsProps) => {
  const CountdownTimer = ({ timeLeft }: { timeLeft: number }) => {
    return (
      <Text
        variant={"bigNumber"}
        color={"useColorModeValue(colors.light.blue, colors.dark.lightblue)"}
      >
        {timeLeft}
      </Text>
    );
  };

  return (
    <Flex
      justifyContent={props.typeMode ? "space-between" : "center"}
      alignItems={props.typeMode ? "flex-end" : "center"}
      width="100%"
    >
      <Flex
        display={props.typeMode ? "flex" : "none"}
        width="30%"
        justifyContent={"space-between"}
        alignItems={"flex-end"}
      >
        {/* TIMER */}
        
          <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"flex-end"}
          >
            <Text variant={"label"}> TIMER</Text>
            <CountdownTimer timeLeft={props.timeLeft}></CountdownTimer>
          </Flex>
        <Flex
          flexDir={"column"}
          alignItems={"center"}
          justifyContent={"flex-end"}
        >
          <Text variant={"label"}> ACCURACY</Text>
          <Text variant={"bigNumber"}>
            {calculateAccuracy(props.errors, props.totalTyped)}
          </Text>
        </Flex>
        <Flex
          flexDir={"column"}
          alignItems={"center"}
          justifyContent={"space-between"}
        >
          <Text variant={"label"}> ERRORS</Text>
          <Text variant={"bigNumber"}> {props.errors}</Text>
        </Flex>
      </Flex>
      <HStack gap={4}>
        <Menu>
          <MenuButton
            as={Button}
            borderRadius={30}
            height={8}
            width={40}
            leftIcon={<Icon as={IoIosArrowDropdownCircle} />}
          >
            {props.currLang}
          </MenuButton>
          <MenuList>
            {languages.map((lang, key) => {
              return (
                <MenuItem key={key} onClick={() => props.setcurrLang(lang)}>
                  {lang}
                </MenuItem>
              );
            })}
          </MenuList>
        </Menu>
        <Button
          borderRadius={30}
          height={8}
          width={40}
          variant="outline"
          leftIcon={<RepeatIcon />}
        >
          REGENERATE
        </Button>
      </HStack>
    </Flex>
  );
}
