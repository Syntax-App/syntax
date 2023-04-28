import React from "react";
import { app as firebaseApp } from "../config/firebase";
import {
  UserCredential,
  createUserWithEmailAndPassword,
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { useState } from "react";

import { MdBuild, MdCall } from "react-icons/md";
import { Button, Box, Flex, Input, InputGroup, InputRightElement, Divider, ButtonGroup, ChakraProvider, useColorModeValue } from "@chakra-ui/react";
import { FcGoogle } from "react-icons/fc";
import { AiFillEyeInvisible, AiFillEye } from "react-icons/ai";
import { IAuthContext, useAuth } from "@/contexts/AuthContext";
import { Text } from "@chakra-ui/react";
import { useRouter } from "next/router";
import Link from "next/link";

export default function Login() {
  const { currentUser, methods } = useAuth();
  const [show, setShow] = useState(false);
  const [pass, setPass] = useState("");
  const [emptyFields, setEmptyFields] = useState(false);
  const [email, setEmail] = useState("");


  const router = useRouter();
  const handleClick = () => setShow(!show)
  
  async function login(isGoogle: boolean) {
    isGoogle ? await methods?.googleLogin() : methods?.emailLogin();
    await router.push("/");
  }

  async function handleLogin() {
    if (email !== "" && pass !== "") {
      setEmptyFields(false);
      await login(false);
    } else {
      setEmptyFields(true);
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
        bg={"#7786AE"}
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
            color={useColorModeValue("light.lightblue", "dark.darkblue")}
          >
            Welcome
          </Text>
        </Flex>
        <Input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          variant={"solid"}
          placeholder="USERNAME"
          _placeholder={{
            fontFamily: "source code pro",
            color: useColorModeValue("light.lightblue", "dark.dullblue"),
          }}
          color={useColorModeValue("light.lightblue", "dark.dullblue")}
          bg={useColorModeValue("light.lightblue", "dark.blue")}
          isRequired={true}
        />
        <InputGroup size="md" mt="1rem" mb=".3rem">
          <Input
            type={show ? "text" : "password"}
            value={pass}
            onChange={(e) => setPass(e.target.value)}
            variant={"solid"}
            placeholder="PASSWORD"
            _placeholder={{
              fontFamily: "source code pro",
              color: useColorModeValue("light.lightblue", "dark.dullblue"),
            }}
            color={useColorModeValue("light.lightblue", "dark.dullblue")}
            bg={useColorModeValue("light.lightblue", "dark.blue")}
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
            color="#4C597B"
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
          width="100%"
          borderRadius="3rem"
          bg="#83BFF6"
          my="1.8rem"
          onClick={() => handleLogin()}
        >
          Sign In
        </Button>

        <Divider variant={"thick"} />
        <Button
          width="100%"
          borderRadius="3rem"
          leftIcon={<FcGoogle size="1.2rem" />}
          bg="#DBE7FF"
          my="1.8rem"
          onClick={() => login(true)}
        >
          Sign in with Google
        </Button>
      </Box>
      <Text color="#7786AE" mt=".3rem">
        New to syntax? &nbsp; &nbsp;<a>Join Now</a>
      </Text>
    </Flex>
  );
}
