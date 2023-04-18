import React from "react";
import { app as firebaseApp } from "../config/firebase";
import {
  UserCredential,
  createUserWithEmailAndPassword,
  getAuth,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { Button, ChakraProvider } from "@chakra-ui/react";
import { useAuth } from "@/contexts/AuthContext";

export default function signup() {
  const {currentUser, methods} = useAuth();

  async function signup() {
    methods?.emailSignup();
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
