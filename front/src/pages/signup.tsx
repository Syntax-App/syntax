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
  useColorModeValue,
  ButtonGroup,
  ChakraProvider,
} from "@chakra-ui/react";
import { FcGoogle } from "react-icons/fc";
import { AiFillEyeInvisible, AiFillEye } from "react-icons/ai";
import { useAuth } from "@/contexts/AuthContext";
import { Text } from "@chakra-ui/react";
import { useRouter } from "next/router";

export default function Signup() {
  const { currentUser, methods } = useAuth();
  const [show, setShow] = React.useState(false);
  const [pass, setPass] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const [invalidSignup, setInvalidSignup] = useState(false);
  const [emptyFields, setEmptyFields] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");

  const router = useRouter();

  async function handleLogin() {
    // this is necessarily logging in with google, as this is the signup page
    await methods?.googleLogin();
    await router.push("/");
  }

  async function handleSignUp(){
    if (pass == confirmPass) setInvalidSignup(false);
      if (
        pass == confirmPass &&
        name !== "" &&
        email !== "" &&
        pass !== "" &&
        confirmPass !== ""
      ) {
        // successfully register user
        await methods?.emailSignup(name, email, pass);
        await router.push("/");
      } else if (pass != confirmPass || pass === "" || confirmPass === "") {
        setInvalidSignup(true);
      } else {
        setEmptyFields(true);
        return;
      }
  }

  const handleClick = () => setShow(!show);

  return (
    <Flex
      flexDirection="row"
      alignItems="center"
      overflow="-moz-hidden-unscrollable"
      height={"100vh"}
    >
      {/* LEFT SIDE */}
      <Flex
        flexDirection="column"
        alignItems={"center"}
        justifyContent={"center"}
        width={"60%"}
        height={"100%"}
      >
        <Flex
          width="25vw"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
          mt="3rem"
        >
          <Text>{currentUser?.email}</Text>
          <Box
            bg={useColorModeValue("light.extraLight", "dark.darkblue")}
            height="80%"
            maxH="80%"
            width="100%"
            display="flex"
            flexDirection="column"
            alignItems="space-between"
            justifyContent="center"
            px="2.3rem"
            pb=".3rem"
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
                color={useColorModeValue("light.darkGrey", "dark.vibrantblue")}
              >
                Join Syntax
              </Text>
            </Flex>
            <Input
              value={name}
              onChange={(e) => setName(e.target.value)}
              variant={"solid"}
              placeholder="NAME"
              _placeholder={{
                fontFamily: "source code pro",
                color: useColorModeValue("light.mediumGrey", "dark.darkblue"),
              }}
              color={useColorModeValue("light.darkGrey", "dark.darkblue")}
              bg={useColorModeValue("light.backgroundGrey", "light.extralight")}
              isRequired={true}
            />
            <Input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              mt="1rem"
              variant={"solid"}
              placeholder="EMAIL"
              _placeholder={{
                fontFamily: "source code pro",
                color: useColorModeValue("light.mediumGrey", "dark.darkblue"),
              }}
              bg={useColorModeValue(
                "light.backgroundGrey",
                "dark.mediumlightblue"
              )}
              isRequired={true}
            />
            <InputGroup size="md" mt="1rem">
              <Input
                value={pass}
                onChange={(e) => setPass(e.target.value)}
                type={show ? "text" : "password"}
                placeholder="PASSWORD"
                variant={"solid"}
                _placeholder={{
                  fontFamily: "source code pro",
                  color: useColorModeValue("light.mediumGrey", "dark.darkblue"),
                }}
                bg={useColorModeValue(
                  "light.backgroundGrey",
                  "dark.mediumlightblue"
                )}
                isRequired={true}
              />
              <InputRightElement width="2.8rem">
                {show ? (
                  <AiFillEye
                    size="1.5rem"
                    opacity={"70%"}
                    color="#2A3656"
                    onClick={handleClick}
                    cursor={"pointer"}
                  >
                    {show ? "Hide" : "Show"}
                  </AiFillEye>
                ) : (
                  <AiFillEyeInvisible
                    size="1.5rem"
                    opacity={"70%"}
                    color="#2A3656"
                    onClick={handleClick}
                    cursor={"pointer"}
                  >
                    {show ? "Hide" : "Show"}
                  </AiFillEyeInvisible>
                )}
              </InputRightElement>
            </InputGroup>
            <InputGroup size="md" mt="1rem" mb=".3rem">
              <Input
                value={confirmPass}
                onChange={(e) => setConfirmPass(e.target.value)}
                type={show ? "text" : "password"}
                placeholder="CONFIRM PASSWORD"
                variant={"solid"}
                _placeholder={{
                  fontFamily: "source code pro",
                  color: useColorModeValue("light.mediumGrey", "dark.darkblue"),
                }}
                bg={useColorModeValue(
                  "light.backgroundGrey",
                  "dark.mediumlightblue"
                )}
                isRequired={true}
              />
              <InputRightElement width="2.8rem">
                {show ? (
                  <AiFillEye
                    size="1.5rem"
                    opacity={"70%"}
                    color="#2A3656"
                    onClick={handleClick}
                    cursor={"pointer"}
                  >
                    {show ? "Hide" : "Show"}
                  </AiFillEye>
                ) : (
                  <AiFillEyeInvisible
                    size="1.5rem"
                    opacity={"70%"}
                    color="#2A3656"
                    onClick={handleClick}
                    cursor={"pointer"}
                  >
                    {show ? "Hide" : "Show"}
                  </AiFillEyeInvisible>
                )}
              </InputRightElement>
            </InputGroup>
            <Text
              display={invalidSignup ? "show" : "none"}
              color="#ee6f2a"
              fontSize={".8rem"}
            >
              * Passwords should match. Please try again.
            </Text>
            <Text
              display={emptyFields ? "show" : "none"}
              color="#ee6f2a"
              fontSize={".8rem"}
            >
              * Please enter all fields.
            </Text>

            <Button
              width="100%"
              borderRadius="3rem"
              bg={useColorModeValue("light.darkGrey", "#83BFF6")}
              color={useColorModeValue("light.extraLight", "dark.blue")}
              my="1.8rem"
              onClick={() => handleSignUp()}
            >
              Create Account
            </Button>
          </Box>

          <Divider variant={"thick"} width="90%" />
          <Button
            width="80%"
            borderRadius="3rem"
            leftIcon={<FcGoogle size="1.2rem" />}
            bg={useColorModeValue("light.extraLight", "#DBE7FF")}
            color={"#7786AE"}
            onClick={() => handleLogin()}
          >
            Sign in with Google
          </Button>
          <Text
            color={useColorModeValue("light.mediumGrey", "#7786AE")}
            mt="3rem"
          >
            Already a Syntax user? &nbsp; &nbsp;<a>Sign In</a>
          </Text>
        </Flex>
      </Flex>
      {/* LEFT SIDE */}
      <Flex
        flexDirection="column"
        alignItems={"center"}
        justifyContent={"center"}
        width={"40%"}
      >
        <Box
          bg={useColorModeValue("light.lightGrey", "dark.darkblue")}
          height={"100vh"}
          borderLeftRadius={"2rem"}
          width="100%"
          display="flex"
          flexDir={"column"}
          alignItems={"center"}
          justifyContent={"center"}
          px="8rem"
        >
          <Text
            variant="header"
            color={useColorModeValue("light.darkGrey", "dark.vibrantblue")}
          >
            SYNTAX
          </Text>
          <br></br>
          <br></br>
          <Text
            variant="signupDescription"
            color={useColorModeValue("light.darkGrey", "dark.mediumlightblue")}
          >
            Higher productivity, fluency with syntax, and building of muscle
            memory are all improvements to be gained from practicing typing out
            code, while the help of AI gives users the chance to learn about
            what they’ve typed out.
          </Text>
        </Box>
      </Flex>
    </Flex>
  );
}

