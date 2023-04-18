import { ReactNode } from "react";
import {
  Box,
  Flex,
  Button,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  MenuDivider,
  useDisclosure,
  useColorModeValue,
  Stack,
  useColorMode,
  Center,
} from "@chakra-ui/react";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import { FaUser } from "react-icons/fa";
import { IoIosStats } from "react-icons/io";
import { Icon } from "@chakra-ui/react";
import Link from "next/link";

export default function NavBar() {
  const { colorMode, toggleColorMode } = useColorMode();
  const { isOpen, onOpen, onClose } = useDisclosure();

  const nav_color = useColorModeValue("light.extralight", "dark.blue");
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");

  return (
    <>
      <Box bg={"transparent"} px={4}>
        <Flex h={16} alignItems={"center"} justifyContent={"space-between"}>
          <Link href={'/#'} className="logo" color={icon_color}>SYNTAX</Link>

          <Flex alignItems={"center"}>
            <Stack direction={"row"} spacing={6}>
              <Button bg={"transparent"}>
                  <Icon as={IoIosStats} color={icon_color} />
              </Button>
              <Button onClick={toggleColorMode} bg={"transparent"}>
                {colorMode === "dark" ? <SunIcon color={icon_color}/> : <MoonIcon color={icon_color}/>}
              </Button>
              <Menu>
                <MenuButton
                  as={Button}
                  rounded={"full"}
                  variant={"link"}
                  cursor={"pointer"}
                  minW={0}
                >
                  <Icon as={FaUser} color={icon_color} />
                </MenuButton>
                <MenuList alignItems={"center"}>
                  <br />
                  <Center>
                    <Icon as={FaUser} />
                  </Center>
                  <br />
                  <Center>
                    <p>Username</p>
                  </Center>
                  <MenuDivider />
                  <MenuItem as='a' href='/profile'>Your Profile</MenuItem>
                  <MenuItem>Account Settings</MenuItem>
                  <MenuItem>Logout</MenuItem>
                </MenuList>
              </Menu>
            </Stack>
          </Flex>
        </Flex>
      </Box>
    </>
  );
}
