import React from "react";
import { app as firebaseApp } from "../config/firebase";
import {
  UserCredential,
  createUserWithEmailAndPassword,
  getAuth,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { Button, ChakraProvider } from "@chakra-ui/react";

export default function signup() {
  const auth = getAuth(firebaseApp);

  function signup() {
    createUserWithEmailAndPassword(
      auth,
      "daniel_liu2@brown.edu",
      "supersecurepassword"
    )
      .then((userCredentials: UserCredential) => {
        console.log(userCredentials.user.email);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  return (
    <>
      <ChakraProvider>
        <Button color="teal" size="sm" onClick={signup}>
          Create Dan's Account!
        </Button>
      </ChakraProvider>
    </>
  );
}
