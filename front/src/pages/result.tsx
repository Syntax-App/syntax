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
  createIcon,
  Box,
  useColorModeValue,
  Spacer,
  CircularProgress,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
} from "@chakra-ui/react";
import { useAuth } from "@/contexts/AuthContext";
import { IoIosArrowDropdownCircle } from "react-icons/io";
import { useRouter } from "next/router";

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

// TODO: replace this instance w real prop
const myProp = {
  lpm: 16,
  acc: 85,
  currLang: "PYTHON",
}

const ChatGPTIcon = createIcon({
  displayName: 'ChatGPTIcon',
  viewBox: '0 0 340 342',
  path: (
    <path 
      fill="black"
      fill-rule="evenodd" 
      clip-rule="evenodd" 
      d="M128.859 2.70548C115.639 6.37226 102.896 13.5404 92.0349 23.4198C83.9523 30.7735 79.7541 36.5939 73.2566 49.4572L68.1944 59.4797L60.32 61.944C24.3831 73.1902 0.184906 105.739 0.00379336 143.076C-0.0558276 155.393 0.560631 159.789 3.76104 169.837C6.75221 179.232 10.4397 186.532 16.7438 195.541L21.8262 202.804L20.3852 207.834C19.5932 210.601 18.7034 218.938 18.4109 226.361C17.8046 241.683 19.9566 252.889 25.9468 265.607C41.8183 299.304 77.3198 319.251 114.016 315.093L123.277 314.043L128.359 318.846C135.683 325.766 140.437 328.99 150.45 333.827C170.193 343.364 198.396 344.825 218.795 336.778C241.64 327.765 259.186 311.351 267.893 290.847C269.534 286.983 271.184 283.518 271.558 283.144C271.934 282.772 276.192 281.14 281.022 279.517C307.921 270.479 327.648 250.46 336.239 223.481C339.439 213.433 340.056 209.037 339.996 196.72C339.91 178.815 334.279 161.804 323.453 146.731L318.326 139.592L320.095 131.863C322.618 120.843 322.407 103.009 319.624 92.2013C312.115 63.0336 291.148 40.7133 261.681 30.5187C243.499 24.7188 227.589 26.4266 216.311 27.1246L210.311 21.8541C202.747 15.209 191.959 8.765 181.812 4.82888C167.221 -0.829149 143.972 -1.48597 128.859 2.70548ZM165.63 23.9227C174.873 25.8696 194.262 35.5344 192.416 37.2756C191.754 37.9003 163.263 54.3947 140.19 67.5106C129.671 73.4897 120.192 79.471 119.122 80.8031C117.348 83.0148 117.177 87.5243 117.153 132.678C117.139 162.213 116.709 182.131 116.088 182.131C114.699 182.131 91.2261 168.931 88.7243 166.743C86.9165 165.161 86.759 161.728 86.795 124.682C86.84 78.3512 87.6151 71.3886 94.2038 58.1442C99.7395 47.0164 112.286 34.4269 123.034 29.2123C136.482 22.6889 151.134 20.8695 165.63 23.9227ZM249.352 50.8318C264.99 54.7814 279.676 64.6988 287.988 76.9229C295.176 87.493 298.497 97.274 299.119 109.698C299.417 115.654 299.363 121.296 298.998 122.239C298.281 124.098 294.025 121.814 249.414 95.6178C237.098 88.3859 225.455 82.2336 223.541 81.9453C219.777 81.3787 223.028 79.6576 163.438 113.766C147.146 123.092 133.604 130.722 133.347 130.722C133.089 130.722 132.878 123.299 132.878 114.228C132.878 98.6442 133.009 97.6507 135.257 96.2559C136.565 95.4446 152.984 86.0256 171.744 75.3247C204.703 56.5237 213.061 52.335 221.648 50.3133C228.252 48.7587 242.174 49.0191 249.352 50.8318ZM65.3821 127.307V169.712L67.9132 172.457C69.3047 173.966 77.2782 179.097 85.6307 183.86C141.991 215.997 152.531 222.199 152.267 223.067C151.915 224.22 126.344 239.127 124.717 239.127C124.102 239.127 109.361 230.987 91.9595 221.038C74.557 211.088 58.123 201.716 55.4389 200.211C48.3159 196.218 37.502 185.889 32.6356 178.432C25.5497 167.574 23.211 159.063 23.2121 144.133C23.2132 124.989 27.9908 113.204 41.1006 100.004C47.38 93.6821 60.6057 84.9594 63.976 84.9181C65.0761 84.9046 65.3821 94.118 65.3821 127.307ZM218.965 104.326C220.185 105.174 233.839 113.036 249.307 121.796C286.701 142.974 293.746 147.581 301.114 155.67C319.016 175.323 322.521 204.233 309.896 228.102C303.712 239.793 290.855 251.883 279.668 256.527L275.743 258.156L275.738 216.511C275.735 177.738 275.589 174.635 273.626 171.514C272.277 169.369 267.11 165.678 259.287 161.27C239.242 149.977 190.976 122.032 188.674 120.387C186.711 118.984 187.678 118.205 200.485 110.875C208.157 106.484 214.954 102.866 215.59 102.837C216.226 102.807 217.745 103.477 218.965 104.326ZM188.561 138.917L206.56 149.303L206.846 171.025L207.13 192.748L188.88 203.236L170.629 213.725L163.54 209.942C159.639 207.861 151.272 203.123 144.945 199.413L133.44 192.668L133.135 171.259L132.829 149.85L141.01 145.093C145.508 142.477 153.741 137.67 159.305 134.411C164.87 131.153 169.679 128.497 169.992 128.509C170.306 128.522 178.662 133.205 188.561 138.917ZM237.588 167.141C244.899 171.189 251.449 175.182 252.143 176.012C253.045 177.093 253.287 190.215 252.99 222.016C252.593 264.595 252.467 266.834 250.042 274.088C238.958 307.236 206.143 325.787 172.52 317.91C165.149 316.185 154.482 311.33 149.823 307.583L147.09 305.384L149.827 303.69C153.915 301.161 185.32 283.077 202.623 273.292C210.975 268.568 218.948 263.446 220.34 261.91L222.871 259.119V209.449C222.871 182.131 223.192 159.779 223.582 159.779C223.974 159.779 230.276 163.092 237.588 167.141ZM206.3 244.862C203.979 247.167 131.972 287.364 126.354 289.491C118.364 292.515 101.934 293.611 92.4298 291.752C70.6872 287.503 52.5681 271.937 44.3427 250.441C41.1006 241.968 39.123 219.011 41.635 219.011C42.9421 219.011 51.3599 223.734 93.1182 247.894C106.287 255.512 115.864 260.361 117.744 260.361C120.79 260.361 125.256 257.933 180.124 226.445L206.56 211.274L206.873 227.629C207.044 236.623 206.786 244.379 206.3 244.862Z"
      />
  ),
})

// TODO: remove props!
export default function Result(props: any) {
  const { currentUser, methods } = useAuth();
  const { isOpen, onOpen, onClose } = useDisclosure()
  const router = useRouter();
  const { lpm, acc, currLang } = router.query

  return (
    <>
      <Flex justifyContent="center" maxH="100vh - 64">
        <Flex direction='column' alignContent='center' alignItems='center' w="80%" gap={20} paddingY="14" >
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
              <Stack alignItems="center" gap={8}>
                {/* code display */}
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
                {/* button/modal for ChatGPT explanation */}
                <Button 
                  onClick={onOpen}
                  bg={useColorModeValue("light.lightblue", "dark.indigo")} 
                  color={useColorModeValue("light.blue", "dark.extralight")} 
                  w="70%" 
                  borderRadius={15}
                  leftIcon={<ChatGPTIcon/>}
                  _hover={{
                    bg: useColorModeValue("blue.400", "blue.600") 
                  }}
                  >ChatGPT Says...</Button>

                <Modal onClose={onClose} isOpen={isOpen} isCentered scrollBehavior="inside">
                  <ModalOverlay />
                  <ModalContent>
                    <ModalHeader>ChatGPT Says...</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                      {gptSays}
                    </ModalBody>
                    <ModalFooter>
                      <Button onClick={onClose}>Close</Button>
                    </ModalFooter>
                  </ModalContent>
                </Modal>
              </Stack>
            </Flex>
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
                          // TODO: setCurrLang(lang) HOW TO GET ACCESS TO THIS??
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
