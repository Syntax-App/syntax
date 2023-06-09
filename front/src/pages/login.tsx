import React from "react";
import { useState } from "react";
import {
  Button,
  Box,
  Flex,
  Input,
  InputGroup,
  InputRightElement,
  Divider,
  ButtonGroup,
  ChakraProvider,
  useColorModeValue,
} from "@chakra-ui/react";
import { FcGoogle } from "react-icons/fc";
import { AiFillEyeInvisible, AiFillEye } from "react-icons/ai";
import { useAuth } from "@/contexts/AuthContext";
import { Text } from "@chakra-ui/react";
import { useRouter } from "next/router";

export const TEXT_login_accessible_name = "Login Button";

export default function Login() {
  const { currentUser, methods } = useAuth();
  const [show, setShow] = useState(false);
  const [pass, setPass] = useState("");
  const [emptyFields, setEmptyFields] = useState(false);
  const [email, setEmail] = useState("");

  const router = useRouter();
  const handleClick = () => setShow(!show);

  async function handleLogin(isGoogle: boolean) {
    if (isGoogle) {
      await methods?.googleLogin();
      await router.push("/");

    } else {
      if (email !== "" && pass !== "") {
        setEmptyFields(false);
        await methods?.emailLogin(email, pass);
        await router.push("/");

      } else {
        setEmptyFields(true);
      }
    }
  }

  return (
    <Flex
      justifyContent="center"
      flexDirection="column"
      alignItems="center"
      overflow="-moz-hidden-unscrollable"
    >
      <Text>{currentUser?.email}</Text>
      <Box
        bg={useColorModeValue("light.mediumGrey", "#7786AE")}
        width="25%"
        height="80%"
        maxH="80%"
        display="flex"
        flexDirection="column"
        alignItems="space-between"
        justifyContent="center"
        px="2.3rem"
        pb=".3rem"
        mt="8%"
        borderRadius="1.7rem"
      >
        <Flex
          height="9rem"
          mt=".5rem"
          alignItems="center"
          justifyContent="center"
        >
          <Text
            variant="header"
            color={useColorModeValue("light.backgroundGrey", "dark.darkblue")}
          >
            Welcome
          </Text>
        </Flex>
        <Input
          data-testid="testid_username"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          variant={"solid"}
          placeholder="EMAIL"
          _placeholder={{
            fontFamily: "source code pro",
            color: useColorModeValue("light.darkGrey", "dark.dullblue"),
          }}
          color={useColorModeValue("light.darkGrey", "dark.dullblue")}
          bg={useColorModeValue("light.extraLight", "dark.blue")}
          isRequired={true}
        />
        <InputGroup size="md" mt="1rem" mb=".3rem">
          <Input
            data-testid="testid_password"
            type={show ? "text" : "password"}
            value={pass}
            onChange={(e) => setPass(e.target.value)}
            variant={"solid"}
            placeholder="PASSWORD"
            _placeholder={{
              fontFamily: "source code pro",
              color: useColorModeValue("light.darkGrey", "dark.dullblue"),
            }}
            color={useColorModeValue("light.darkGrey", "dark.dullblue")}
            bg={useColorModeValue("light.extraLight", "dark.blue")}
            isRequired={true}
          />
          <InputRightElement width="2.8rem">
            {show ? (
              <AiFillEye
                size="1.5rem"
                opacity={"70%"}
                color="#7786AE"
                onClick={handleClick}
                cursor={"pointer"}
              >
                {show ? "Hide" : "Show"}
              </AiFillEye>
            ) : (
              <AiFillEyeInvisible
                size="1.5rem"
                opacity={"70%"}
                color="#7786AE"
                onClick={handleClick}
                cursor={"pointer"}
              >
                {show ? "Hide" : "Show"}
              </AiFillEyeInvisible>
            )}
          </InputRightElement>
        </InputGroup>
        <Flex justifyContent={"flex-start"} alignItems={"flex-start"}>
          <Text
            color={useColorModeValue("light.darkGrey", "#4C597B")}
            fontWeight={"bold"}
            cursor={"pointer"}
            fontSize={".8rem"}
          >
            Forgot Password?
          </Text>
        </Flex>
        <Text
          display={emptyFields ? "show" : "none"}
          color="#fff"
          fontSize={".8rem"}
        >
          * Please enter all fields.
        </Text>
        <Button
          aria-label={TEXT_login_accessible_name}
          width="100%"
          borderRadius="3rem"
          bg={useColorModeValue("light.darkGrey", "#83BFF6")}
          color={useColorModeValue("light.backgroundGrey", "dark.blue")}
          my="1.8rem"
          onClick={() => handleLogin(false)}
        >
          Sign In
        </Button>

        <Divider variant={"thick"} />
        <Button
          width="100%"
          borderRadius="3rem"
          leftIcon={<FcGoogle size="1.2rem" />}
          bg={useColorModeValue("light.extraLight", "#DBE7FF")}
          color={"#7786AE"}
          my="1.8rem"
          onClick={() => handleLogin(true)}
        >
          Sign in with Google
        </Button>
      </Box>
      <Text color={useColorModeValue("light.mediumGrey", "#7786AE")} mt=".3rem">
        New to syntax? &nbsp; &nbsp;<a href="/signup">Join Now</a>
      </Text>
    </Flex>
  );
}
