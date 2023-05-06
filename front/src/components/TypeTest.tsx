import React from "react";
import { useState, useEffect } from "react";
import { Text, Flex, Box, useColorModeValue } from "@chakra-ui/react";
import TopButtons from "@/components/TopButtons";
import ControlButtons from "@/components/ControlButtons";
import WordsContainer from "@/components/WordsContainer";
import { SkeletonText } from "@chakra-ui/react";
import { State } from "@/pages/TypingTestInterface/hooks/useEngine";
import Hotkeys from "react-hot-keys";

// let gptSays = `This Java program starts by creating a HashMap named "languages" to store a mapping of programming languages and their positions. It adds three key-value pairs to the map using the put() method, with the keys being string values representing the positions (e.g. "pos1", "pos2", "pos3") and the values being string values representing the programming languages (e.g. "Java", "Python", "JS").`;

interface TypeTestProps {
  state: string;
  setState: React.Dispatch<React.SetStateAction<State>>;
  typeMode: boolean;
  currLang: string;
  setCurrLang: React.Dispatch<React.SetStateAction<string>>;
  timeLeft: number;
  errors: number;
  typed: string;
  totalTyped: number;
  words: string;
  gptSays: string;
  startTest: () => void;
  restart: () => void;
  stats: { acc: number; lpm: number };
  setStats: React.Dispatch<React.SetStateAction<{ acc: number; lpm: number }>>;
  COUNTDOWN_SECONDS: number;
  timeElapsed: number;
  getNewSnippet: () => void;
  loadGpt: boolean;
  languages: string[];
}

export default function TypeTest(props: TypeTestProps) {
  const [lpm, setlpm] = useState(0);
  const [restartShortcut, setRestartShortcut] = useState(false);

  useEffect(() => {
    if (props.state === "start") {
      setlpm(0);
    }
  }, [props.state]);

  // prevents typing while code snippet is loading
  useEffect(() => {
    if (props.loadGpt) {
      props.setState("idle");
    } else {
      props.setState("idle");
    }
  }, [props.loadGpt]);

  // RESTART SHORTCUT

  useEffect(() => {
    if (restartShortcut) {
      setRestartShortcut(false);
      props.restart();
    }
  }, [restartShortcut, setRestartShortcut]);

  // useEffect(() => {
  //   const handleKeyPress = (e: KeyboardEvent) => {
  //     if (e.shiftKey && e.key === "r" && props.state == "running") {
  //       console.log("RESTART SHORTCUT");
  //       props.restart;
  //     }
  //   };
  //   window.addEventListener("keydown", handleKeyPress);
  // }, [])

  return (
    <>
      <Hotkeys
        keyName="shift+r"
        onKeyDown={() => {
          console.log("pressed shortcut");
          setRestartShortcut(true);
        }}
        onKeyUp={() => {
          setRestartShortcut(true);
        }}
      >
        <Flex justifyContent="center" data-testid="typetest">
          <Flex
            direction="column"
            alignContent="center"
            alignItems="center"
            w="80%"
            gap={props.typeMode ? 5 : 14}
            paddingY="14"
          >
            {/* language dropdown and regenerate button // accuracy and lpm in type mode */}
            <TopButtons
              typeMode={props.typeMode}
              languages={props.languages}
              currLang={props.currLang}
              setCurrLang={props.setCurrLang}
              timeLeft={props.timeLeft}
              errors={props.errors}
              totalTyped={props.totalTyped}
              stats={props.stats}
              setStats={props.setStats}
              lpm={lpm}
              getNewSnippet={props.getNewSnippet}
            />

            {/* code box and chatgpt explanations */}
            <Flex direction="row" gap={6}>
              <Flex
                className="stats-box"
                borderRadius={30}
                w={props.typeMode ? "80vw" : "60vw"}
                h="md"
                transition={"ease 1s"}
                padding={5}
                bg={useColorModeValue("light.lightblue", "dark.darkblue")}
                overflowY="scroll"
              >
                <WordsContainer
                  userInput={props.typed}
                  words={props.words}
                  typeMode={props.typeMode}
                  setlpm={setlpm}
                  COUNTDOWN_SECONDS={props.COUNTDOWN_SECONDS}
                  timeElapsed={props.timeElapsed}
                  loadGpt={props.loadGpt}
                  state={props.state}
                />
              </Flex>
              <Box
                display={props.typeMode ? "none" : "show"}
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
                {props.loadGpt ? (
                  <Box w="27vw">
                    <SkeletonText
                      height="20px"
                      noOfLines={10}
                      spacing={4}
                      skeletonHeight={4}
                      fadeDuration={30}
                      startColor="dark.indigo"
                      endColor="dark.blue"
                    />
                  </Box>
                ) : (
                  <Text
                    fontSize="md"
                    fontWeight="medium"
                    textAlign="justify"
                    color="blue.200"
                    px={4}
                  >
                    {props.gptSays}
                  </Text>
                )}
              </Box>
            </Flex>
            {/* start, skip, restart buttons */}
            <ControlButtons
              typeMode={props.typeMode}
              startTest={props.startTest}
              restart={props.restart}
            />
          </Flex>
        </Flex>
      </Hotkeys>
    </>
  );
}
