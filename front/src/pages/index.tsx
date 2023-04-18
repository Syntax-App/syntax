import React from "react";
import { ChakraProvider } from "@chakra-ui/react";
import {
  Text,
  Card,
  CardBody,
  Image,
  Stack,
  Heading,
  Divider,
  CardFooter,
  ButtonGroup,
  Button,
} from "@chakra-ui/react";
import { getAuth } from "firebase/auth";
import { app as firebaseApp } from "../config/firebase";
import { useAuth } from "@/contexts/AuthContext";
import NavBar from "@/components/NavBar";

export default function Home() {
  const {currentUser, methods} = useAuth();
  return (
    <>
      <NavBar/>
      <Text fontSize="3xl">Welcome to Syntax!</Text>
      {currentUser ? <Text>You are logged in as {currentUser.email}</Text> : <Text>Please log in!</Text>}
      {currentUser && <Button color="crimson" size="lg" onClick={methods?.signout}>Sign out</Button>}
      {!currentUser && <Button color="crimson" size="lg" onClick={methods?.googleLogin}>Google Login</Button>}
      <Card maxW="2xs">
        <CardBody>
          <Image
            src="https://images.unsplash.com/photo-1555041469-a586c61ea9bc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80"
            alt="Green double couch with wooden legs"
            borderRadius="lg"
          />
          <Stack mt="6" spacing="3">
            <Heading size="sm">Living room Sofa</Heading>
            <Text fontSize="sm">
              This sofa is perfect for modern tropical spaces, baroque inspired
              spaces, earthy toned spaces and for people who love a chic design
              with a sprinkle of vintage design.
            </Text>
            <Text color="blue.600" fontSize="md">
              $450
            </Text>
          </Stack>
        </CardBody>
        <Divider />
        <CardFooter>
          <ButtonGroup spacing="2">
            <Button size="sm" variant="solid" colorScheme="blue">
              Buy now
            </Button>
            <Button size="sm" variant="ghost" colorScheme="blue">
              Add to cart
            </Button>
          </ButtonGroup>
        </CardFooter>
      </Card>
    </>
  );
}
