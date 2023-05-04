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
import { IoIosStats } from "react-icons/io";
import { Icon } from "@chakra-ui/react";
import Link from "next/link";
import { useAuth } from "@/contexts/AuthContext";

export const TEXT_profile_accessible_name = "Profile Button";
export const TEXT_ranking_accessible_name = "Leaderboard Button";

// NavBar component
export default function NavBar() {
  const { colorMode, toggleColorMode } = useColorMode();
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");
  const {currentUser, userInfo, methods} = useAuth();

  return (
    <>
      <Box bg={"transparent"} px={4}>
        <Flex h={16} alignItems={"center"} justifyContent={"space-between"}>
          <Link href='/#' className="logo" color={icon_color}>SYNTAX</Link>

          <Flex alignItems={"center"}>
            <Stack direction={"row"} spacing={6}>
              <Button as='a' href='/leaderboard' bg={"transparent"} aria-label={TEXT_ranking_accessible_name}>
                  <Icon as={IoIosStats} color={icon_color} />
              </Button>
              <Button onClick={toggleColorMode} bg={"transparent"}>
                {colorMode === "dark" ? <SunIcon color={icon_color}/> : <MoonIcon color={icon_color}/>}
              </Button>
              <Button as='a' href='/profile' bg={"transparent"} aria-label={TEXT_profile_accessible_name}>
                <Avatar
                  size={'sm'}
                  name={userInfo?.name ? userInfo?.name : undefined}
                  src={userInfo?.pic ? userInfo?.pic : undefined}
                />
              </Button>
            </Stack>
          </Flex>
        </Flex>
      </Box>
    </>
  );
}
