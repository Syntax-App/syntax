import {
  Box,
  Flex,
  Button,
  Center,
  useColorModeValue,
  Stack,
  useColorMode,
} from "@chakra-ui/react";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import { FaUser } from "react-icons/fa";
import { IoIosStats } from "react-icons/io";
import { Icon } from "@chakra-ui/react";
import Link from "next/link";

// NavBar component
export default function NavBar() {
  const { colorMode, toggleColorMode } = useColorMode();
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");

  return (
    <>
      <Box bg={"transparent"} px={4}>
        <Flex h={16} alignItems={"center"} justifyContent={"space-between"}>
          <Link href='/#' className="logo" color={icon_color}>SYNTAX</Link>

          <Flex alignItems={"center"}>
            <Stack direction={"row"} spacing={6}>
              <Button  as='a' href='/leaderboard' bg={"transparent"}>
                  <Icon as={IoIosStats} color={icon_color} />
              </Button>
              <Button onClick={toggleColorMode} bg={"transparent"}>
                {colorMode === "dark" ? <SunIcon color={icon_color}/> : <MoonIcon color={icon_color}/>}
              </Button>
              <Button  as='a' href='/profile' bg={"transparent"}>
                <Icon as={FaUser} color={icon_color} />
              </Button>
            </Stack>
          </Flex>
        </Flex>
      </Box>
    </>
  );
}
