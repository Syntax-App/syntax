import { ReactNode } from "react";
import {
  Box,
  Flex,
  Link,
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

const NavLink = ({ children }: { children: ReactNode }) => (
  <Link
    px={2}
    py={1}
    rounded={"md"}
    _hover={{
      textDecoration: "none",
      bg: useColorModeValue("gray.200", "gray.700"),
    }}
    href={"#"}
  >
    {children}
  </Link>
);

export default function NavBar() {
  const { colorMode, toggleColorMode } = useColorMode();
  const { isOpen, onOpen, onClose } = useDisclosure();

  const nav_color = useColorModeValue("light.extralight", "dark.blue");
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");

  return (
    <>
      <Box bg={nav_color} px={4}>
        <Flex h={16} alignItems={"center"} justifyContent={"space-between"}>
          <Box color={icon_color}>SYNTAX</Box>

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
                  <MenuItem>Your Profile</MenuItem>
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
