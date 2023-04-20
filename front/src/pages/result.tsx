import { Dispatch, SetStateAction, useState } from "react";
import {
  Text,
  Flex,
  Button,
  HStack,
  Stack,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  Icon,
  Box,
  useColorModeValue,
  Spacer,
  CircularProgress,
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

interface ResultProps {
  lpm: number;
  acc: number;
  currLang: string;
  setCurrLang: Dispatch<SetStateAction<string>>;
}

// TODO: replace this instance w real prop
const myProp = {
  lpm: 16,
  acc: 85,
  currLang: "PYTHON",
}

export default function Result(props: ResultProps) {
  const { currentUser, methods } = useAuth();

  return (
    <>
      <Flex justifyContent="center" maxH="100vh">
        <Flex direction='column' alignContent='center' alignItems='center' w="80%" gap={10} paddingY="14" >
            <Flex direction="row" w="100%">
              <Flex direction="column" alignItems="start" justifyContent="space-around">
                  <Stack alignItems="flex-start">
                    <Text fontFamily="code" fontSize="s" fontWeight="regular">LINES/MIN</Text>
                    <Text fontSize="8xl" fontWeight="bold">{myProp.lpm}</Text>
                  </Stack>
                  <Stack alignItems="flex-start">
                    <Text fontFamily="code" fontSize="s" fontWeight="regular">ACCURACY</Text>
                    <HStack>
                        <Text fontSize="8xl" fontWeight="bold">{myProp.acc}%</Text>
                        <CircularProgress value={myProp.acc} size="75px" thickness={15} color={useColorModeValue("light.blue", "dark.lightblue")} />
                    </HStack>
                  </Stack>
              </Flex>
              <Spacer/>
              <Box 
                className="code-box" 
                borderRadius={30} 
                w="50vw" 
                h="sm"
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
            </Flex>
            {/* <Flex direction="row" gap={6}>
              <Box 
                className="chatgpt-box" 
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
            </Flex> */}
            {/* continue and skip buttons */}
            <HStack gap={4} alignItems="flex-end">
              <Stack alignItems="center" gap={4}>
                <Menu>
                  <MenuButton as={Button} fontSize="sm" borderRadius={30} height={6} width={36} leftIcon={<Icon as={IoIosArrowDropdownCircle}/>}>
                    {myProp.currLang}
                  </MenuButton>
                  <MenuList>
                    {languages.map((lang) => {
                        return (
                          <MenuItem onClick={() => props.setCurrLang(lang)}>{lang}</MenuItem>
                        );
                      })}
                  </MenuList>
                </Menu>
                <Button borderRadius={30} height={10} width={40} variant="solid" bgColor="green.200">CONTINUE</Button>
              </Stack>
              <Button borderRadius={30} height={8} width={28} variant="outline">SKIP</Button>
            </HStack>
        </Flex>
      </Flex>
    </>
  );
}
