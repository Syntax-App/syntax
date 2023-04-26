import React from "react";
import {
  Text,
  Flex,
  Box,
  useColorModeValue,
} from "@chakra-ui/react";
import TopButtons from "@/components/TopButtons";
import ControlButtons from "@/components/ControlButtons";
import WordsContainer from "@/components/WordsContainer";

const gptSays = `This Java program starts by creating a HashMap named "languages" to store a mapping of programming languages and their positions. It adds three key-value pairs to the map using the put() method, with the keys being string values representing the positions (e.g. "pos1", "pos2", "pos3") and the values being string values representing the programming languages (e.g. "Java", "Python", "JS").`;

interface TypeTestProps {
    typeMode: boolean;
    currLang: string;
    setCurrLang: React.Dispatch<React.SetStateAction<string>>;
    timeLeft: number;
    errors: number;
    typed: string;
    totalTyped: number;
    words: string;
    startTest: () => void;
    restart: () => void;
    stats: {acc: number, lpm: number};
    setStats: React.Dispatch<React.SetStateAction<{acc: number, lpm: number}>>;
}

export default function TypeTest(props: TypeTestProps) {
      
    return (
        <>
            <Flex justifyContent="center">
            <Flex
                direction="column"
                alignContent="center"
                alignItems="center"
                w="80%"
                gap={props.typeMode ? 5 : 14}
                paddingY="14"
            >
                {/* language dropdown and regenerate button // accuracy and lpm in type mode */}
                <TopButtons typeMode={props.typeMode} 
                    currLang={props.currLang} 
                    setCurrLang={props.setCurrLang} timeLeft={props.timeLeft}
                    errors={props.errors}
                    totalTyped={props.totalTyped}
                    stats={props.stats}
                    setStats={props.setStats}/>
                
                {/* code box and chatgpt explanations */}
                <Flex direction="row" gap={6}>
                <Box
                    className="stats-box"
                    borderRadius={30}
                    w={props.typeMode ? "80vw" : "60vw"}
                    h="md"
                    transition={"ease 1s"}
                    padding={5}
                    bg={useColorModeValue("light.lightblue", "dark.darkblue")}
                    overflowY="scroll"
                    
                >
                    <WordsContainer userInput={props.typed} words={props.words} typeMode={props.typeMode}/>
                </Box>
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
                <ControlButtons typeMode={props.typeMode} startTest={props.startTest} restart={props.restart}/>
            </Flex>
            </Flex>
        </>
    );
}