import {
  Box,
  Flex,
  Button,
  Avatar,
  useColorModeValue,
  Stack,
  useColorMode,
} from "@chakra-ui/react";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import { FaUser } from "react-icons/fa";
import { IoIosStats } from "react-icons/io";
import { Icon } from "@chakra-ui/react";
import Link from "next/link";
import { useAuth } from "@/contexts/AuthContext";

// NavBar component
export default function NavBar() {
  const { colorMode, toggleColorMode } = useColorMode();
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");
  const {currentUser, methods} = useAuth();

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
                <Avatar
                  size={'sm'}
                  name={currentUser?.displayName ? currentUser?.displayName : undefined}
                  src={currentUser?.photoURL ? currentUser?.photoURL : undefined}
                />
              </Button>
            </Stack>
          </Flex>
        </Flex>
      </Box>
    </>
  );
}
