import React from "react";
import { useState } from "react";
import {
  Text,
  Flex,
  Button,
  HStack,
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

const languages = ["PYTHON", "JAVA", "JAVASCRIPT", "C++", "C"];

const code = `class Main { 
  public static void main(String[] args) {
    Map<String, String> languages = new HashMap<>(); 
    languages.put("pos3", "JS");
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

  return (
    <>
      <Flex justifyContent="center">
        <Flex direction='column' alignContent='center' alignItems='center' w="80%" gap={14} paddingY="14" >
            {/* language dropdown and regenerate button */}
            <HStack gap={4}>
              <Menu>
                <MenuButton as={Button} borderRadius={30} height={8} width={40} leftIcon={<Icon as={IoIosArrowDropdownCircle}/>}>
                  {currLang}
                </MenuButton>
                <MenuList>
                  {languages.map((lang) => {
                      return (
                        <MenuItem onClick={() => setcurrLang(lang)}>{lang}</MenuItem>
                      );
                    })}
                </MenuList>
              </Menu>
              <Button borderRadius={30} height={8} width={40} variant="outline" leftIcon={<RepeatIcon />}>REGENERATE</Button>
            </HStack>
            {/* code box and chatgpt explanations */}
            <Flex direction="row" gap={6}>
              <Box 
                className="stats-box" 
                borderRadius={30} 
                w="60vw" 
                h="md"
                padding={5} 
                bg={useColorModeValue("light.lightblue", "dark.darkblue")}
                overflowY="scroll"
                >
                <pre>
                  <code>
                    {code}
                  </code>
                </pre>
              </Box>
              <Box 
                className="stats-box" 
                borderRadius={30} 
                w="30vw" 
                h="md" 
                padding={5} 
                bg={useColorModeValue("light.indigo", "dark.indigo")}
                overflowY="scroll"
                >
                <Text fontSize="xl" fontWeight={600} textAlign="center" color="blue.100">ChatGPT Says...</Text>
                <br />
                <Text fontSize="md" fontWeight="medium" textAlign="justify" color="blue.200" px={4}>{gptSays}</Text>
              </Box>
            </Flex>
            {/* start and skip buttons */}
            <HStack gap={4}>
              <Button borderRadius={30} height={10} width={32} variant="solid" bgColor="green.200">START</Button>
              <Button borderRadius={30} height={8} width={28} variant="outline">SKIP</Button>
            </HStack>
        </Flex>
      </Flex>
    </>
  );
}
